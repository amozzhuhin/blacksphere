package edu.nops.blacksphere.core.device.elements;

import edu.nops.blacksphere.core.CoreMessages;

public class And3NotElement extends AbstractChipElement {

	// Марка три элемента 3И-НЕ
	protected static String DEFAULT_MARK = CoreMessages.getString("And3NotElement.DefaultMark");  //$NON-NLS-1$
	
	@Override
	protected void init() {
		super.init();
		setMark(DEFAULT_MARK);
	}

	@Override
	public String getFunctionName() {
		return "&"; //$NON-NLS-1$
	}

	@Override
	public int getPinCount() {
		return 4;
	}

	public static final int PIN_A = 1;
	public static final int PIN_B = 2;
	public static final int PIN_C = 3;
	public static final int PIN_D = 4;
	
	private static final String[]  PIN_NAMES = new String[] {"A", "B", "C", "~D"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	private static final int[][]  LEFT_PIN_SECTIONS = new int[][] {{PIN_A, PIN_B, PIN_C}};
	private static final int[][]  RIGHT_PIN_SECTIONS = new int[][] {{PIN_D}};

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
		setLogicalOutputValue(PIN_D,
				!(getLogicInputsMask(new int[] {PIN_A, PIN_B, PIN_C}) == 7));
	}
}
