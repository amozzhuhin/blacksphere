package edu.nops.blacksphere.ide.editors.asm;

import java.util.ResourceBundle;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

/**
 * Редактор исходного текста на языке ассемблера 
 * @author nops
 */
public class ASMEditor extends TextEditor {

	public ASMEditor() {
		super();
		// Установка конфигурации редактора
		setSourceViewerConfiguration(new ASMSourceViewerConfiguration());
	}

	@Override
	protected void createActions() {
		ResourceBundle bundle = ResourceBundle.getBundle(
				"edu.nops.blacksphere.ide.editors.asm.messages"); //$NON-NLS-1$
		// Вызов дополнения кода
		IAction action= new ContentAssistAction(bundle,
				"ContentAssistProposal.", this); //$NON-NLS-1$
		action.setActionDefinitionId(
				ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		String actionId = "ContentAssistProposal"; //$NON-NLS-1$
		setAction(actionId, action);
		markAsStateDependentAction(actionId, true);
		//PlatformUI.getWorkbench().getHelpSystem().setHelp(action, helpContextId);
		
		super.createActions();
	}
	
	@Override
	protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
		getPreferenceStore().setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_PRINT_MARGIN_COLUMN, 80);
		getPreferenceStore().setValue(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_PRINT_MARGIN, true);
		super.configureSourceViewerDecorationSupport(support);
	}	
	
}
