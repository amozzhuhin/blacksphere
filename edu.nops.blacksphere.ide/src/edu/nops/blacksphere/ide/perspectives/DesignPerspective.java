package edu.nops.blacksphere.ide.perspectives;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import edu.nops.blacksphere.ide.BSP;

/**
 * Перспектива "Проектирование устройства"
 * Предоставляет расположение окон по умолчанию
 * @author nops
 */
public class DesignPerspective implements IPerspectiveFactory {

	/** Расположение используемых окон в BlackSphere по умолчанию */
	@Override
	public void createInitialLayout(IPageLayout layout) {
		// Добавление пунктов главного меню "Навигация" и "Выполнить" 
		layout.addActionSet(IDebugUIConstants.LAUNCH_ACTION_SET);
		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
		// Быстрый выбор мастеров создания
		layout.addNewWizardShortcut(BSP.NEW_PROJECT_WIZARD_ID);
		layout.addNewWizardShortcut(BSP.NEW_INCLUDE_FILE_WIZARD_ID);
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//$NON-NLS-1$
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//$NON-NLS-1$
        // Настройка расположения страниц 
		String editorArea = layout.getEditorArea();
		// Навигатор слева 
		layout.addView(IPageLayout.ID_RES_NAV, IPageLayout.LEFT, 0.20f, editorArea);
		// Список ошибок и консоль внизу
		IFolderLayout bottomFolder = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.75f, editorArea); //$NON-NLS-1$
		bottomFolder.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottomFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		// Схема и свойства справа
		IFolderLayout rightFolder = layout.createFolder("right", IPageLayout.RIGHT, 0.75f, editorArea); //$NON-NLS-1$
		rightFolder.addView(IPageLayout.ID_OUTLINE);
		rightFolder.addView(IPageLayout.ID_PROP_SHEET);
	}

	
}
