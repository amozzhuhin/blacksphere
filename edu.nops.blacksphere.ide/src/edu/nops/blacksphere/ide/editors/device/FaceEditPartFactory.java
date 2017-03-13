package edu.nops.blacksphere.ide.editors.device;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.ide.editors.device.parts.FaceDeviceEditPart;
import edu.nops.blacksphere.ide.editors.device.parts.FaceElementEditPart;

public class FaceEditPartFactory implements EditPartFactory {

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
			return new FaceDeviceEditPart();
		else return new FaceElementEditPart();
	}

}
