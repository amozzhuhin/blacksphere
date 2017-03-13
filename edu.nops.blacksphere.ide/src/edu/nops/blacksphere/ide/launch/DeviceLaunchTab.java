package edu.nops.blacksphere.ide.launch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourceSorter;

import edu.nops.blacksphere.ide.BSPMessages;

/**
 * Страница настройки запуска "Устройство"
 * @author nops
 *
 */
public class DeviceLaunchTab extends AbstractLaunchConfigurationTab {

	public static final String DEVICE_ATTRIBUTE = "device"; //$NON-NLS-1$
	public static final String FIRMWARE_ATTRIBUTE = "firmware"; //$NON-NLS-1$
	
	private Label deviceLabel;
	private Text deviceText;
	private Button deviceButton;
	
	private Label firmwareLabel;
	private Text firmwareText;
	private Button firmwareButton;
	
	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		//PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IDebugHelpContextIds.LAUNCH_CONFIGURATION_DIALOG_COMMON_TAB);
		GridLayout gl = new GridLayout(3, false);
		comp.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		comp.setFont(parent.getFont());
	
		deviceLabel = new Label(comp, SWT.NONE);
		deviceLabel.setText(BSPMessages.getString("DeviceLaunchTab.DeviceDescriptionFile")); //$NON-NLS-1$
		
		deviceText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		deviceText.setEditable(false);
		deviceText.setLayoutData(gd);
		
		deviceButton = createPushButton(comp, BSPMessages.getString("DeviceLaunchTab.Open"), null);	  //$NON-NLS-1$
		deviceButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleDeviceButtonSelected();
			}
		});	

		firmwareLabel = new Label(comp, SWT.NONE);
		firmwareLabel.setText(BSPMessages.getString("DeviceLaunchTab.firmwareFile")); //$NON-NLS-1$
		
		firmwareText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		firmwareText.setEditable(false);
		firmwareText.setLayoutData(gd);
		
		firmwareButton = createPushButton(comp, BSPMessages.getString("DeviceLaunchTab.Open"), null);	  //$NON-NLS-1$
		firmwareButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleFirmwareButtonSelected();
			}
		});	
	}

	/**
	 * Handles the shared location button being selected
	 */
	private void handleDeviceButtonSelected() { 
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
        dialog.setTitle(BSPMessages.getString("DeviceLaunchTab.selectResource"));  //$NON-NLS-1$
        dialog.setMessage(BSPMessages.getString("DeviceLaunchTab.selectDeviceDescriptionFile"));  //$NON-NLS-1$
        dialog.setInput(ResourcesPlugin.getWorkspace().getRoot()); 
        ResourceSorter sorter = new ResourceSorter(ResourceSorter.NAME);
        dialog.setSorter(sorter);
        dialog.addFilter(new ViewerFilter() {
		
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IFile && ((IFile)element).getFileExtension().equals("bsx")) //$NON-NLS-1$
					return true;
				return element instanceof IProject || element instanceof IFolder;
			}
		
		});
        if (dialog.open() == IDialogConstants.OK_ID) {
            IResource resource = (IResource) dialog.getFirstResult();
            String arg = resource.getLocation().toString();
            //String fileLoc = VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc", arg); //$NON-NLS-1$
            deviceText.setText(arg);
            setDirty(true);
            updateLaunchConfigurationDialog();
        }
	}

	/**
	 * Handles the shared location button being selected
	 */
	private void handleFirmwareButtonSelected() { 
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
        dialog.setTitle(BSPMessages.getString("DeviceLaunchTab.selectResource")); //$NON-NLS-1$
        dialog.setMessage(BSPMessages.getString("DeviceLaunchTab.SelectFirmwareFile")); //$NON-NLS-1$
        dialog.setInput(ResourcesPlugin.getWorkspace().getRoot()); 
        ResourceSorter sorter = new ResourceSorter(ResourceSorter.NAME);
        dialog.setSorter(sorter);
        dialog.addFilter(new ViewerFilter() {
		
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (element instanceof IFile && ((IFile)element).getFileExtension() == null)
					return true;
				return element instanceof IProject || element instanceof IFolder;
			}
		
		});
        if (dialog.open() == IDialogConstants.OK_ID) {
            IResource resource = (IResource) dialog.getFirstResult();
            String arg = resource.getLocation().toString();
            //String fileLoc = VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression("workspace_loc", arg); //$NON-NLS-1$
            firmwareText.setText(arg);
            setDirty(true);
            updateLaunchConfigurationDialog();
        }
	}

	public static final String TAB_NAME = BSPMessages.getString("DeviceLaunchTab.DeviceTabName"); //$NON-NLS-1$
	
	public String getName() {
		return TAB_NAME;
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		String device;
		try {
			device = configuration.getAttribute(DEVICE_ATTRIBUTE, ""); //$NON-NLS-1$
		} catch (CoreException e) {
			device = ""; //$NON-NLS-1$
		}
		deviceText.setText(device);
		String firmware;
		try {
			firmware = configuration.getAttribute(FIRMWARE_ATTRIBUTE, ""); //$NON-NLS-1$
		} catch (CoreException e) {
			firmware = ""; //$NON-NLS-1$
		}
		firmwareText.setText(firmware);
		updateLaunchConfigurationDialog();
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(DEVICE_ATTRIBUTE, deviceText.getText());
		configuration.setAttribute(FIRMWARE_ATTRIBUTE, firmwareText.getText());
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(DEVICE_ATTRIBUTE, ""); //$NON-NLS-1$
		configuration.setAttribute(FIRMWARE_ATTRIBUTE, ""); //$NON-NLS-1$
	}

	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);
		setMessage(null);
		return validateDevice() && validateFirmware();
	}

	private boolean validateDevice() {
		if (deviceText.getText().equals("")) { //$NON-NLS-1$
			setErrorMessage(BSPMessages.getString("DeviceLaunchTab.descriptionFileNotSet")); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	private boolean validateFirmware() {
		if (firmwareText.getText().equals("")) { //$NON-NLS-1$
			setErrorMessage(BSPMessages.getString("DeviceLaunchTab.firmwareFileNotSet")); //$NON-NLS-1$
			return false;
		}
		return true;
	}
	
	

}
