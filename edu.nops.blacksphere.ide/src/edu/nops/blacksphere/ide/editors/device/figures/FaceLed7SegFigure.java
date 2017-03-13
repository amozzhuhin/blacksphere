package edu.nops.blacksphere.ide.editors.device.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import edu.nops.blacksphere.core.device.elements.Led7SegElement;
import edu.nops.blacksphere.ide.BSP;

/**
 * Фигура 7-сегментного индикатора на лицевой панели 
 * @author nops
 */
public class FaceLed7SegFigure extends ElementFigure {

	private class Led7SegBody extends Figure {

		@Override
		protected void paintFigure(Graphics graphics) {
			configureGraphics(graphics);
			graphics.setBackgroundColor(ColorConstants.white);
			graphics.fillRectangle(getBounds());
			graphics.drawRectangle(getBounds());
			graphics.setLineWidth(1);
			PointList segment = new PointList();
			Led7SegElement el = (Led7SegElement) getElement();
			segment.addPoint(8, 10);
			segment.addPoint(10, 8);
			segment.addPoint(28, 8);
			segment.addPoint(30, 10);
			segment.addPoint(28, 12);
			segment.addPoint(10, 12);
			segment.addPoint(8, 10);
			segment.performTranslate(getBounds().x + 4, getBounds().y + 4);
			graphics.setBackgroundColor(ColorConstants.green);
			graphics.setForegroundColor(ColorConstants.lightGray);
			// Сегмент A
			if (el.isSegmentAFired())
				graphics.fillPolygon(segment);
			graphics.drawPolyline(segment);
			// Сегмент G
			segment.performTranslate(0, 25);
			if (el.isSegmentGFired())
				graphics.fillPolygon(segment);
			graphics.drawPolyline(segment);
			// Сегмент D
			segment.performTranslate(0, 25);
			if (el.isSegmentDFired())
				graphics.fillPolygon(segment);
			graphics.drawPolyline(segment);
			// вертикальные сегменты
			segment = new PointList();
			segment.addPoint(10, 8);
			segment.addPoint(8, 10);
			segment.addPoint(8, 28);
			segment.addPoint(10, 30);
			segment.addPoint(12, 28);
			segment.addPoint(12, 10);
			segment.addPoint(10, 8);
			// Сегмент F
			segment.performTranslate(getBounds().x, getBounds().y + 8);
			if (el.isSegmentFFired())
				graphics.fillPolygon(segment);
			graphics.drawPolyline(segment);
			// Сегмент E
			segment.performTranslate(0, 25);
			if (el.isSegmentEFired())
				graphics.fillPolygon(segment);
			graphics.drawPolyline(segment);
			// Сегмент C
			segment.performTranslate(26, 0);
			if (el.isSegmentCFired())
				graphics.fillPolygon(segment);
			graphics.drawPolyline(segment);
			// Сегмент B
			segment.performTranslate(0, -25);
			if (el.isSegmentBFired())
				graphics.fillPolygon(segment);
			graphics.drawPolyline(segment);
			// точка (сегмент H)
			Rectangle dotRect = new Rectangle(42, 58, 4, 4);
			dotRect.performTranslate(getBounds().x, getBounds().y+4);
			if (el.isSegmentHFired())
				graphics.fillRectangle(dotRect);
			graphics.drawRectangle(dotRect);
		}
		
	}
	
	@Override
	protected Figure createBody() {
		Led7SegBody body = new  Led7SegBody();
		// 20x25
		body.setPreferredSize(BSP.GRID_SPACING*3, BSP.GRID_SPACING*5);
		return body;
	}

}
