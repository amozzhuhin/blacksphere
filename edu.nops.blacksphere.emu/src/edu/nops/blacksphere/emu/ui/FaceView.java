package edu.nops.blacksphere.emu.ui;

import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.emu.BSPE;
import edu.nops.blacksphere.emu.MicroPC;
import edu.nops.blacksphere.ide.editors.device.FaceEditPartFactory;

public class FaceView extends ViewPart {

	ScrollingGraphicalViewer viewer;
	MicroPC pc;
	
	@Override
	public void createPartControl(Composite parent) {
		viewer = new ScrollingGraphicalViewer();
		viewer.createControl(parent);
		configureGraphicalViewer();
	}

	@Override
	public void setFocus() {}
	
	protected void configureGraphicalViewer() {
		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
		viewer.setRootEditPart(root);
		viewer.setEditPartFactory(new FaceEditPartFactory());
		try {
			Device device = BSPE.getDevice();
			// включение всех элементов устройства
			for (AbstractElement el : device.getElements())
				el.setPowered(true);
			pc = new MicroPC(device, BSPE.getFirmware());
			viewer.setContents(device);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
