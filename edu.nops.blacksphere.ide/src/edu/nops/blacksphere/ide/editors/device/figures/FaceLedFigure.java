package edu.nops.blacksphere.ide.editors.device.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import edu.nops.blacksphere.core.device.elements.LedElement;
import edu.nops.blacksphere.ide.BSP;
import edu.nops.blacksphere.ide.BSPImages;

public class FaceLedFigure extends ElementFigure {

	
	protected static final Image FIRED_IMAGE =
		BSP.getImages().getImage(BSPImages.IMG_FACE_RED_LED);  

	protected static final Image GRAYED_IMAGE =
		BSP.getImages().getImage(BSPImages.IMG_FACE_GRAY_LED);  

	protected static final int IMAGE_SIZE = FIRED_IMAGE.getImageData().width;

	private class LedDiodeBody extends Figure {

		@Override
		protected void paintFigure(Graphics graphics) {
			super.paintFigure(graphics);
			Rectangle bounds = getBounds();
			Point imagePos = new Point(
					bounds.x + (bounds.width-IMAGE_SIZE) / 2,
					bounds.y + (bounds.height-IMAGE_SIZE) / 2);
			if (((LedElement) getElement()).isFired())
				graphics.drawImage(FIRED_IMAGE, imagePos);
			else
				graphics.drawImage(GRAYED_IMAGE, imagePos);

/*			Rectangle edge = new Rectangle(bounds.x + bounds.width / 2 - BSP.GRID_SPACING / 2,
					bounds.y + bounds.height / 2 - BSP.GRID_SPACING / 2,
					BSP.GRID_SPACING,
					BSP.GRID_SPACING);
			Color color;
			if (((LedElement) getElement()).isFired())
				color = ColorConstants.red;
			else
				color = ColorConstants.white;
			graphics.setBackgroundColor(color);
			graphics.fillOval(edge);
			graphics.drawOval(edge);*/
		}
		
	}
	
	@Override
	protected Figure createBody() {
		Figure body = new LedDiodeBody();
		body.setPreferredSize(BSP.GRID_SPACING*2, BSP.GRID_SPACING*2);
		return body;
	}
}
