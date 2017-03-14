package edu.nops.blacksphere.ide.editors.device.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import edu.nops.blacksphere.core.device.elements.LedMatrixElement;
import edu.nops.blacksphere.ide.BSP;

public class FaceLedMatrixFigure extends ElementFigure {

	private class LedMatrixBody extends Figure implements ColorConstants {

		// Эта процедура ДОЛЖНА быть как можно больше оптимизированной по времени
		@Override
		protected void paintFigure(Graphics graphics) {
			super.paintFigure(graphics);
			LedMatrixElement matrix = (LedMatrixElement) getElement();
			Rectangle bounds = getBounds().getCopy();
			boolean[] rows = matrix.getFiredRows();
			boolean[] cols = matrix.getFiredColumns();
			graphics.setBackgroundColor(white);
			graphics.fillRectangle(bounds);
			// линии разделяющие ячейки
			int xoffset = bounds.x;
			int yoffset = bounds.y;
			for (int i = 0; i < 8; i++) {
				graphics.drawLine(bounds.x, yoffset, bounds.x + bounds.width, yoffset);
				graphics.drawLine(xoffset, bounds.y, xoffset, bounds.y + bounds.height);
				xoffset += BSP.GRID_SPACING; yoffset += BSP.GRID_SPACING;
			}			
			Rectangle cell = new Rectangle(0, bounds.y, BSP.GRID_SPACING-1, BSP.GRID_SPACING-1);
			cell.y = bounds.y+1;
			graphics.setBackgroundColor(red);
			if (matrix.isPowered()) {
				for (int i = 0; i < 8; i++) {
					cell.x = bounds.x+1;
					if (rows[i])
						for (int j = 0; j < 8; j++) {
							if (cols[j])
								graphics.fillRectangle(cell);
							cell.x += BSP.GRID_SPACING;
						}
					cell.y += BSP.GRID_SPACING;
				}
			}
			bounds.width--;bounds.height--;
			graphics.drawLine(bounds.getBottomLeft(), bounds.getBottomRight());
			graphics.drawLine(bounds.getTopRight(), bounds.getBottomRight());
		}
		
	}

	@Override
	protected Figure createBody() {
		LedMatrixBody body = new LedMatrixBody();
		body.setSize(BSP.GRID_SPACING*8, BSP.GRID_SPACING*8);
		return body;
	}

}
