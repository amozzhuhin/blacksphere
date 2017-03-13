package edu.nops.blacksphere.ide.editors.device.figures;

import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.core.device.elements.ButtonElement;
import edu.nops.blacksphere.ide.BSP;

public class FaceButtonFigure extends ElementFigure{

	private ContentsButton body;
	
	private class ContentsButton extends Button  implements ChangeListener {
		public ContentsButton(String label) {
			super(label);
			this.getModel().addChangeListener(this);
		}
		@Override
		public void setContents(IFigure figure) {
			super.setContents(figure);
		}
		public void handleStateChanged(ChangeEvent event) {
			((ButtonElement)getElement()).setChecked(getModel().isPressed());
		}
	}
	
	@Override
	protected Figure createBody() {
		body = new ContentsButton(((ButtonElement)getElement()).getLabel());
		body.setEnabled(getElement().isPowered());
		body.setPreferredSize(BSP.GRID_SPACING*2,BSP.GRID_SPACING*2);
		return body;
	}

	@Override
	public void refreshFigure(AbstractElement model) {
		super.refreshFigure(model);
		Label label = new Label(((ButtonElement) model).getLabel());
		label.setEnabled(model.isPowered());
		body.setContents(label);
		body.setEnabled(model.isPowered());
	}
}
