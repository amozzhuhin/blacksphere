package edu.nops.blacksphere.core.device.elements;

import java.util.Arrays;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class LedMatrixElement extends AbstractChipElement {

	
	@Override
	protected void init() {
		super.init();
		firedRows = new boolean[8];
		firedColumns = new boolean[8];
	}

	public static final String FUNCTION_NAME = "DPY"; //$NON-NLS-1$
	public static final String NAME_PREFIX = "HG"; //$NON-NLS-1$
	
	@Override
	public String getFunctionName() {
		return FUNCTION_NAME;
	}

	@Override
	public String getNamePrefix() {
		return NAME_PREFIX;
	}

	public static final int PIN_A0 = 1;
	public static final int PIN_A1 = 2;
	public static final int PIN_A2 = 3;
	public static final int PIN_A3 = 4;
	public static final int PIN_A4 = 5;
	public static final int PIN_A5 = 6;
	public static final int PIN_A6 = 7;
	public static final int PIN_A7 = 8;
	
	public static final int PIN_K0 = 9;
	public static final int PIN_K1 = 10;
	public static final int PIN_K2 = 11;
	public static final int PIN_K3 = 12;
	public static final int PIN_K4 = 13;
	public static final int PIN_K5 = 14;
	public static final int PIN_K6 = 15;
	public static final int PIN_K7 = 16;

	public static final int PIN_E = 17;
	
	public static String[] PIN_NAMES = {
		"A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		"K0", "K1", "K2", "K3", "K4", "K5", "K6", "K7", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		"~E" //$NON-NLS-1$
	};

	public static int[][] INPUT_PIN_SECTIONS = {
		{PIN_A0, PIN_A1, PIN_A2, PIN_A3, PIN_A4, PIN_A5, PIN_A6, PIN_A7},
		{PIN_K0, PIN_K1, PIN_K2, PIN_K3, PIN_K4, PIN_K5, PIN_K6, PIN_K7},
		{PIN_E}
	};
	
	@Override
	public int getPinCount() {
		return PIN_NAMES.length;
	}

	@Override
	public String getPinName(int index) {
		return PIN_NAMES[index-1];
	}

	@Override
	public int[][] getInputPinSections() {
		return INPUT_PIN_SECTIONS;
	}

	@Override
	public boolean hasFace() {
		return true;
	}

	@XStreamOmitField
	private boolean[] firedRows;  
	@XStreamOmitField
	private boolean[] firedColumns;  
	
	public static final String FIRED_ROWS_PROP = "ledmatrix.firedRows"; //$NON-NLS-1$
	public static final String FIRED_COLUMNS_PROP = "ledmatrix.firedColumns"; //$NON-NLS-1$
	
	public boolean[] getFiredColumns() {
		return firedColumns;
	}

	public void setFiredColumns(boolean[] firedColumns) {
		boolean[] oldFiredColumns = this.firedColumns;
		this.firedColumns = firedColumns;
		if (!Arrays.equals(oldFiredColumns, firedColumns))
			firePropertyChange(FIRED_COLUMNS_PROP, oldFiredColumns, firedColumns);
	}

	public boolean[] getFiredRows() {
		return firedRows;
	}

	public void setFiredRows(boolean[] firedRows) {
		boolean[] oldFiredRows = this.firedRows;
		this.firedRows = firedRows;
		if (!Arrays.equals(oldFiredRows, firedRows))
			firePropertyChange(FIRED_ROWS_PROP, oldFiredRows, firedRows);
	}

	@Override
	protected void doRefresh() {
		super.doRefresh();
		if (!getLogicInputValue(PIN_E)) {
			boolean[] rows = new boolean[8];
			for (int i = PIN_K0; i <= PIN_K7; i++)
				rows[i-PIN_K0] = !getLogicInputValue(i);
			setFiredRows(rows);
			boolean[] cols = new boolean[8];
			for (int i = PIN_A0; i <= PIN_A7; i++)
				cols[i-PIN_A0] = getLogicInputValue(i);
			setFiredColumns(cols);
		} else {
			boolean[] hidden = new boolean[8];
			for (int i = 0; i < hidden.length; i++)
				hidden[i] = false;
			setFiredColumns(hidden);
			setFiredRows(hidden);
		}
	}
	
	
}
