/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package edu.nops.blacksphere.ide.launch.eclipse;

import java.io.*;
import java.net.*;
import java.nio.channels.*;


/**
 * This program is used to start or stop Eclipse Infocenter application. It
 * should be launched from command line.
 */
public class EclipseController implements EclipseLifeCycleListener {
	public static final String CMD_INSTALL = "install"; //$NON-NLS-1$

	public static final String CMD_UPDATE = "update"; //$NON-NLS-1$

	public static final String CMD_ENABLE = "enable"; //$NON-NLS-1$

	public static final String CMD_DISABLE = "disable"; //$NON-NLS-1$

	public static final String CMD_UNINSTALL = "uninstall"; //$NON-NLS-1$

	public static final String CMD_SEARCH = "search"; //$NON-NLS-1$

	public static final String CMD_LIST = "listFeatures"; //$NON-NLS-1$

	public static final String CMD_ADDSITE = "addSite"; //$NON-NLS-1$

	public static final String CMD_REMOVESITE = "removeSite"; //$NON-NLS-1$

	public static final String CMD_APPLY = "apply"; //$NON-NLS-1$

	// application to launch
	protected String applicationId;

	// Eclipse connection params
	protected EclipseConnection connection;

	public Eclipse eclipse = null;

	// Inter process lock
	private FileLock lock;

	private boolean eclipseEnded = false;

	/**
	 * Constructs help system
	 * 
	 * @param applicationId
	 *            ID of Eclipse help application
	 * @param args
	 *            array of String options and their values Option
	 *            <code>-eclipseHome dir</code> specifies Eclipse installation
	 *            directory. It must be provided, when current directory is not
	 *            the same as Eclipse installation directory. Additionally, most
	 *            options accepted by Eclipse execuable are supported.
	 */
	public EclipseController(String applicationId, String[] args) {

		this.applicationId = applicationId;
		Options.init(applicationId, args);
		connection = new EclipseConnection();
	}

	/**
	 * @see org.eclipse.help.standalone.Help#shutdown()
	 */
	public final synchronized void shutdown() throws Exception {
		try {
			obtainLock();
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} finally {
			releaseLock();
		}
	}

	/**
	 * @see org.eclipse.help.standalone.Help#start()
	 */
	public final synchronized void start() throws Exception {
		try {
			obtainLock();
			startEclipse();
		} finally {
			releaseLock();
		}

	}

	/**
	 * Starts Eclipse if not yet running.
	 */
	private void startEclipse() throws Exception {
		boolean fullyRunning = isApplicationRunning();
		if (fullyRunning) {
			return;
		}
		if (Options.isDebug()) {
			System.out
					.println("Using workspace " + Options.getWorkspace().getAbsolutePath()); //$NON-NLS-1$
		}
		// delete old connection file
		Options.getConnectionFile().delete();
		connection.reset();

		if (Options.isDebug()) {
			System.out
					.println("Ensured old .connection file is deleted.  Launching Eclipse."); //$NON-NLS-1$
		}
		eclipseEnded = false;
		eclipse = new Eclipse(this);
		eclipse.start();
		fullyRunning = isApplicationRunning();
		while (!eclipseEnded && !fullyRunning) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException ie) {
			}
			fullyRunning = isApplicationRunning();
		}
		if (eclipseEnded) {
			if (eclipse.getStatus() == Eclipse.STATUS_ERROR) {
				throw eclipse.getException();
			}
			return;
		}
		if (Options.isDebug()) {
			System.out.println("Eclipse launched"); //$NON-NLS-1$
		}
		// in case controller is killed
		Runtime.getRuntime().addShutdownHook(new EclipseCleaner());
	}

	public void eclipseEnded() {
		eclipseEnded = true;
		connection.reset();
	}

	private void obtainLock() throws IOException {
		if (lock != null) {
			// we already have lock
			return;
		}
		if (!Options.getLockFile().exists()) {
			Options.getLockFile().getParentFile().mkdirs();
		}
		RandomAccessFile raf = new RandomAccessFile(Options.getLockFile(), "rw"); //$NON-NLS-1$
		lock = raf.getChannel().lock();
		if (Options.isDebug()) {
			System.out.println("Lock obtained."); //$NON-NLS-1$
		}
	}

	private void releaseLock() {
		if (lock != null) {
			try {
				lock.channel().close();
				if (Options.isDebug()) {
					System.out.println("Lock released."); //$NON-NLS-1$
				}
				lock = null;
			} catch (IOException ioe) {
			}
		}
	}

	/**
	 * Tests whether HelpApplication is running by testing if .applicationlock
	 * is locked
	 */
	private boolean isApplicationRunning() {
		File applicationLockFile = new File(Options.getLockFile()
				.getParentFile(), ".applicationlock"); //$NON-NLS-1$
		RandomAccessFile randomAccessFile = null;
		FileLock applicationLock = null;
		try {
			randomAccessFile = new RandomAccessFile(applicationLockFile, "rw"); //$NON-NLS-1$
			applicationLock = randomAccessFile.getChannel().tryLock();
		} catch (IOException ioe) {
		} finally {
			if (applicationLock != null) {
				try {
					applicationLock.release();
				} catch (IOException ioe) {
				}
			}
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException ioe) {
				}
			}
			if (Options.isDebug()) {
				System.out
						.println("isApplicationRunning? " + (applicationLock == null)); //$NON-NLS-1$
			}
		}
		return applicationLock == null;
	}

	public class EclipseCleaner extends Thread {
		public void run() {
			if (eclipse != null) {
				eclipse.killProcess();
			}
		}
	}

}
