package edu.nops.blacksphere.core.device.elements;

import edu.nops.blacksphere.core.CoreMessages;

/**
 * 7-сегментный индикатор
 * @author nops
 */
public class Led7SegElement extends AbstractChipElement {

	protected static final String DEFAULT_MARK = CoreMessages.getString("Led7SegElement.defaultMark"); //$NON-NLS-1$

	@Override
	protected void init() {
		super.init();
		firedSegments = 0;
		setMark(DEFAULT_MARK);
	}
	
	@Override
	public String getFunctionName() {
		return "HL"; //$NON-NLS-1$
	}

	@Override
	public int getPinCount() {
		return PIN_NAMES.length;
	}

	public static final int PIN_A = 1;
	public static final int PIN_B = 2;
	public static final int PIN_C = 3;
	public static final int PIN_D = 4;
	public static final int PIN_E = 5;
	public static final int PIN_F = 6;  
	public static final int PIN_G = 7;  
	public static final int PIN_H = 8;  
	public static final int PIN_CS = 9;  

	private static final String[]  PIN_NAMES =
		new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "~CS"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
	private static final int[][]  LEFT_PIN_SECTIONS = new int[][] {
		{PIN_A, PIN_B, PIN_C, PIN_D, PIN_E, PIN_F, PIN_G, PIN_H},
		{PIN_CS}
	};

	@Override
	public String getPinName(int index) {
		return PIN_NAMES[index-1];
	}

	@Override
	public int[][] getInputPinSections() {
		return LEFT_PIN_SECTIONS;
	}

	@Override
	public boolean hasFace() {
		return true;
	}
	
	protected void doRefresh() {
		super.doRefresh();
		if (!getLogicInputValue(PIN_CS))
			setFiredSegments(getLogicInputsMask(
				new int[] {PIN_A, PIN_B, PIN_C, PIN_D, PIN_E, PIN_F, PIN_G, PIN_H}));
		else
			setFiredSegments(0);
	}
	
	public static final String FIRED_SEGMENTS_PROP = "led7seg.firedSements"; //$NON-NLS-1$
	
	/** Битовая маска горящих сегментов*/
	private int firedSegments;

	protected void setFiredSegments(int firedSegments) {
		int oldFiredSegments = this.firedSegments;
		this.firedSegments = firedSegments;
		firePropertyChange(FIRED_SEGMENTS_PROP, oldFiredSegments, firedSegments);
	}
	
	/** Горит ли сегмент A */
	public boolean isSegmentAFired() {
		return isPowered() && ((firedSegments & 0x01) != 0);
	}
	
	/** Горит ли сегмент B */
	public boolean isSegmentBFired() {
		return isPowered() && ((firedSegments & 0x02) != 0);
	}
	
	/** Горит ли сегмент C */
	public boolean isSegmentCFired() {
		return isPowered() && ((firedSegments & 0x04) != 0);
	}
	
	/** Горит ли сегмент D */
	public boolean isSegmentDFired() {
		return isPowered() && ((firedSegments & 0x08) != 0);
	}
	
	/** Горит ли сегмент E */
	public boolean isSegmentEFired() {
		return isPowered() && ((firedSegments & 0x10) != 0);
	}
	
	/** Горит ли сегмент F */
	public boolean isSegmentFFired() {
		return isPowered() && ((firedSegments & 0x20) != 0);
	}
	
	/** Горит ли сегмент G */
	public boolean isSegmentGFired() {
		return isPowered() && ((firedSegments & 0x40) != 0);
	}

	/** Горит ли сегмент H */
	public boolean isSegmentHFired() {
		return isPowered() && ((firedSegments & 0x80) != 0);
	}
}
