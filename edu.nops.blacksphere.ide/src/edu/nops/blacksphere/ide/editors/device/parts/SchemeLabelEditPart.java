package edu.nops.blacksphere.ide.editors.device.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.core.device.elements.LabelElement;
import edu.nops.blacksphere.ide.editors.device.LabelCellEditorLocator;
import edu.nops.blacksphere.ide.editors.device.LabelDirectEditManager;
import edu.nops.blacksphere.ide.editors.device.figures.SchemeLabelFigure;
import edu.nops.blacksphere.ide.editors.device.policy.LabelDirectEditPolicy;
import edu.nops.blacksphere.ide.editors.device.policy.SchemeComponentEditPolicy;

public class SchemeLabelEditPart extends SchemeElementEditPart {

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		// установка политики прямого редактирования 
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new LabelDirectEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new SchemeComponentEditPolicy()); 
	}

	private void performDirectEdit(){
		new LabelDirectEditManager(this,
				new LabelCellEditorLocator((SchemeLabelFigure)getFigure())).show();
	}

	public void performRequest(Request request){
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
			performDirectEdit();
	}

/*	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		// Сообщить контейнеру элемента об изменении текста
		LabelElement label = (LabelElement) getModel(); 
		get
	}*/

	@Override
	protected IFigure createFigure() {
		if (getModel().getClass().equals(LabelElement.class)) {
			SchemeLabelFigure figure = new SchemeLabelFigure();
			figure.setElement((AbstractElement)getModel());
			return figure;
		}
		return null;
	}
}
