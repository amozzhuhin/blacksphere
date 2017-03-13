package edu.nops.blacksphere.ide.editors.device.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.ide.editors.device.commands.FaceChangeConstraintCommand;

public class FaceDeviceEditPart extends AbstractGraphicalEditPart
		implements PropertyChangeListener {

	/** Создание фигуры для подложки лицевой панели */
	@Override
	protected IFigure createFigure() {
		FreeformLayer f = new FreeformLayer();
		f.setBorder(new MarginBorder(3));
		f.setLayoutManager(new FreeformLayout());
		return f;
	}

	/** При активации присоединяемся к модели слушателем изменения */
	public void activate() {
		if (!isActive()) {
			super.activate();
			((Device) getModel()).addPropertyChangeListener(this);
		}
	}
	
	/** При деактивации нужно отсоединится от слушателей модели */
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((Device) getModel()).removePropertyChangeListener(this);
		}
	}

	/** Выхывается при изменении прослушиваемых объектов (модели) */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		// При добавлении или удалении элемента нужно обновить представление 
		if (Device.ELEMENT_ADDED_PROP.equals(prop)
				|| Device.ELEMENT_REMOVED_PROP.equals(prop)) {
			refreshChildren();
		}
	}

	/** Получение элементов на лицевой панели */
	@Override
	public List getModelChildren() {
		ArrayList<AbstractElement> childs = new ArrayList<AbstractElement>(); 
		// выборка только отображаемых на лицевой панели элементов 
		for (AbstractElement el : ((Device) getModel()).getElements()){
			  if (el.hasFace())
				  childs.add(el);
		}
		return childs; 
	}

	@Override
	protected void createEditPolicies() {
		// запрет уничтожения
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
		// управление изменениями элементов модели
		installEditPolicy(EditPolicy.LAYOUT_ROLE,  new FaceDeviceLayoutEditPolicy());

	}

	private static class FaceDeviceLayoutEditPolicy extends XYLayoutEditPolicy {

		protected Command createChangeConstraintCommand(ChangeBoundsRequest request,
				EditPart child, Object constraint) {
			if (child.getModel() instanceof AbstractElement) {
				return new FaceChangeConstraintCommand(
						(AbstractElement) child.getModel(), request,
						(Rectangle) constraint);
			}
			return super.createChangeConstraintCommand(request, child, constraint);
		}
		
		protected Command createChangeConstraintCommand(EditPart child,
				Object constraint) {
			return null; // не используется
		}

		@Override
		protected Command getCreateCommand(CreateRequest request) {
			return null; // на лицевой панели нельзя создавать элементов
		}
		
	}
}
