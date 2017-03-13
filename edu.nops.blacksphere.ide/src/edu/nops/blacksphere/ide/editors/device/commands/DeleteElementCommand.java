package edu.nops.blacksphere.ide.editors.device.commands;

import java.util.ArrayList;

import org.eclipse.gef.commands.Command;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.core.device.elements.AbstractChipElement;
import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.core.device.elements.WireElement;
import edu.nops.blacksphere.ide.BSPMessages;

public class DeleteElementCommand extends Command {

	private Device device;
	
	private AbstractElement element;
	ArrayList<WireElement> inputWires, outputWires;
	private boolean wasRemoved;
	
	public DeleteElementCommand(Device device, AbstractElement element) {
		if (device == null || element == null) {
			throw new IllegalArgumentException();
		}
		setLabel(BSPMessages.getString("DeleteElementCommand.label")); //$NON-NLS-1$
		this.device = device;
		this.element = element;
	}
	
	public boolean canUndo() {
		return wasRemoved;
	}

	public void execute() {
		if (element instanceof AbstractChipElement) {
			//Object
			inputWires = new ArrayList<WireElement>(((AbstractChipElement) element).getInputWires());
			for(WireElement wire : inputWires)
				wire.disconnect();
			outputWires = new ArrayList<WireElement>(((AbstractChipElement) element).getOutputWires());
			for(WireElement wire : outputWires)
				wire.disconnect();
		} else {
			inputWires = null;
			outputWires = null;
		}
		wasRemoved = device.removeElement(element);
	}
	
	public void undo() {
		device.addElement(element);
		if (inputWires != null)
			for(WireElement wire : inputWires)
				wire.connect();
		if (outputWires != null)
			for(WireElement wire : outputWires)
				wire.connect();
	}
}
