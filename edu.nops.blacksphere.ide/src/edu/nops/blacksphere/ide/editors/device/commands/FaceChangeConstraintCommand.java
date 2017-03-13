package edu.nops.blacksphere.ide.editors.device.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.ide.BSPMessages;

public class FaceChangeConstraintCommand extends Command {

	protected AbstractElement element;
	
	protected ChangeBoundsRequest request;
	
	protected Point newLocation;
	
	protected Point oldLocation;
	
	/**
	 * Создание команды изменения размера или позиции 
	 * @param element элемент схемы
	 * @param request описание запроса
	 * @param bounds новый размер и позиция
	 * @throws IllegalArgumentException если хотя бы один параметр равен null
	 */
	public FaceChangeConstraintCommand(AbstractElement element,
			ChangeBoundsRequest request, Rectangle bounds) {
		if (element == null || request == null || bounds == null) {
			throw new IllegalArgumentException();
		}
		this.element = element;
		this.request = request;
		this.newLocation = bounds.getLocation();
		setLabel(BSPMessages.getString("FaceChangeConstraintCommand.move")); //$NON-NLS-1$
	}
	
	public boolean canExecute() {
		Object type = request.getType();
		return (RequestConstants.REQ_MOVE.equals(type)
				|| RequestConstants.REQ_MOVE_CHILDREN.equals(type) 
				|| RequestConstants.REQ_RESIZE.equals(type)
				|| RequestConstants.REQ_RESIZE_CHILDREN.equals(type));
	}

	public void execute() {
		oldLocation = element.getFaceLocation().getCopy();
		element.setFaceLocation(newLocation);
	}

	public void undo() {
		element.setFaceLocation(oldLocation);
	}
}
