package edu.nops.blacksphere.emu;

import java.io.File;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * Класс управляющий запуском эмулятора
 * @author nops
 */
public class EmulatorApplication implements IPlatformRunnable {

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IPlatformRunnable#run(java.lang.Object)
	 */
	public Object run(Object args) throws Exception {
		System.err.println(EmuMessages.getString("EmulatorApplication.EmuStarting")); //$NON-NLS-1$
		Display display = PlatformUI.createDisplay();
		try {
			parseArgs();
			if (BSPE.getDevice() == null) {
				ErrorDialog dialog = new ErrorDialog(null,
						EmuMessages.getString("EmulatorApplication.EmuStartingError"), EmuMessages.getString("EmulatorApplication.deviceFileNotFound"), //$NON-NLS-1$ //$NON-NLS-2$
						new Status(Status.ERROR, BSPE.PLUGIN_ID, -1,
							EmuMessages.getString("EmulatorApplication.needSetDeviceParam"), null), //$NON-NLS-1$
						IStatus.ERROR);
				dialog.open();
				return IPlatformRunnable.EXIT_OK;
			}
			if (BSPE.getFirmware() == null) {
				ErrorDialog dialog = new ErrorDialog(null,
						EmuMessages.getString("EmulatorApplication.EmuStartingError"), EmuMessages.getString("EmulatorApplication.firmwareFileNotFound"), //$NON-NLS-1$ //$NON-NLS-2$
						new Status(Status.ERROR, BSPE.PLUGIN_ID, -1,
							EmuMessages.getString("EmulatorApplication.needSetFirmwareParam"), null), //$NON-NLS-1$
						IStatus.ERROR);
				dialog.open();
				return IPlatformRunnable.EXIT_OK;
			}
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IPlatformRunnable.EXIT_RESTART;
			}
			return IPlatformRunnable.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	public static String DEVICE_ARG = "-device";  //$NON-NLS-1$
	public static String FIRMWARE_ARG = "-firmware";  //$NON-NLS-1$
	
	public void parseArgs() {
		int i = 0;
		String[] args = Platform.getApplicationArgs();
		while (i < args.length) {
			if (args[i].equals(DEVICE_ARG) && i+1 < args.length) {
				BSPE.loadDevice(new File(args[i+1]));
				i++;
			} else if (args[i].equals(FIRMWARE_ARG) && i+1 < args.length) {
				BSPE.loadFirmware(new File(args[i+1]));
				i++;
			}
			i++;
		}
	}
}
