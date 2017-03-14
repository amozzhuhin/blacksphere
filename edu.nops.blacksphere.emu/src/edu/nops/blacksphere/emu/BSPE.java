package edu.nops.blacksphere.emu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.core.device.DeviceTransform;

/**
 * The activator class controls the plug-in life cycle
 */
public class BSPE extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "edu.nops.blacksphere.emu"; //$NON-NLS-1$

	// The shared instance
	private static BSPE plugin;
	
	/**
	 * The constructor
	 */
	public BSPE() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static BSPE getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public static final String TEST_DEVICE_PERSPECTIVE_ID
			= "edu.nops.blacksphere.emu.TestDevicePerspective"; //$NON-NLS-1$
	
	public static final String FACE_VIEW_ID
			= "edu.nops.blacksphere.emu.FaceView"; //$NON-NLS-1$
	
	public static final String EMU_CONSOLE_ID =
		EmuMessages.getString("BSPE.emuConsole"); //$NON-NLS-1$
	
	private static Device device = null; 
	private static InputStream firmware = null;
	
	public static void loadDevice(File file) {
		try {
			FileInputStream deviceInputStream = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(deviceInputStream, "UTF-8"); //$NON-NLS-1$
			device = DeviceTransform.loadDevice(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Device getDevice() {
		return device;
	}
	
	public static void loadFirmware(File file) {
		try {
			firmware = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static InputStream getFirmware() {
		return firmware;
	}
}
