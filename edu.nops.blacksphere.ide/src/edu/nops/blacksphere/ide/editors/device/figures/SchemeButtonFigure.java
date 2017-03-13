package edu.nops.blacksphere.ide.editors.device.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

import edu.nops.blacksphere.core.device.elements.ButtonElement;
import edu.nops.blacksphere.ide.BSP;

/**
 * Изображение кнопки на схеме
 * @author nops
 */
public class SchemeButtonFigure extends ElementFigure {

	/** Создание якорей для соединения */ 
	protected void createAnchors() {
		FixedConnectionAnchor c;
		// вход
		c = new FixedConnectionAnchor(this);
		c.offsetV = BSP.GRID_SPACING*2;
		connectionAnchors.put(ButtonElement.PIN_IN, c);
		inputConnectionAnchors.addElement(c);
		// выход
		c = new FixedConnectionAnchor(this);
		c.offsetV = BSP.GRID_SPACING*2;
		c.offsetH = BSP.GRID_SPACING*3;
		connectionAnchors.put(ButtonElement.PIN_OUT, c);
		outputConnectionAnchors.addElement(c);
		
	}
	
	/** Класс основной части фигуры кнопки  */
	private class ButtonBody extends Figure {
		@Override
		protected void paintFigure(Graphics graphics) {
			graphics.setAntialias(SWT.ON);
			graphics.setLineWidth(2);
			graphics.setFont(BSP.schemeFont);
			Rectangle bounds = getBounds();
			int SPACE = BSP.GRID_SPACING;
			graphics.drawLine(bounds.x, bounds.y + SPACE,
					bounds.x + SPACE, bounds.y + SPACE);
			graphics.drawLine(bounds.right() - SPACE, bounds.y + SPACE,
					bounds.right(), bounds.y + SPACE);
			graphics.drawLine(bounds.x + SPACE, bounds.y + SPACE,
					bounds.right() - SPACE, bounds.y + SPACE/2);
			graphics.drawLine(bounds.right() - SPACE, bounds.y + SPACE,
					bounds.right() - SPACE, bounds.y + SPACE - SPACE/4);
		}
	}
	
	@Override
	protected Figure createBody() {
		ButtonBody body = new ButtonBody();
		body.setSize(BSP.GRID_SPACING*3, BSP.GRID_SPACING*2);
		return body;
	}
	
}
