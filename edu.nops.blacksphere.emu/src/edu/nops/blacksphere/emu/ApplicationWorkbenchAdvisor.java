package edu.nops.blacksphere.emu;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "edu.nops.blacksphere.emu.TestDevicePerspective"; //$NON-NLS-1$
	
    @Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		// Сохранять состояние не нужно
        configurer.setSaveAndRestore(false);
	}

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	@Override
	public void preStartup() {
		super.preStartup();
		IWorkbench workbench = getWorkbenchConfigurer().getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (PlatformUI.isWorkbenchRunning());
					openDevice();
			}
		});
	}
	
	//private static final String DEVICE_EDITOR_ID = "edu.nops.blacksphere.editors.device.DeviceEditor"; //$NON-NLS-1$

	MicroPC pc;
	
	protected void openDevice() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = getWorkbenchConfigurer().getWorkbench()
				.getActiveWorkbenchWindow();
		try {
			workbench.showPerspective(BSPE.TEST_DEVICE_PERSPECTIVE_ID, window);
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}
		/*try {
			IFileStore store = EFS.getStore(new URI("file:///home/nops/Source/runtime-EclipseApplication/DiodeAndButton/device.bsx"));
			IEditorPart part = window.getActivePage().openEditor(new FileStoreEditorInput(store), DEVICE_EDITOR_ID);
			Device device = ((DeviceEditor) part).getDevice();
			// включение всех элементов устройства
			for (AbstractElement el : device.getElements())
				el.setPowered(true);
			File firmware = new File("/home/nops/Source/runtime-EclipseApplication/DiodeAndButton/bin/firmware");
		//	pc = new MicroPC(device, new FileInputStream(firmware));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
