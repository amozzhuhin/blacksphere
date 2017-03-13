package edu.nops.blacksphere.ide.editors.device.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

import edu.nops.blacksphere.core.device.elements.AbstractChipElement;
import edu.nops.blacksphere.ide.BSP;

/**
 * Условное графическое обозначение (УГО) элемента на схеме
 * @author nops
 */
public class SchemeChipElementFigure extends ElementFigure {

	boolean showSections = true;
	
	public SchemeChipElementFigure(boolean showSections) {
		this.showSections = showSections;
	}
	
	private int getSectionsHeight(int[][] sections) {
		int SPACE = BSP.GRID_SPACING;
		int result = showSections ? SPACE*2 : 0;
		if (sections != null &&
				sections.length > 0) {
			for (int[] section : sections)
				result += SPACE * (section.length + 1);
		}
		return result;
	}
	
	@Override
	protected Figure createBody() {
		ChipElementBody body = new ChipElementBody();
		AbstractChipElement chip = (AbstractChipElement) getElement();
		int leftHeight = getSectionsHeight(chip.getInputPinSections());
		int rightHeight = getSectionsHeight(chip.getOutputPinSections());
		body.setSize(showSections ? BSP.GRID_SPACING*8 : BSP.GRID_SPACING*4,
				leftHeight > rightHeight ? leftHeight : rightHeight);
		return body;
	}

	private class ChipElementBody extends Figure {
		
		@Override
		protected void paintFigure(Graphics graphics) {
			configureGraphics(graphics);
			paintGeneral(graphics);
			AbstractChipElement chip = (AbstractChipElement) getElement();
			int SPACE = BSP.GRID_SPACING;
			paintPins(graphics, chip.getInputPinSections(),
					getBounds().x + SPACE + SPACE/2, SWT.LEFT);
			paintPins(graphics, chip.getOutputPinSections(),
					getBounds().right() - SPACE - SPACE/2, SWT.RIGHT);
		}
		
		private void paintGeneral(Graphics graphics) {
			Rectangle r = getBounds().getCopy();
			int SPACE = BSP.GRID_SPACING;
			r.x += SPACE;
			r.y++;
			r.width -= SPACE*2;
			r.height--;r.height--;
			graphics.fillRectangle(r);
			graphics.drawRectangle(r);
			drawAlignedString(graphics, ((AbstractChipElement) getElement()).getFunctionName(),
					r.x + r.width/2, r.y + SPACE/2, SWT.CENTER);
			String mark = ((AbstractChipElement) getElement()).getMark();
			if (FigureUtilities.getTextExtents(mark, graphics.getFont()).width < r.width)
				drawAlignedString(graphics, mark,
					r.x + r.width/2, r.bottom() - SPACE/2, SWT.CENTER);
		}
		
		private void paintPins(Graphics graphics, int[][] sections, int x, int align) {
			int SPACE = BSP.GRID_SPACING;
			if (sections != null) {
				int y = getBounds().y + (showSections ? SPACE : 0);
				AbstractChipElement chip = ((AbstractChipElement) getElement());
				for (int[] section : sections) {
					y += SPACE;
					for(int pin : section) {
						if (align == SWT.LEFT)
							graphics.drawLine(x-SPACE-SPACE/2, y, x-SPACE/2, y);
						else
							graphics.drawLine(x+SPACE/2, y, x+SPACE+SPACE/2, y);
						String pinName = chip.getPinName(pin); 
						// отрицание
						if (pinName.length() > 0 && pinName.charAt(0) == '~') {
							Rectangle r;
							if (align == SWT.LEFT)
								r = new Rectangle(x-SPACE, y-SPACE/4, SPACE/2, SPACE/2);
							else
								r = new Rectangle(x+SPACE/2, y-SPACE/4, SPACE/2, SPACE/2);
							graphics.fillOval(r);
							graphics.drawOval(r);
							pinName = pinName.substring(1);
						}
						if (showSections)
							drawAlignedString(graphics, pinName, x, y, align);
						y += SPACE;
					}
				}
			}
		}
		
	}

	@Override
	protected void createAnchors() {
		super.createAnchors();
		FixedConnectionAnchor c;
		AbstractChipElement chip = ((AbstractChipElement) getElement());
		int y = showSections ? BSP.GRID_SPACING*2 : BSP.GRID_SPACING;
		// входы
		for (int[] section : chip.getInputPinSections()) {
			y += BSP.GRID_SPACING;
			for(int pin : section) {
				c = new FixedConnectionAnchor(this);
				c.offsetV = y;
				connectionAnchors.put(pin, c);
				inputConnectionAnchors.addElement(c);
				y += BSP.GRID_SPACING;
			}
		}
		// выходы
		y = showSections ? BSP.GRID_SPACING*2 : BSP.GRID_SPACING;
		for (int[] section : chip.getOutputPinSections()) {
			y += BSP.GRID_SPACING;
			for(int pin : section) {
				c = new FixedConnectionAnchor(this);
				c.offsetV = y;
				c.offsetH = getBounds().width;
				connectionAnchors.put(pin, c);
				outputConnectionAnchors.addElement(c);
				y += BSP.GRID_SPACING;
			}
		}
	}

	
}
