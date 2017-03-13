package edu.nops.blacksphere.core.device.elements;

import edu.nops.blacksphere.core.CoreMessages;

public class NotElement extends AbstractChipElement {

	protected static String DEFAULT_MARK = CoreMessages.getString("NotElement.defaultMark"); //$NON-NLS-1$
	
	@Override
	protected void init() {
		super.init();
		setMark(DEFAULT_MARK);
	}

	@Override
	public String getFunctionName() {
		return "1"; //$NON-NLS-1$
	}

	@Override
	public int getPinCount() {
		return 2;
	}

	public static final int PIN_IN = 1;
	public static final int PIN_OUT = 2;
	
	private static final String[] PIN_NAMES = new String[] {"D", "~D"}; //$NON-NLS-1$ //$NON-NLS-2$
	private static final int[][] LEFT_PIN_SECTIONS = new int[][] {{PIN_IN}};
	private static final int[][] RIGHT_PIN_SECTIONS = new int[][] {{PIN_OUT}};
	
	@Override
	public String getPinName(int index) {
		return PIN_NAMES[index-1];
	}

	@Override
	public int[][] getInputPinSections() {
		return LEFT_PIN_SECTIONS;
	}

	@Override
	public int[][] getOutputPinSections() {
		return RIGHT_PIN_SECTIONS;
	}

	@Override
	protected void doRefresh() {
		super.doRefresh();
		setLogicalOutputValue(PIN_OUT, !getLogicInputValue(PIN_IN));
	}
}
