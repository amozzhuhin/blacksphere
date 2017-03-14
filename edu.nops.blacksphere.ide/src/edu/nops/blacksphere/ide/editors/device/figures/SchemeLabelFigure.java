package edu.nops.blacksphere.ide.editors.device.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.core.device.elements.LabelElement;
import edu.nops.blacksphere.ide.BSP;

/**
 * Изображение метки с произвольным текстом
 * @author nops
 */
public class SchemeLabelFigure extends ElementFigure {
	
	private String text;
	
	private class LabelBody extends Figure {
		public LabelBody() {
			setFont(BSP.schemeFont);
			setBorder(new LineBorder());
			setBackgroundColor(ColorConstants.tooltipBackground);
			setForegroundColor(ColorConstants.tooltipForeground);
			setOpaque(true);
			//setLabelAlignment(PositionConstants.MIDDLE);
		}

		@Override
		protected void paintFigure(Graphics graphics) {
			configureGraphics(graphics);
			int SPACE = BSP.GRID_SPACING;
			graphics.fillRectangle(getBounds());
			//graphics.drawRectangle(getBounds());
			drawAlignedString(graphics, text, getBounds().x + getBounds().width/2,
					getBounds().y + SPACE, SWT.CENTER);
		}

		
	}

	@Override
	public void refreshFigure(AbstractElement model) {
		super.refreshFigure(model);
		setText(((LabelElement) getElement()).getText());
		Dimension dim = new Dimension((FigureUtilities.getTextExtents(getText(), getBody().getFont()).width / BSP.GRID_SPACING + 1) * BSP.GRID_SPACING,
				BSP.GRID_SPACING * 2);
		setSize(dim);
		getBody().setSize(dim);
		getBody().setPreferredSize(dim);
	}

	@Override
	protected Figure createBody() {
		text = ((LabelElement) getElement()).getText();
		LabelBody body = new LabelBody();
		body.setSize(BSP.GRID_SPACING * 12, BSP.GRID_SPACING * 2);
		return body;
	}

	@Override
	protected boolean isShowName() {
		return false;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
}
