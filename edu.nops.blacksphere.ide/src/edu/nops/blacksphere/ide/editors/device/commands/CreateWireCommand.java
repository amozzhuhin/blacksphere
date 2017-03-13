package edu.nops.blacksphere.ide.editors.device.commands;

import org.eclipse.gef.commands.Command;

import edu.nops.blacksphere.core.device.elements.AbstractChipElement;
import edu.nops.blacksphere.core.device.elements.WireElement;
import edu.nops.blacksphere.ide.BSPMessages;

/**
 * Класс команды создания проводника
 * @author nops
 */
public class CreateWireCommand extends Command {

	AbstractChipElement source, target;
	int sourcePin, targetPin;
	WireElement wire;
	
	public CreateWireCommand(AbstractChipElement source, int sourcePin) {
		setLabel(BSPMessages.getString("CreateWireCommand.WireCreation")); //$NON-NLS-1$
		this.source = source;
		this.sourcePin = sourcePin;
	}

	@Override
	public boolean canExecute() {
		return target != null;
	}

	@Override
	public void execute() {
		wire = new WireElement();
		wire.setSource(source, sourcePin);
		wire.setTarget(target, targetPin);
	}

	@Override
	public boolean canUndo() {
		return wire != null;
	}

	@Override
	public void undo() {
		wire.disconnect();
	}
	
	public void setTarget(AbstractChipElement target, int targetPin) {
		this.target = target;
		this.targetPin = targetPin;
	}

}
