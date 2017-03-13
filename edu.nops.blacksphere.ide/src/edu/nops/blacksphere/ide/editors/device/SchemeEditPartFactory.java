package edu.nops.blacksphere.ide.editors.device;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.core.device.elements.LabelElement;
import edu.nops.blacksphere.core.device.elements.WireElement;
import edu.nops.blacksphere.ide.editors.device.parts.SchemeDeviceEditPart;
import edu.nops.blacksphere.ide.editors.device.parts.SchemeElementEditPart;
import edu.nops.blacksphere.ide.editors.device.parts.SchemeLabelEditPart;
import edu.nops.blacksphere.ide.editors.device.parts.SchemeWireEditPart;

/**
 * Фабрика преобразования объекта модели в
 * элемент редактируемую единицу
 * @author nops
 */
public class SchemeEditPartFactory implements EditPartFactory {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object modelElement) {
		// Получение редактируемой единицы по модели
		EditPart part = getPartForElement(modelElement);
		// установка модели для редактируемой единицы
		part.setModel(modelElement);
		return part;
	}
	
	/** Преобразование объекта модели в редактируемый элемент */
	private EditPart getPartForElement(Object modelElement) {
		if (modelElement.getClass() == Device.class)
			return new SchemeDeviceEditPart();
		else if (modelElement.getClass() == LabelElement.class)
			return new SchemeLabelEditPart();
		else if (modelElement.getClass() == WireElement.class)
			return new SchemeWireEditPart();
		else
			return new SchemeElementEditPart();
	}
}
