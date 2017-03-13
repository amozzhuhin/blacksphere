package edu.nops.blacksphere.ide.editors.device.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.swt.SWT;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.ide.BSP;
import edu.nops.blacksphere.ide.editors.device.commands.CreateElementCommand;
import edu.nops.blacksphere.ide.editors.device.commands.ElementChangeConstraintCommand;

/**
 * Редактируемый элемент устройства на схеме.
 * Управляет расположением элементов на схеме.
 * Предоставляет команды создания и удаления элемонтов со схемы.
 * @author nops
 */
public class SchemeDeviceEditPart extends AbstractGraphicalEditPart
  implements PropertyChangeListener,  LayerConstants {
	
	@Override
	protected IFigure createFigure() {
		Figure f = new FreeformLayer();
		f.setFont(BSP.schemeFont);
		f.setBorder(new MarginBorder(3));
		f.setLayoutManager(new FreeformLayout());

		// Create the static router for the connection layer
		ConnectionLayer connLayer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
		connLayer.setConnectionRouter(new ShortestPathConnectionRouter(f));

		return f;
	}

	@Override
	protected void createEditPolicies() {
		// запрет уничтожения
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
		// управление изменениями (передвижение, изменение размера) элементов модели
		// и создание новых элементов
		installEditPolicy(EditPolicy.LAYOUT_ROLE,  new DeviceXYLayoutEditPolicy());
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		// При добавлении или удалении элемента нужно обновить представление 
		if (Device.ELEMENT_ADDED_PROP.equals(prop)
				|| Device.ELEMENT_REMOVED_PROP.equals(prop)) {
			refreshChildren();
		}
	}

	
	@Override
	public List getModelChildren() {
		return ((Device) getModel()).getElements();
	}

	
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		// установка трассировщика содинений
		Animation.markBegin();
		ConnectionLayer cLayer = (ConnectionLayer) getLayer(CONNECTION_LAYER);
        cLayer.setAntialias(SWT.ON);
		cLayer.setConnectionRouter(new ManhattanConnectionRouter());
		Animation.run(400);
	}

	public Object getAdapter(Class adapter) {
		if (adapter == SnapToHelper.class) {
			List snapStrategies = new ArrayList();
			Boolean val = (Boolean)getViewer()
					.getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
			if (val != null && val.booleanValue())
				snapStrategies.add(new SnapToGuides(this));
			val = (Boolean)getViewer().getProperty(
					SnapToGeometry.PROPERTY_SNAP_ENABLED);
			if (val != null && val.booleanValue())
				snapStrategies.add(new SnapToGeometry(this));
			val = (Boolean)getViewer().getProperty(
					SnapToGrid.PROPERTY_GRID_ENABLED);
			if (val != null && val.booleanValue())
				snapStrategies.add(new SnapToGrid(this));
			
			if (snapStrategies.size() == 0)
				return null;
			if (snapStrategies.size() == 1)
				return snapStrategies.get(0);

			SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
			for (int i = 0; i < snapStrategies.size(); i++)
				ss[i] = (SnapToHelper)snapStrategies.get(i);
			return new CompoundSnapToHelper(ss);
		}
		return super.getAdapter(adapter);
	}

	private static class DeviceXYLayoutEditPolicy extends XYLayoutEditPolicy {
		
		protected Command createChangeConstraintCommand(ChangeBoundsRequest request,
				EditPart child, Object constraint) {
			
			if (child.getModel() instanceof AbstractElement) {
				// return a command that can move and/or resize a Shape
				return new ElementChangeConstraintCommand(
						(AbstractElement) child.getModel(), request,
						(Rectangle) constraint);
			}
			return super.createChangeConstraintCommand(request, child, constraint);
		}
		
		
		protected Command createChangeConstraintCommand(EditPart child,
				Object constraint) {
			return null; // не используется
		}
		
		
		protected Command getCreateCommand(CreateRequest request) {
			if (request.getNewObject() instanceof AbstractElement) {
				return new CreateElementCommand((AbstractElement)request.getNewObject(), 
						(Device)getHost().getModel(), (Rectangle)getConstraintFor(request));
			}
			return super.getCommand(request);
		}
		
	}
}
