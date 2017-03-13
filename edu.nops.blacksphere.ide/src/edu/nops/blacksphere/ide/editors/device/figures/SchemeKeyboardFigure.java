package edu.nops.blacksphere.ide.editors.device.figures;

import edu.nops.blacksphere.core.device.elements.AbstractElement;

public class SchemeKeyboardFigure extends SchemeChipElementFigure {

	public SchemeKeyboardFigure(boolean showSections) {
		super(showSections);
	}

	@Override
	public void refreshFigure(AbstractElement model) {
		super.refreshFigure(model);
		// Могло измениться количество колонок или строк
		// нужно пересоздать фигуру полностью
		reconstructFigure();
	}

}
