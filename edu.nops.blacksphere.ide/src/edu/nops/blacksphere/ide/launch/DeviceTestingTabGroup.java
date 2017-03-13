package edu.nops.blacksphere.ide.launch;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
//import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;

public class DeviceTestingTabGroup implements ILaunchConfigurationTabGroup {

	DeviceLaunchTab deviceTab;
	//CommonTab commontab;
	
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		deviceTab = new DeviceLaunchTab(); 
		//commontab = new CommonTab();
	}

	public void dispose() { }

	public ILaunchConfigurationTab[] getTabs() {
		return new ILaunchConfigurationTab[] {/*commontab,*/deviceTab};
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		deviceTab.initializeFrom(configuration);
	}

	public void launched(ILaunch launch) { }

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		deviceTab.performApply(configuration);
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		deviceTab.setDefaults(configuration);
	}

}
