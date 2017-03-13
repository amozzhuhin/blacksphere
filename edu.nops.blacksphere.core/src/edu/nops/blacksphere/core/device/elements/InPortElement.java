package edu.nops.blacksphere.core.device.elements;

import java.util.Arrays;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import edu.nops.blacksphere.core.CoreMessages;
import edu.nops.blacksphere.core.utils.NumberCellEditorValidator;

public class InPortElement extends AbstractChipElement {

	protected static final String FUNCTION_NAME = "RG"; //$NON-NLS-1$
	protected static final String DEFAULT_MARK = CoreMessages.getString("InPortElement.Mark"); //$NON-NLS-1$

	@Override
	protected void init() {
		super.init();
		setMark(DEFAULT_MARK);
	}
	
	@Override
	public String getFunctionName() {
		return FUNCTION_NAME;
	}

	protected static final String[] PIN_NAMES = {
		"D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		"~OE", "GND", "STB", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		"Q7", "Q6", "Q5", "Q4", "Q3", "Q2", "Q1", "Q0", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		"Ucc" //$NON-NLS-1$
	};

	public static int D0 = 1; 
	public static int D1 = 2; 
	public static int D2 = 3; 
	public static int D3 = 4; 
	public static int D4 = 5; 
	public static int D5 = 6; 
	public static int D6 = 7; 
	public static int D7 = 8; 
	
	protected static final int[][] LEFT_PIN_SECTIONS = {
		{D0, D1, D2, D3, D4, D5, D6, D7}
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
		return LEFT_PIN_SECTIONS;
	}

	public static final String ADDRESS_PROP = "inport.address";  //$NON-NLS-1$
	
	/** Адрес порта ввода */
	private int address;
	
	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}
	
	@XStreamOmitField
	private static IPropertyDescriptor[] descriptors;
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (descriptors == null) {
			descriptors = super.getPropertyDescriptors();
			int len = descriptors.length;
			descriptors = Arrays.copyOf(descriptors, len+1);
			TextPropertyDescriptor addressProp;
			addressProp = new TextPropertyDescriptor(ADDRESS_PROP, CoreMessages.getString("InPortElement.AddressProp")); //$NON-NLS-1$
			addressProp.setValidator(NumberCellEditorValidator.getInstance());
			descriptors[len] = addressProp;
		}
		return descriptors; 
	}
	
	@Override
	public Object getPropertyValue(Object propertyId) {
		if (propertyId.equals(ADDRESS_PROP))
			return new Integer(getAddress()).toString();
		return super.getPropertyValue(propertyId);
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if (propertyId.equals(ADDRESS_PROP))
			setAddress(Integer.parseInt((String)value));
		super.setPropertyValue(propertyId, value);
	}

	/** Маска входов D0-D7 */
	@XStreamOmitField
	private int inputData;
	
	/** Получить значение c входов D0-D7 порта ввода */
	public int getInputData() {
		return inputData;
	}
	
	public void doRefresh() {
		super.doRefresh();
		inputData = getLogicInputsMask(new int[] {D0,D1,D2,D3,D4,D5,D6,D7});
	}
}
