package edu.nops.blacksphere.ide.wizards;

import java.io.FileInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

import edu.nops.blacksphere.ide.BSP;
import edu.nops.blacksphere.ide.BSPMessages;

/**
 * Мастер создания включаемого файла
 * @author nops
 */
public class NewInclideWizard extends Wizard implements INewWizard {

	private WizardNewFileCreationPage creationPage;

	private IStructuredSelection selection;
	
	@Override
	public boolean performFinish() {
		createFile();
		
		return true;
	}

	private void createFile() {
		IFile file = creationPage.createNewFile();
		String template = BSP.getFilePathFromPlugin("templates") + "/include.inc"; //$NON-NLS-1$ //$NON-NLS-2$
		InputStream is;
		try {
			is = new FileInputStream(template);
			file.setContents(is, IFile.FORCE, null);
			// Открыть файл в среде 
			IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
			IDE.openEditor(page, file);
			// TODO Перейти курсором на файл
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	/**
	 * Добавление страниц в мастер
	 */
	@Override
	public void addPages() {
		creationPage = new WizardNewFileCreationPage("NewBlackSphereInclideFilePage", selection); //$NON-NLS-1$
		creationPage.setTitle(BSPMessages.getString("NewInclideWizard.IncludeFile")); //$NON-NLS-1$
		creationPage.setDescription(BSPMessages.getString("NewInclideWizard.CreateNewIncludeFile")); //$NON-NLS-1$
		addPage(creationPage);
	}
	
}
