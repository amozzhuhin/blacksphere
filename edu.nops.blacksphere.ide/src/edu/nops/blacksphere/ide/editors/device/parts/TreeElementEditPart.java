package edu.nops.blacksphere.ide.editors.device.parts;

import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;

import edu.nops.blacksphere.core.device.elements.AbstractElement;

public class TreeElementEditPart extends AbstractTreeEditPart {

	public TreeElementEditPart(AbstractElement model) {
		super(model);
	}

	@Override
	public void activate() {
		// TODO Автоматически созданная заглушка метода
		super.activate();
	}

	@Override
	protected void createEditPolicies() {
		// TODO Автоматически созданная заглушка метода
		super.createEditPolicies();
	}

	@Override
	public void deactivate() {
		// TODO Автоматически созданная заглушка метода
		super.deactivate();
	}

	@Override
	protected Image getImage() {
		// TODO Автоматически созданная заглушка метода
		return super.getImage();
	}

	@Override
	protected String getText() {
		// TODO Автоматически созданная заглушка метода
		return ((AbstractElement)getModel()).getName();
	}
	
	
}
