package edu.nops.blacksphere.ide.editors.device.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

import edu.nops.blacksphere.core.device.elements.GroundElement;
import edu.nops.blacksphere.ide.BSP;

/**
 * Фигура элемента "Земля"
 * @author nops
 */
public class SchemeGroundFigure extends ElementFigure {

	/** Запрет отображения имени */
	protected boolean isShowName() {
		return false;
	}

	private class GroundBody extends Figure {
		@Override
		protected void paintFigure(Graphics graphics) {
			configureGraphics(graphics);

			int SPACE = BSP.GRID_SPACING;
			Rectangle bounds = getBounds();
			graphics.setAntialias(SWT.ON);
			graphics.setLineWidth(2);
			graphics.drawLine(bounds.x + SPACE, bounds.y, bounds.x + SPACE, bounds.y + SPACE-1);
			graphics.setLineWidth(4);
			graphics.drawLine(bounds.x + SPACE/2, bounds.y + SPACE-1, bounds.x + SPACE + SPACE/2,
				bounds.y + SPACE-1);
		}
	}
	
	
	@Override
	protected Figure createBody() {
		GroundBody body = new GroundBody();
		body.setSize(BSP.GRID_SPACING*2, BSP.GRID_SPACING);
		return body;
	}
	
	/** Создание якорей для соединения */ 
	@Override
	protected void createAnchors() {
		FixedConnectionAnchor c;
		/* вход не нужен 
		c = new FixedConnectionAnchor(this);
		c.offsetH = BSP.GRID_SPACING;
		connectionAnchors.put(PowerElement.Ucc, c);
		inputConnectionAnchors.addElement(c);*/
		// выход
		c = new FixedConnectionAnchor(this);
		c.offsetH = BSP.GRID_SPACING;
		connectionAnchors.put(GroundElement.PIN_GND, c);
		outputConnectionAnchors.addElement(c);
		
	}
}
