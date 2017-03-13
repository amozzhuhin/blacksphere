package edu.nops.blacksphere.ide.editors.device.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import edu.nops.blacksphere.core.device.elements.LabelElement;
import edu.nops.blacksphere.ide.editors.device.commands.ChangeLabelTextCommand;
import edu.nops.blacksphere.ide.editors.device.figures.SchemeLabelFigure;
import edu.nops.blacksphere.ide.editors.device.parts.SchemeLabelEditPart;

public class LabelDirectEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		String labelText = (String)request.getCellEditor().getValue();
		SchemeLabelEditPart label = (SchemeLabelEditPart)getHost();
		return new ChangeLabelTextCommand((LabelElement)label.getModel(),labelText);
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String)request.getCellEditor().getValue();
		((SchemeLabelFigure)getHostFigure()).setText(value);
		//hack to prevent async layout from placing the cell editor twice.
		getHostFigure().getUpdateManager().performUpdate();
	}

}
