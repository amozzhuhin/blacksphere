package edu.nops.blacksphere.ide.editors.device.commands;

import org.eclipse.gef.commands.Command;

import edu.nops.blacksphere.core.device.elements.LabelElement;
import edu.nops.blacksphere.ide.BSPMessages;

/**
 * Крманда изменения текста метки
 * @author nops
 */
public class ChangeLabelTextCommand extends Command {

	private String oldText, newText;
	
	private LabelElement label;
	
	public ChangeLabelTextCommand(LabelElement label, String text) {
		newText = text != null ? text : ""; //$NON-NLS-1$
		this.label = label;
		setLabel(BSPMessages.getString("ChangeLabelTextCommand.ChangeLabelText")); //$NON-NLS-1$
	}

	@Override
	public boolean canUndo() {
		return label != null && oldText != null;
	}

	@Override
	public void execute() {
		oldText = label.getText() != null ? label.getText() : null;
		label.setText(newText);
	}

	@Override
	public void undo() {
		label.setText(oldText);
	}
	
	
}
