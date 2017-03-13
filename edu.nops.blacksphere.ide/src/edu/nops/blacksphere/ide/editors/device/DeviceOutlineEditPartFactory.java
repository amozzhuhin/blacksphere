package edu.nops.blacksphere.ide.editors.device;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.ide.editors.device.parts.TreeDeviceEditPart;
import edu.nops.blacksphere.ide.editors.device.parts.TreeElementEditPart;

public class DeviceOutlineEditPartFactory implements EditPartFactory {

	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof Device)
			return new TreeDeviceEditPart((Device)model); 
		if (model instanceof AbstractElement)
			return new TreeElementEditPart((AbstractElement)model);
		return null;
	}

}
