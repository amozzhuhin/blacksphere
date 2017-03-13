package edu.nops.blacksphere.core.device.elements;

public class PowerElement extends AbstractChipElement {

	public static final int PIN_UCC = 1;
	
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
		return "Ucc"; //$NON-NLS-1$
	}

	@Override
	public String getNamePrefix() {
		return "Ucc"; //$NON-NLS-1$
	}
	
	protected void doRefresh() {
		super.doRefresh();
		setLogicalOutputValue(PIN_UCC, true);
	}
	
}
