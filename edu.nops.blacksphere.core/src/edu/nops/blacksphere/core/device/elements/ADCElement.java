package edu.nops.blacksphere.core.device.elements;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Аналого-цифровой преобразователь
 * @author nops
 */
public class ADCElement extends AbstractChipElement {

	
	@Override
	protected void init() {
		super.init();
		data = 0;
		ready = true;
	}

	@Override
	public String getFunctionName() {
		return "A/#"; //$NON-NLS-1$
	}

	@Override
	public int getPinCount() {
		return PIN_NAMES.length;
	}

	protected static final String[] PIN_NAMES = {
		"D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		"CO", "RDY"  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
	};

	public static final int PIN_D0 = 1;
	public static final int PIN_D1 = 2;
	public static final int PIN_D2 = 3;
	public static final int PIN_D3 = 4;
	public static final int PIN_D4 = 5;
	public static final int PIN_D5 = 6;
	public static final int PIN_D6 = 7;
	public static final int PIN_D7 = 8;
	public static final int PIN_CO = 9;
	public static final int PIN_RDY = 10;

	protected static final int[][] INPUT_PIN_SECTIONS = {{PIN_CO}}; 
	protected static final int[][] OUTPUT_PIN_SECTIONS = {
		{PIN_D0, PIN_D1, PIN_D2, PIN_D3, PIN_D4, PIN_D5, PIN_D6, PIN_D7},
		{PIN_RDY}
	}; 
	
	@Override
	public String getPinName(int index) {
		return PIN_NAMES[index-1];
	}

	@Override
	public int[][] getInputPinSections() {
		return INPUT_PIN_SECTIONS;
	}

	@Override
	public int[][] getOutputPinSections() {
		return OUTPUT_PIN_SECTIONS;
	}

	@Override
	public boolean hasFace() {
		return true;
	}
	
	public static final String DATA_PROP = "adc.data"; //$NON-NLS-1$
	
	/** Значение снимаемое с выходов АЦП */
	@XStreamOmitField
	private int data;
	/** Готовность АЦП к выдаче данных */
	@XStreamOmitField
	private boolean ready;

	public int getData() {
		return data;
	}

	public void setData(int data) {
		int oldData = this.data;
		this.data = data;
		firePropertyChange(DATA_PROP, oldData, data);
	}

	@Override
	protected void doRefresh() {
		super.doRefresh();
		setLogicOutputsMask(getData(),
				new int[] {PIN_D0, PIN_D1, PIN_D2, PIN_D3, PIN_D4, PIN_D5, PIN_D6, PIN_D7});
		setLogicalOutputValue(PIN_RDY, ready);
	}
	
	
}
