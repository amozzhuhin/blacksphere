package edu.nops.blacksphere.ide.editors.device.commands;

import org.eclipse.gef.commands.Command;

import edu.nops.blacksphere.core.device.elements.AbstractChipElement;
import edu.nops.blacksphere.core.device.elements.WireElement;
import edu.nops.blacksphere.ide.BSPMessages;

/**
 * Класс команды пересоединения проводника 
 * @author andrei
 */
public class ReconnectWireCommand extends Command {

	WireElement wire;
	int newSourcePin, oldSourcePin;
	AbstractChipElement newSource, oldSource;
	int newTargetPin, oldTargetPin;
	AbstractChipElement newTarget, oldTarget;
	
	public ReconnectWireCommand(WireElement wire) {
		this.wire = wire;
		newSource = oldSource = null;
		newTarget = oldTarget = null;
		setLabel(BSPMessages.getString("ReconnectWireCommand.ChangeWire")); //$NON-NLS-1$
	}

	public void setNewSource(AbstractChipElement newSource, int newSourcePin) {
		this.newSource = newSource;
		this.newSourcePin = newSourcePin;
	}
	
	public void setNewTarget(AbstractChipElement newTarget, int newTargetPin) {
		this.newTarget = newTarget;
		this.newTargetPin = newTargetPin;
	}
	
	@Override
	public boolean canExecute() {
		return wire != null && (newSource != null || newTarget != null);
	}

	@Override
	public boolean canUndo() {
		return wire != null && (oldSource != null || oldTarget != null);
	}

	@Override
	public void execute() {
		if (newTarget != null) {
			oldTarget = wire.getTarget();
			oldTargetPin = wire.getTargetPin();
			wire.setTarget(newTarget, newTargetPin);
		}
		if (newSource != null) {
			oldSource = wire.getSource();
			oldSourcePin = wire.getSourcePin();
			wire.setSource(newSource, newSourcePin);
		}
	}

	@Override
	public void undo() {
		if (oldTarget != null)
			wire.setTarget(oldTarget, oldTargetPin);
		if (oldSource != null)
			wire.setSource(oldSource, oldSourcePin);
	}
}
