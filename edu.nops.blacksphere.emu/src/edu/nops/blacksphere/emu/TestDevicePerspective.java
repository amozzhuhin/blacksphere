package edu.nops.blacksphere.emu;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class TestDevicePerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false); 
		layout.addStandaloneView(BSPE.FACE_VIEW_ID,
                 false, IPageLayout.TOP, 0.8f, layout.getEditorArea());
		layout.addView(IConsoleConstants.ID_CONSOLE_VIEW,
				IPageLayout.BOTTOM, 0.2f, layout.getEditorArea());
		layout.getViewLayout(IConsoleConstants.ID_CONSOLE_VIEW).setCloseable(false);
	}
}
