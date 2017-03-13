package edu.nops.blacksphere.ide.wizards;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import edu.nops.blacksphere.core.utils.InputStreamDecoder;
import edu.nops.blacksphere.ide.BSP;
import edu.nops.blacksphere.ide.BSPMessages;

/**
 * Мастер создания нового проекта
 * @author nops
 */
public class NewProjectWizard extends Wizard implements INewWizard, IExecutableExtension {

	private WizardNewProjectCreationPage creationPage;
	private ProjectTemplatePage templatePage;

	public void init(IWorkbench workbench, IStructuredSelection selection) { }

	/** Создание главного ассемблерного файла прошивки */
	private void createFirmwareFile(IProject project) {
		IFile firmware = project.getFolder(BSP.SOURCE_FOLDER_NAME).getFile(BSP.FIRMWARE_SOURCE_FILE_NAME);
		try {
			InputStream is = templatePage.getTemplateInputStream(BSP.FIRMWARE_SOURCE_FILE_NAME);
			firmware.create(InputStreamDecoder.decodeInputStream(is,
					"UTF-8", ResourcesPlugin.getEncoding()), //$NON-NLS-1$
					true, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Создание файла описания устройства
	 * из созданного устройства с полями по умолчанию
	 */
	private void createDeviceFile(IProject project) {
		IFile file = project.getFile(BSP.DEVICE_FILE_NAME);
		try {
			InputStream is = templatePage.getTemplateInputStream(BSP.DEVICE_FILE_NAME);
			file.create(is, true, null);
			file.setCharset("UTF-8", null); //$NON-NLS-1$
			// Открыть файл в среде 
			IWorkbenchWindow window = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();
			IDE.openEditor(page, file);
			// Перейти курсором на файл
			BasicNewResourceWizard.selectAndReveal(file, window);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	/** Назначение проекту конфигурации */
	private void setNature(IProject project) {
		try {
			IProjectDescription description = project.getDescription();
			description.setNatureIds(new String[] {BSP.NATURE_ID});
			project.setDescription(description, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	/** Создание каталогов по умолчанию */
	private void createPaths(IProject project) {
		try {
			IFolder folder = project.getFolder(BSP.SOURCE_FOLDER_NAME);
			folder.create(true, true, null);
			folder = project.getFolder(BSP.INCLUDE_FOLDER_NAME);
			folder.create(true, true, null);
			folder = project.getFolder(BSP.BINARY_FOLDER_NAME);
			folder.create(true, true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean performFinish() {
		// Создание нового проекта в корне рабочей области
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(creationPage.getProjectName());
		try {
			project.create(null);
			project.open(null);
			createPaths(project);
			createFirmwareFile(project);
			createDeviceFile(project);
			setNature(project);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		/* реализуется аттрибутом finalPerspective в plugin.xml
		// Открытие перспективы проектирвоания устройства
		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			workbench.showPerspective(BSP.DESIGN_PERSPECTIVE_ID, window);
		} catch (WorkbenchException e) {
			e.printStackTrace();
		}*/
		
		return true;
	}

	/** Добавление страниц в мастер */
	@Override
	public void addPages() {
		creationPage = new WizardNewProjectCreationPage("NewBlackSphereProjectPage"); //$NON-NLS-1$
		creationPage.setTitle(BSPMessages.getString("NewProjectWizard.GeneralProjectParams")); //$NON-NLS-1$
		creationPage.setDescription(BSPMessages.getString("NewProjectWizard.SetProjectParams")); //$NON-NLS-1$
		addPage(creationPage);
		templatePage = new ProjectTemplatePage("ProjectTemplatePage"); //$NON-NLS-1$
		templatePage.setTitle(BSPMessages.getString("NewProjectWizard.ProjectTemplate")); //$NON-NLS-1$
		templatePage.setDescription(BSPMessages.getString("NewProjectWizard.SelectProjectTemplate")); //$NON-NLS-1$
		addPage(templatePage);
	}

	/** Установка перспектвы из параметра finalPerspective в plugin.xml */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		BasicNewProjectResourceWizard.updatePerspective(config);
	}

	
}
