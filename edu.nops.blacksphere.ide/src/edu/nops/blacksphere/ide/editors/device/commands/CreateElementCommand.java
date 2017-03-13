package edu.nops.blacksphere.ide.editors.device.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.core.device.elements.AbstractElement;

public class CreateElementCommand extends Command {
	
	/**
	 * Создаваемый элемент
	 */
	private AbstractElement element;
	
	/**
	 * Устройство, в котором создайтся элемент
	 */
	private Device device;
	
	/**
	 * Позиция и размеры элемента
	 */
	private Rectangle bounds;
	
	public CreateElementCommand(AbstractElement element, 
			Device device, Rectangle bounds) {
		this.element = element;
		this.device = device;
		this.bounds = bounds;
	}

	/**
	 * Проверка выполнимости создания
	 */
	@Override
	public boolean canExecute() {
		return element != null && device != null;
	}

	/**
	 * Вызывается при выполнении действия
	 */
	@Override
	public void execute() {
		element.setSchemeLocation(bounds.getLocation());
		// Для элементов с отображением на лицевой панели
		// устанавливаем такую же позицию как на схеме
		if (element.hasFace())
			element.setFaceLocation(element.getSchemeLocation());
		element.setName(device.getWhiteName(element.getNamePrefix()));
		redo();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		device.addElement(element);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		device.removeElement(element);
	}
	
}
