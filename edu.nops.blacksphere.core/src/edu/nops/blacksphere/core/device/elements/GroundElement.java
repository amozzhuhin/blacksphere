package edu.nops.blacksphere.core.device.elements;

public class GroundElement extends AbstractChipElement {

	public static final int PIN_GND = 1;
	
	@Override
	public String getFunctionName() {
		return null;
	}

	@Override
	public int getPinCount() {
		return 1;
	}

	@Override
	public String getPinName(int index) {
		return "GND"; //$NON-NLS-1$
	}

	@Override
	public String getNamePrefix() {
		return "GND"; //$NON-NLS-1$
	}

	protected void doRefresh() {
		super.doRefresh();
		setLogicalOutputValue(PIN_GND, false);
	}
	
}
