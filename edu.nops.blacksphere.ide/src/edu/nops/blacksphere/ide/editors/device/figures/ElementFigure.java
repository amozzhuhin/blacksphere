package edu.nops.blacksphere.ide.editors.device.figures;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;


import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.ide.BSP;

/**
 * Абстрактный класс фигуры элемента
 * Все фигуры должны быть потомками этого класса (на схеме и лицевой панели)
 * @author nops
 */
public abstract class ElementFigure extends Figure {

	public ElementFigure() {
		setFont(BSP.schemeFont);
	}
	
	
	protected void constructFigure() {
		setLayoutManager(new ToolbarLayout());
		if (isShowName())
			add(getLabel());
		add(getBody());
		setSize(getPreferredSize());
		createAnchors();
	}
	
	protected void reconstructFigure() {
		removeAll();
		body = null;
		constructFigure();
	}
	
	public void refreshFigure(AbstractElement model) {
		if (isShowName())
			getLabel().setText(getElement().getName());
	}
	
	private Figure body;
	
	public Figure getBody() {
		if (body == null)
			body = createBody();
		return body;
	}
	
	private Label label;
	
	public Label getLabel() {
		if (label == null) {
			label = createLabel();
		}
		return label;
	}
	
	/** Создание фигуры с текстом со стандартными для схемы свойствами */
	protected Label createLabel() {
		Label label = new Label(getElement().getName()) {
			@Override
			public Dimension getPreferredSize(int hint, int hint2) {
				Dimension result = super.getPreferredSize(hint, hint2);
				result.height = BSP.GRID_SPACING;
				return result;
			}

			@Override
			protected void paintFigure(Graphics graphics) {
				configureGraphics(graphics);
				super.paintFigure(graphics);
			}
		};
		label.setFont(BSP.schemeFont);
		label.setLabelAlignment(PositionConstants.MIDDLE);
		return label;
	}
	
	/** Элемент, который представляется фигурой */
	private AbstractElement element;
	
	public AbstractElement getElement() {
		return element;
	}
	
	public void setElement(AbstractElement element) {
		this.element = element;
		constructFigure();
	}
	
	/** Создание тела рисунка */
	protected abstract Figure createBody();
	
	/** Надо ли показывать имя компонента */
	protected boolean isShowName() {
		return true;
	}
	
	protected void configureGraphics(Graphics graphics) {
		// добавляет багов на win32
		//graphics.setAntialias(SWT.ON); 
		graphics.setLineWidth(2);
		graphics.setFont(BSP.schemeFont);
	}
	
	protected void drawAlignedString(Graphics graphics, String s, Point point, int align) {
		drawAlignedString(graphics, s, point.x, point.y, align);
	}

	protected void drawAlignedString(Graphics graphics, String s, int x, int y, int align) {
		Dimension dim = FigureUtilities.getTextExtents(s, graphics.getFont());
		y -= dim.height/2;
		switch (align) {
		case SWT.LEFT:
			graphics.drawString(s, x, y);
			break;
		case SWT.CENTER:
			graphics.drawString(s, x - dim.width/2, y);
			break;
		case SWT.RIGHT:
			graphics.drawString(s, x - dim.width, y);
			break;
		}
	}
	
	/** Потомки должны реализовать метод, чтобы добавить якоря соединений  */
	protected void createAnchors() {};
	
	/** Таблица номер контакта -> якорь соединения */
	protected Hashtable<Integer, ConnectionAnchor> connectionAnchors =
		new Hashtable<Integer, ConnectionAnchor>(8);
	
	public Hashtable<Integer, ConnectionAnchor> getConnectionAnchors() {
		return connectionAnchors;
	}
	
	/** Якоря входных контактов */
	protected Vector<ConnectionAnchor> inputConnectionAnchors =
		new Vector<ConnectionAnchor>(2,2);
	
	/** Якоря выходных контактов */
	protected Vector<ConnectionAnchor> outputConnectionAnchors =
		new Vector<ConnectionAnchor>(2,2);
	
	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		ConnectionAnchor closest = null;
		long min = Long.MAX_VALUE;

		Enumeration e = getSourceConnectionAnchors().elements();
		while (e.hasMoreElements()) {
			ConnectionAnchor c = (ConnectionAnchor) e.nextElement();
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}

	public Vector<ConnectionAnchor> getSourceConnectionAnchors() {
		return outputConnectionAnchors;
	}

	public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
		ConnectionAnchor closest = null;
		long min = Long.MAX_VALUE;

		Enumeration<ConnectionAnchor> e = getTargetConnectionAnchors().elements();
		while (e.hasMoreElements()) {
			ConnectionAnchor c = e.nextElement();
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}

	public Vector<ConnectionAnchor> getTargetConnectionAnchors() {
		return inputConnectionAnchors;
	}
	
	public int getAnchorPin(ConnectionAnchor anchor) {
		Enumeration<Integer> e = connectionAnchors.keys();
		while (e.hasMoreElements()) {
			Integer pin = e.nextElement();
			if (anchor.equals(connectionAnchors.get(pin)))
				return pin;
		}
		return -1;
	}
}
