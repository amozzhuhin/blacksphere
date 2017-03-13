package edu.nops.blacksphere.ide.editors.device.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.widgets.Display;

import edu.nops.blacksphere.core.device.elements.*;
import edu.nops.blacksphere.ide.editors.device.figures.*;
import edu.nops.blacksphere.ide.editors.device.policy.SchemeComponentEditPolicy;

public class FaceElementEditPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	@Override
	protected IFigure createFigure() {
		ElementFigure figure;
		AbstractElement element = (AbstractElement)getModel();
		Class modelClass = element.getClass();
		if (modelClass == ButtonElement.class) {
			figure = new FaceButtonFigure();
		} else if (modelClass == LedElement.class) {
			figure = new FaceLedFigure();
		} else if (modelClass == Led7SegElement.class) {
			figure = new FaceLed7SegFigure();
		} else if (modelClass == ADCElement.class) {
			figure = new FaceADCFigure();
		} else if (modelClass == KeyboardElement.class) {
			figure = new FaceKeyboardFigure();
		} else if (modelClass == LedMatrixElement.class) {
			figure = new FaceLedMatrixFigure();
		} else return null;
		figure.setElement(element);
		if (element.getFaceLocation() != null)
			figure.setLocation(element.getFaceLocation());
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		// Разрешение удаление компонента
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new SchemeComponentEditPolicy());
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

	public void propertyChange(PropertyChangeEvent evt) {
		Display.getDefault().asyncExec(new Runnable () {
		      public void run () {
		  		refreshVisuals();
		      }
		});
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		ElementFigure figure = (ElementFigure) getFigure();
		AbstractElement model = (AbstractElement) getModel();
		figure.refreshFigure(model);
		// Сообщить контейнеру элемента об изменении позиции
		Rectangle bounds = new Rectangle(model.getFaceLocation(), figure.getSize());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure, bounds);
		figure.repaint();
	}
}
