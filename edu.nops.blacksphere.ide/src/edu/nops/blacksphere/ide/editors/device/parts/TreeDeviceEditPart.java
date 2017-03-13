package edu.nops.blacksphere.ide.editors.device.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import edu.nops.blacksphere.core.device.Device;

public class TreeDeviceEditPart extends AbstractTreeEditPart
  		implements PropertyChangeListener {

	public TreeDeviceEditPart(Device model) {
		super(model);
	}
	
	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			((Device) getModel()).addPropertyChangeListener(this);
		}
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
	}

	@Override
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((Device) getModel()).removePropertyChangeListener(this);
		}
	}

	@Override
	public List getModelChildren() {
		return ((Device)getModel()).getElements();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		refreshVisuals();	
	}

	
}
