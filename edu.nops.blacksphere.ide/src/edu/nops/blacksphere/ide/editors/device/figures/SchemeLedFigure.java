package edu.nops.blacksphere.ide.editors.device.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;


import edu.nops.blacksphere.core.device.elements.LedElement;
import edu.nops.blacksphere.ide.BSP;

public class SchemeLedFigure extends ElementFigure {

	/** Создание якорей для соединения */ 
	protected void createAnchors() {
		FixedConnectionAnchor c;
		// вход A
		c = new FixedConnectionAnchor(this);
		c.offsetV = BSP.GRID_SPACING*2;
		connectionAnchors.put(LedElement.PIN_A, c);
		inputConnectionAnchors.addElement(c);
		// вход K
		c = new FixedConnectionAnchor(this);
		c.offsetH = BSP.GRID_SPACING*3;
		c.offsetV = BSP.GRID_SPACING*2;
		connectionAnchors.put(LedElement.PIN_K, c);
		inputConnectionAnchors.addElement(c);	}
	
	private class LedDiodeBody extends Figure {
		@Override
		protected void paintFigure(Graphics graphics) {
			configureGraphics(graphics);
			Rectangle bounds = getBounds();
			int SPACE = BSP.GRID_SPACING;
			// контакты
			graphics.drawLine(bounds.x, bounds.y + SPACE,
					bounds.x + bounds.width, bounds.y + SPACE);
			// стрелки
			graphics.pushState();
			try {
				graphics.setLineWidth(1);
				// левая стрелка
				graphics.drawLine(bounds.x + (int) (SPACE * 1.5), bounds.y + SPACE/2,
						bounds.x + (int) (SPACE * 1.8), bounds.y);
				graphics.drawLine(bounds.x + (int) (SPACE * 1.75), bounds.y + SPACE/4,
						bounds.x + (int) (SPACE * 1.8), bounds.y);
				graphics.drawLine(bounds.x + (int) (SPACE * 1.6), bounds.y + SPACE/5,
						bounds.x + (int) (SPACE * 1.8), bounds.y);
				// правая стрелка
				graphics.drawLine(bounds.x + (int) (SPACE * 1.75), bounds.y + SPACE/2,
						bounds.x + (int) (SPACE * 2), bounds.y);
				graphics.drawLine(bounds.x + (int) (SPACE * 1.95), bounds.y + SPACE/4,
						bounds.x + (int) (SPACE * 2), bounds.y);
				graphics.drawLine(bounds.x + (int) (SPACE * 1.85), bounds.y + SPACE/5,
						bounds.x + (int) (SPACE * 2), bounds.y);
			} finally {
				graphics.popState();
			}
			// закрашенный треугольник
			PointList pointList = new PointList();
			pointList.addPoint(bounds.x + SPACE*2, bounds.y + SPACE);
			pointList.addPoint(bounds.x + SPACE, bounds.y + SPACE/2 - 1);
			pointList.addPoint(bounds.x + SPACE, bounds.y + SPACE + SPACE/2 + 1);
			graphics.fillPolygon(pointList);
			graphics.drawPolygon(pointList);
			// палка :)
			graphics.drawLine(bounds.x + SPACE*2, bounds.y + SPACE/2,
					bounds.x + SPACE * 2, bounds.y + SPACE + SPACE/2 + 1);
			
		}
	}
	
	@Override
	protected Figure createBody() {
		LedDiodeBody body = new LedDiodeBody();
		body.setSize(BSP.GRID_SPACING*3, BSP.GRID_SPACING*2);
		return body;
	}

}
