package edu.nops.blacksphere.ide.editors.device.commands;

import org.eclipse.gef.commands.Command;

import edu.nops.blacksphere.core.device.elements.AbstractChipElement;
import edu.nops.blacksphere.core.device.elements.WireElement;

public class DeleteWireCommand extends Command {

	WireElement wire;
	AbstractChipElement source, target;
	int sourcePin, targetPin;
	
	public DeleteWireCommand(WireElement wire) {
		this.wire = wire; 
	}

	@Override
	public boolean canExecute() {
		return wire != null;
	}

	@Override
	public boolean canUndo() {
		return source != null && target != null;
	}

	@Override
	public void execute() {
		source = wire.getSource();
		sourcePin = wire.getSourcePin();
		target = wire.getTarget();
		targetPin = wire.getTargetPin();
		wire.disconnect();
	}

	@Override
	public void undo() {
		wire.setSource(source, sourcePin);
		wire.setTarget(target, targetPin);
		source = target = null;
	}
	
	
}
