package edu.nops.blacksphere.ide.editors.device;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

import edu.nops.blacksphere.ide.editors.device.figures.SchemeLabelFigure;

public class LabelCellEditorLocator implements CellEditorLocator {

	SchemeLabelFigure label;
	
	public LabelCellEditorLocator(SchemeLabelFigure label) {
		this.label = label;
	}

	@Override
	public void relocate(CellEditor celleditor) {
		Text text = (Text)celleditor.getControl();
		Rectangle rect = label.getClientArea();
		label.translateToAbsolute(rect);
		org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
		rect.translate(trim.x, trim.y);
		rect.width += trim.width;
		rect.height += trim.height;
		text.setBounds(rect.x, rect.y, rect.width, rect.height);
	}
	
}
