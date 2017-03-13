package edu.nops.blacksphere.ide.editors.device.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import edu.nops.blacksphere.core.device.elements.PowerElement;
import edu.nops.blacksphere.ide.BSP;

/**
 * Фигура элемента "Питание"
 * @author nops
 */
public class SchemePowerFigure extends ElementFigure {
	
	/** Запрет отображения имени */
	protected boolean isShowName() {
		return false;
	}

	private class PowerBody extends Figure {
		@Override
		protected void paintFigure(Graphics graphics) {
			configureGraphics(graphics);
			Rectangle bounds = getBounds();
			int SPACE = BSP.GRID_SPACING;
			Rectangle rect = new Rectangle(bounds.x + SPACE - SPACE/4, bounds.y + SPACE - SPACE/4, SPACE/2, SPACE/2);
			graphics.fillOval(rect);
			graphics.drawOval(rect);
			graphics.drawLine(rect.getBottomLeft(), rect.getTopRight());
		}
	}

	@Override
	protected Figure createBody() {
		PowerBody body = new PowerBody();
		body.setSize(BSP.GRID_SPACING*2, BSP.GRID_SPACING*2);
		return body;
	}

	/** Создание якорей для соединения */ 
	@Override
	protected void createAnchors() {
		FixedConnectionAnchor c;
		/* вход не нужен 
		c = new FixedConnectionAnchor(this);
		c.offsetV = BSP.GRID_SPACING;
		c.offsetH = BSP.GRID_SPACING;
		connectionAnchors.put(PowerElement.Ucc, c);
		inputConnectionAnchors.addElement(c);*/
		// выход
		c = new FixedConnectionAnchor(this);
		c.offsetV = BSP.GRID_SPACING;
		c.offsetH = BSP.GRID_SPACING;
		connectionAnchors.put(PowerElement.PIN_UCC, c);
		outputConnectionAnchors.addElement(c);
	}
}
