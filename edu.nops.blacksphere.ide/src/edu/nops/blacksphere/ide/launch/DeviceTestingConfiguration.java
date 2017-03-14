package edu.nops.blacksphere.ide.launch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;

/**
 * Делегат запуска тестирования устройства
 * @author nops
 */
public class DeviceTestingConfiguration implements
		ILaunchConfigurationDelegate2 {

	public boolean buildForLaunch(ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor) throws CoreException {
		return true;
	}

	public boolean finalLaunchCheck(ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor) throws CoreException {
		return true;
	}

	public ILaunch getLaunch(ILaunchConfiguration configuration, String mode)
			throws CoreException {
		return null;
	}

	public boolean preLaunchCheck(ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor) throws CoreException {
		return true;
	}

	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		ArrayList<String> args = new ArrayList<String>(6);
		// Путь к файлу запуска
		String eclipseHome = null; 
		try {
			eclipseHome = FileLocator.toFileURL(Platform.getInstallLocation().getURL()).getPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		args.add("-eclipseHome"); //$NON-NLS-1$
		args.add(eclipseHome);
		// Файл описания устройства
		String device = configuration.getAttribute(DeviceLaunchTab.DEVICE_ATTRIBUTE, ""); //$NON-NLS-1$
		if (!device.equals("")) { //$NON-NLS-1$
			args.add("-device"); //$NON-NLS-1$
			args.add(device);
		}
		// Файл прошивки устройства
		String firmware = configuration.getAttribute(DeviceLaunchTab.FIRMWARE_ATTRIBUTE, ""); //$NON-NLS-1$
		if (!firmware.equals("")) { //$NON-NLS-1$
			args.add("-firmware"); //$NON-NLS-1$
			args.add(firmware);
		}
		//StandaloneEmulator emu = new StandaloneEmulator();
		try {
			System.err.println("start emulator h:" + eclipseHome //$NON-NLS-1$
					+ " d:" + device //$NON-NLS-1$
					+ " f:" + firmware); //$NON-NLS-1$
			DebugPlugin.exec(new String[] {eclipseHome + "/blacksphere", //$NON-NLS-1$
					"-nosplash", //$NON-NLS-1$
					"-application", "edu.nops.blacksphere.emu.BlackSphereEmulator", //$NON-NLS-1$ //$NON-NLS-2$
					"-device", device, //$NON-NLS-1$
					"-firmware", firmware}, //$NON-NLS-1$
					new File(eclipseHome));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
