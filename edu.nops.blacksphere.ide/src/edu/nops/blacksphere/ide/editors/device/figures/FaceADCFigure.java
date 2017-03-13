package edu.nops.blacksphere.ide.editors.device.figures;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ScrollBar;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.SWT;

import edu.nops.blacksphere.core.device.elements.ADCElement;
import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.ide.BSP;

public class FaceADCFigure extends ElementFigure {

	Label valueLabel;
	ScrollBar scrollBar;
	
	@Override
	protected Figure createBody() {
		Figure body = new Figure();
		ToolbarLayout layout = new ToolbarLayout(false);
		layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
		body.setLayoutManager(layout);
		valueLabel = createLabel();
		valueLabel.setText("0x" + Integer.toHexString(((ADCElement)getElement()).getData())); //$NON-NLS-1$
		body.add(valueLabel);
		scrollBar = new ScrollBar();
		scrollBar.setPreferredSize(SWT.DEFAULT, BSP.GRID_SPACING * 9);
		scrollBar.setMinimum(0);
		scrollBar.setMaximum(255);
		scrollBar.setStepIncrement(1);
		scrollBar.setExtent(0);
		scrollBar.setEnabled(getElement().isPowered());
		scrollBar.setValue(255 - ((ADCElement) getElement()).getData());
		scrollBar.getRangeModel().addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				((ADCElement)getElement()).setData(255 - scrollBar.getValue());
			}
		});
		body.setPreferredSize(BSP.GRID_SPACING*3, BSP.GRID_SPACING * 10);
		body.add(scrollBar);
		return body;
	}

	@Override
	public void refreshFigure(AbstractElement model) {
		valueLabel.setText("0x" + Integer.toHexString(((ADCElement)getElement()).getData())); //$NON-NLS-1$
	}

	
	
}
