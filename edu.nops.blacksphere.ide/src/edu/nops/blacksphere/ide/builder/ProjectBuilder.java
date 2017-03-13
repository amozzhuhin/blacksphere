package edu.nops.blacksphere.ide.builder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.core.device.DeviceTransform;
import edu.nops.blacksphere.ide.BSP;
import edu.nops.blacksphere.ide.BSPMessages;

public class ProjectBuilder extends IncrementalProjectBuilder {

	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		// TODO обработка неполного построения (инкрементальный построитель)
		fullBuild(monitor);
		return null;
	}

	/** Получить строку запуска компилятора */
	protected String getCompileCommand() {
		String separator = System.getProperty("file.separator"); //$NON-NLS-1$
		String src = BSP.SOURCE_FOLDER_NAME + separator
				+ BSP.FIRMWARE_SOURCE_FILE_NAME;
		String lst = BSP.BINARY_FOLDER_NAME + separator
				+ BSP.FIRMWARE_LST_FILE_NAME;
		String bin = BSP.BINARY_FOLDER_NAME + separator
				+ BSP.FIRMWARE_BINARY_FILE_NAME;
		String command = "nasm"  // команда запуска компилятора //$NON-NLS-1$
			+ " -f bin"            // бинарный выходной формат //$NON-NLS-1$
			+ " -l " + lst // листинг //$NON-NLS-1$
			+ " -o " + bin // выходной файл //$NON-NLS-1$
			+ " " + src;   // входной файл //$NON-NLS-1$
		return command;
	}
	
	/** Удаление всех ранее установленных маркеров */
	private void deleteAllMarkers() {
		try {
			getProject().deleteMarkers(BSP.PROBLEM_MARKER_ID,
					true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/** Посититель ресурсов устанавливающий флаг вторичности */
	private class DerivedVisitor implements IResourceVisitor {
		private boolean derived;
		public DerivedVisitor(boolean derived) {
			this.derived = derived;
		}
		public boolean visit(IResource resource) throws CoreException {
			resource.setDerived(derived);
			return true;
		}
	}
	
	/** Полная сборка */
	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		deleteAllMarkers();
		updateDeviceConsts();
		File workdir = new File(getProject().getLocation().toOSString());
		try {
			String command = getCompileCommand();
			BSP.getConsoles().print(BSP.BUILD_CONSOLE_ID, "> " + command + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			Process process = Runtime.getRuntime().exec(command, null, workdir);
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BufferedReader stderr = new BufferedReader(
					new InputStreamReader(process.getErrorStream()));
			while (stderr.ready()) {
				String problem = stderr.readLine();
				BSP.getConsoles().print(BSP.BUILD_CONSOLE_ID, problem + "\n"); //$NON-NLS-1$
				parseProblem(problem);
			}
			stderr.close();
			getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
			// Установка вторичности всех файлов в каталоге bin 
			getProject().getFolder(BSP.BINARY_FOLDER_NAME).accept(new DerivedVisitor(true));
		} catch (IOException e) {
			e.printStackTrace();
			addMarker(getProject().getFolder(BSP.SOURCE_FOLDER_NAME)
					.getFile(BSP.FIRMWARE_SOURCE_FILE_NAME),
					BSPMessages.getString("ProjectBuilder.CompilerLaunchError") + e.getMessage(), //$NON-NLS-1$
					10, IMarker.SEVERITY_ERROR);
		}
	}
	
	/** Обновить файл констант устройства */
	protected void updateDeviceConsts() {
		IFile deviceFile = getProject().getFile(BSP.DEVICE_FILE_NAME);
		try {
			InputStreamReader reader = new InputStreamReader(deviceFile.getContents(), "UTF-8"); //$NON-NLS-1$
			Device device = DeviceTransform.loadDevice(reader);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			DeviceTransform.saveDeviceConsts(device, os);
			IFile constsFile = getProject().getFolder(BSP.INCLUDE_FOLDER_NAME)
					.getFile(BSP.DEVICE_CONSTS_FILE_NAME);
			if (!constsFile.exists())
				constsFile.create(new ByteArrayInputStream(os.toByteArray()),
						IFile.FORCE, null);
			else
				constsFile.setContents(new ByteArrayInputStream(os.toByteArray()),
						IFile.FORCE, null);
			constsFile.setDerived(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Разбор сообщения о проблеме компиляции, вида:
	 * /path/to/file:10: warning: ignoring unknown section attribute: "start" 
	 */
	private void parseProblem(String problem) {
		StringTokenizer st = new StringTokenizer(problem, ":"); //$NON-NLS-1$
		String filename = st.nextToken().trim();
		int lineNumber = Integer.parseInt(st.nextToken());
		String problemType = st.nextToken().trim();
		int severity = IMarker.SEVERITY_ERROR;
		if (problemType == "warning") { //$NON-NLS-1$
			severity = IMarker.SEVERITY_WARNING;
		}
		String message = st.nextToken();
		while (st.hasMoreTokens()) {
			message += ":" + st.nextToken(); //$NON-NLS-1$
		}
		addMarker(getProject().getFile(new Path(filename)),
				message, lineNumber, severity);
	}

	private void addMarker(IFile file, String message, int lineNumber,
			int severity) {
		try {
			IMarker marker = file.createMarker(BSP.PROBLEM_MARKER_ID);
			marker.setAttribute(IMarker.MESSAGE, message);
			marker.setAttribute(IMarker.SEVERITY, severity);
			if (lineNumber == -1) {
				lineNumber = 1;
			}
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			// TODO подчёркивание ошибки  
		} catch (CoreException e) {
		}
	}
}
