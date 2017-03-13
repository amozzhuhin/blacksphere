package edu.nops.blacksphere.emu;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
        configurer.setShowPerspectiveBar(false);
        configurer.setShowMenuBar(false);
        configurer.setShowFastViewBars(false);
        configurer.setTitle(EmuMessages.getString("ApplicationWorkbenchWindowAdvisor.windowTitle")); //$NON-NLS-1$
    }

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
        IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		try {
			workbench.showPerspective(BSPE.TEST_DEVICE_PERSPECTIVE_ID, window);
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}
	}
    
}
