package edu.nops.blacksphere.ide.editors.device;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

import edu.nops.blacksphere.ide.editors.device.actions.GEFEditorAsImageAction;

public class SchemeContextMenuProvider extends ContextMenuProvider {

	public SchemeContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
		super(viewer);
		setActionRegistry(registry);
	}

	private ActionRegistry actionRegistry;
	
	private ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	private void setActionRegistry(ActionRegistry registry) {
		actionRegistry = registry;
	}
	
	
	@Override
	public void buildContextMenu(IMenuManager manager) {
		GEFActionConstants.addStandardActionGroups(manager);
		IAction action;

		action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
		manager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry().getAction(ActionFactory.REDO.getId());
		manager.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

		action = getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT);
		if (action != null && action.isEnabled())
			manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		
		action = getActionRegistry().getAction(GEFEditorAsImageAction.ID);
		manager.appendToGroup(GEFActionConstants.GROUP_PRINT, action);
		
		/*action = getActionRegistry().getAction(PropertiesAction.ID);
		manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);*/
		
		/*
		action = getActionRegistry().getAction(ActionFactory.PASTE.getId());
		manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		action = getActionRegistry().getAction(ActionFactory.COPY.getId());
		manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		action = getActionRegistry().getAction(ActionFactory.CUT.getId());
		manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
*/
		action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
		manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
/*
		action = getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT);
		manager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);*/
	}

}
