package edu.nops.blacksphere.ide.editors.device.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.core.device.elements.WireElement;
import edu.nops.blacksphere.ide.editors.device.commands.DeleteWireCommand;


public class SchemeWireEditPart extends AbstractConnectionEditPart
	implements PropertyChangeListener{

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, 
				new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy(){
			protected Command getDeleteCommand(GroupRequest request) {
				WireElement wire = (WireElement) getHost().getModel();
				return new DeleteWireCommand(wire);
			}
		});
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		((PolylineConnection) getFigure()).setLineWidth(2);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		refreshVisuals();		
	}

	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			((AbstractElement) getModel()).addPropertyChangeListener(this);
		}
	}

	@Override
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((AbstractElement) getModel()).removePropertyChangeListener(this);
		}
	}

}
