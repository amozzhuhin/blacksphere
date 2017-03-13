package edu.nops.blacksphere.ide.editors.device;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;

import edu.nops.blacksphere.ide.BSPMessages;

public class DeviceActionBarContributor extends MultiPageEditorActionBarContributor {

	private DeleteRetargetAction deleteAction;
	private ZoomInRetargetAction zoomInAction;
	private ZoomOutRetargetAction zoomOutAction;
	private ZoomComboContributionItem zoomComboContributionItem;
	private RetargetAction toggleGrid;
	
	@Override
	public void init(IActionBars bars, IWorkbenchPage page) {
		buildActions(page);
		super.init(bars, page);
	}

	protected void buildActions(IWorkbenchPage page) {
		deleteAction = new DeleteRetargetAction();
		page.addPartListener(deleteAction);
		zoomInAction = new ZoomInRetargetAction();
		page.addPartListener(zoomInAction);
		zoomOutAction = new ZoomOutRetargetAction();
		page.addPartListener(zoomOutAction);
		String[] zoomStrings = new String[] {ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH};
		zoomComboContributionItem = new ZoomComboContributionItem(page, zoomStrings);
		toggleGrid = new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY, 
				BSPMessages.getString("DeviceActionBarContributor.toggleGrid"), IAction.AS_CHECK_BOX); //$NON-NLS-1$
	}

	protected static String[] actions = new String[] {
		ActionFactory.COPY.getId(), ActionFactory.CUT.getId(), ActionFactory.PASTE.getId(),
		ActionFactory.DELETE.getId(), ActionFactory.SELECT_ALL.getId(),
		ActionFactory.PRINT.getId(), // TODO проверить печать GEF на других версиях
		ActionFactory.UNDO.getId(), ActionFactory.REDO.getId(),
		GEFActionConstants.ZOOM_IN, GEFActionConstants.ZOOM_OUT,
		GEFActionConstants.TOGGLE_GRID_VISIBILITY
	}; 

	protected void declareGlobalActionKeys(IEditorPart activeEditor) { }
	
	@Override
	public void setActivePage(IEditorPart activeEditor) { 
		IActionBars actionBars = getActionBars();
		actionBars.clearGlobalActionHandlers();
		zoomComboContributionItem.setZoomManager(null);
		if (activeEditor instanceof GraphicalEditor) {
			zoomComboContributionItem.setZoomManager((ZoomManager) activeEditor.getAdapter(ZoomManager.class));
			ActionRegistry registry = (ActionRegistry) activeEditor.getAdapter(ActionRegistry.class);
			for (String actionId : actions)
				actionBars.setGlobalActionHandler(actionId, registry.getAction(actionId));
		} else if (activeEditor instanceof ITextEditor) {
			ITextEditor editor = (ITextEditor) activeEditor;
			for (String actionId : actions)
				actionBars.setGlobalActionHandler(actionId, editor.getAction(actionId));
		}
		declareGlobalActionKeys(activeEditor);
		actionBars.updateActionBars();
	}
	
	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		MenuManager viewMenu = new MenuManager(BSPMessages.getString("DeviceActionBarContributor.title")); //$NON-NLS-1$
		viewMenu.add(zoomInAction);
		viewMenu.add(zoomOutAction);
		viewMenu.add(new Separator());
		viewMenu.add(toggleGrid);
		menuManager.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
	}

	
	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(zoomInAction);
		toolBarManager.add(zoomOutAction);
		toolBarManager.add(zoomComboContributionItem);
		toolBarManager.add(deleteAction);
	}

}
