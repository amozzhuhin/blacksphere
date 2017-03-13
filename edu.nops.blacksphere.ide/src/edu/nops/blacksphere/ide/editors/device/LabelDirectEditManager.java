package edu.nops.blacksphere.ide.editors.device;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import edu.nops.blacksphere.ide.editors.device.figures.SchemeLabelFigure;

public class LabelDirectEditManager extends DirectEditManager {

	public LabelDirectEditManager(GraphicalEditPart source, 
			CellEditorLocator locator) {
		super(source, null, locator);
	}
	
	@Override
	protected void initCellEditor() {
		SchemeLabelFigure label = (SchemeLabelFigure)getEditPart().getFigure();
		getCellEditor().setValue(label.getText());
	}
	

	protected CellEditor createCellEditorOn(Composite composite) {
		return new TextCellEditor(composite, SWT.MULTI | SWT.WRAP);
	}
}
