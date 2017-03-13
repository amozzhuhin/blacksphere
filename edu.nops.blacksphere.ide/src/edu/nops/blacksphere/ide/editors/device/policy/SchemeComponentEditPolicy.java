package edu.nops.blacksphere.ide.editors.device.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.ide.editors.device.commands.DeleteElementCommand;

/** Политика редактирования элементов схемы */
public class SchemeComponentEditPolicy extends ComponentEditPolicy {

	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		Device device = (Device) getHost().getParent().getModel();
		AbstractElement element = (AbstractElement) getHost().getModel();
		return new DeleteElementCommand(device, element);
	}

}
