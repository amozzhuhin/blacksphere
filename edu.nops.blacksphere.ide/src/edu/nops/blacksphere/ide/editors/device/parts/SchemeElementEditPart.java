package edu.nops.blacksphere.ide.editors.device.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import edu.nops.blacksphere.core.device.elements.*;
import edu.nops.blacksphere.ide.editors.device.commands.CreateWireCommand;
import edu.nops.blacksphere.ide.editors.device.commands.ReconnectWireCommand;
import edu.nops.blacksphere.ide.editors.device.figures.*;
import edu.nops.blacksphere.ide.editors.device.policy.SchemeComponentEditPolicy;

public class SchemeElementEditPart extends AbstractGraphicalEditPart
	implements PropertyChangeListener, NodeEditPart {

	@Override
	protected IFigure createFigure() {
		ElementFigure figure;
		Class modelClass = getModel().getClass();
		if (modelClass == OutPortElement.class
				|| modelClass == InPortElement.class
				|| modelClass == Led7SegElement.class
				|| modelClass == ADCElement.class
				|| modelClass == LedMatrixElement.class) {
			figure = new SchemeChipElementFigure(true);
		} else if (modelClass == NotElement.class
				|| modelClass == And3NotElement.class) {
			figure = new SchemeChipElementFigure(false);
		} else if (modelClass == LedElement.class) {
			figure = new SchemeLedFigure();
		} else if (modelClass == PowerElement.class) {
			figure = new SchemePowerFigure();
		} else if (modelClass == GroundElement.class) {
			figure = new SchemeGroundFigure();
		} else if (modelClass == ButtonElement.class) {
			figure = new SchemeButtonFigure();
		} else if (modelClass == KeyboardElement.class) {
			figure = new SchemeKeyboardFigure(true);
		} else return null;
		figure.setElement((AbstractElement)getModel());
		figure.setLocation(((AbstractElement)getModel()).getSchemeLocation());
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		// Разрешение удаление компонента
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new SchemeComponentEditPolicy());
		// Разрешение создания и пересоединения проводников
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ElementNodeEditPolicy(this) {
		});
	}

	private static class ElementNodeEditPolicy extends GraphicalNodeEditPolicy {
		
		SchemeElementEditPart editPart;
		
		public ElementNodeEditPolicy(SchemeElementEditPart editPart) {
			this.editPart = editPart;
		}
		
		/** Получение команды завершения соединения */
		protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
			CreateWireCommand cmd 
				= (CreateWireCommand) request.getStartCommand();
			ConnectionAnchor anchor = editPart.getTargetConnectionAnchor(request);
			int pin = ((ElementFigure) editPart.getFigure()).getAnchorPin(anchor);
			cmd.setTarget((AbstractChipElement) getHost().getModel(), pin);
			return cmd;
		}

		/** Команда создания соединения */
		protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
			AbstractChipElement source = (AbstractChipElement) getHost().getModel();
			ConnectionAnchor anchor = editPart.getSourceConnectionAnchor(request);
			int pin = ((ElementFigure) editPart.getFigure()).getAnchorPin(anchor);
			CreateWireCommand cmd = new CreateWireCommand(source, pin);
			request.setStartCommand(cmd);
			return cmd;
		}
		
		/** Создание команды пересоединения начала проводника */
		protected Command getReconnectSourceCommand(ReconnectRequest request) {
			WireElement wire = (WireElement) request.getConnectionEditPart().getModel();
			AbstractChipElement newSource = (AbstractChipElement) getHost().getModel();
			ConnectionAnchor anchor = editPart.getSourceConnectionAnchor(request);
			int newSourcePin = ((ElementFigure) editPart.getFigure()).getAnchorPin(anchor);
			ReconnectWireCommand cmd = new ReconnectWireCommand(wire);
			cmd.setNewSource(newSource, newSourcePin);
			return cmd;
		}
		
		/** Создание команды пересоединения конца проводника  */
		protected Command getReconnectTargetCommand(ReconnectRequest request) {
			WireElement wire = (WireElement) request.getConnectionEditPart().getModel();
			AbstractChipElement newTarget = (AbstractChipElement) getHost().getModel();
			ConnectionAnchor anchor = editPart.getTargetConnectionAnchor(request);
			int newTargetPin = ((ElementFigure) editPart.getFigure()).getAnchorPin(anchor);
			ReconnectWireCommand cmd = new ReconnectWireCommand(wire);
			cmd.setNewTarget(newTarget, newTargetPin);
			return cmd;
		}

	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (AbstractChipElement.INPUT_CONNECTIONS_PROP.equals(prop)) {
			refreshTargetConnections();
		} else if (AbstractChipElement.OUTPUT_CONNECTIONS_PROP.equals(prop)) {
			refreshSourceConnections();
		}
		refreshVisuals();
	}

	
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		ElementFigure figure = (ElementFigure) getFigure();
		AbstractElement model = (AbstractElement) getModel();
		figure.refreshFigure(model);
		// Сообщить контейнеру элемента об изменении позиции
		Rectangle bounds = new Rectangle(model.getSchemeLocation(), figure.getSize());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure, bounds);
		figure.repaint();
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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		WireElement wire = (WireElement) connection.getModel();
		return ((ElementFigure) getFigure()).
				getConnectionAnchors().get(wire.getSourcePin());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest)request).getLocation());
		return ((ElementFigure) getFigure()).getSourceConnectionAnchorAt(pt);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		WireElement wire = (WireElement) connection.getModel();
		return ((ElementFigure) getFigure()).
				getConnectionAnchors().get(wire.getTargetPin());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest)request).getLocation());
		return ((ElementFigure) getFigure()).getTargetConnectionAnchorAt(pt);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	protected List getModelSourceConnections() {
		if (getModel() instanceof AbstractChipElement)
			return ((AbstractChipElement) getModel()).getOutputWires();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	protected List getModelTargetConnections() {
		if (getModel() instanceof AbstractChipElement)
			return ((AbstractChipElement) getModel()).getInputWires();
		return null;
	}
}
