package edu.nops.blacksphere.ide.editors.device.figures;

import java.util.ArrayList;

import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;

import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.core.device.elements.KeyboardElement;
import edu.nops.blacksphere.ide.BSP;
import edu.nops.blacksphere.ide.editors.device.figures.ElementFigure;

public class FaceKeyboardFigure extends ElementFigure {

	private class FaceKeyboardFigureBody extends Figure implements ChangeListener {

		ArrayList<Button> buttons;
		
		public FaceKeyboardFigureBody() {
			setFont(BSP.schemeFont);
			KeyboardElement keyboard = (KeyboardElement) getElement();
			GridLayout layout = new GridLayout(keyboard.getColumnsCount(), true);
			layout.horizontalSpacing = layout.verticalSpacing = 0;
			layout.marginHeight = layout.marginWidth = 0;
			
			String[] labels = keyboard.getLabels().split(","); //$NON-NLS-1$
			setLayoutManager(layout);
			buttons = new ArrayList<Button>();
			for (int i = 0; i < keyboard.getRowsCount(); i++)
				for (int j = 0; j < keyboard.getColumnsCount(); j++) {
					int cur = i*keyboard.getColumnsCount()+j;
					String label = cur < labels.length ? labels[cur] : ""; //$NON-NLS-1$
					Button btn = new Button(label);
					btn.setEnabled(getElement().isPowered());
					btn.setPreferredSize(BSP.GRID_SPACING*2, BSP.GRID_SPACING*2);
					buttons.add(btn);
					btn.addChangeListener(this);
					add(btn);
				}
		}

		public void handleStateChanged(ChangeEvent event) {
			KeyboardElement keyboard = (KeyboardElement) getElement();
			Button btn = (Button) event.getSource();
			int index = buttons.indexOf(btn);
			int row = index / keyboard.getColumnsCount();
			int col = index % keyboard.getColumnsCount();
			keyboard.setPressed(row, col, btn.getModel().isPressed());
		}
		
		
	}
	
	@Override
	protected Figure createBody() {
		FaceKeyboardFigureBody body = new FaceKeyboardFigureBody();
		KeyboardElement keyboard = (KeyboardElement) getElement();
		body.setSize(keyboard.getColumnsCount() * BSP.GRID_SPACING * 2,
				keyboard.getRowsCount() * BSP.GRID_SPACING * 2);
		return body;
	}

	@Override
	public void refreshFigure(AbstractElement model) {
		super.refreshFigure(model);
		// Могло измениться количество колонок или строк
		// нужно пересоздать фигуру полностью
		reconstructFigure();
	}

}
