package edu.nops.blacksphere.core.device.elements;

import java.util.Arrays;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import edu.nops.blacksphere.core.CoreMessages;
import edu.nops.blacksphere.core.utils.NumberCellEditorValidator;

/**
 * Модель порта вывода (часть порта К1810ИР82) 
 * @author nops
 */
public class OutPortElement extends AbstractChipElement {

	protected static final String FUNCTION_NAME = "RG"; //$NON-NLS-1$
	protected static final String DEFAULT_MARK = CoreMessages.getString("OutPortElement.Mark"); //$NON-NLS-1$

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
	
	
	protected static final int Q0 = 19;
	protected static final int Q1 = 18;
	protected static final int Q2 = 17;
	protected static final int Q3 = 16;
	protected static final int Q4 = 15;
	protected static final int Q5 = 14;
	protected static final int Q6 = 13;
	protected static final int Q7 = 12;
	
	protected static final int[][] RIGHT_PIN_SECTIONS = {
		{Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7}
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
	public int[][] getOutputPinSections() {
		return RIGHT_PIN_SECTIONS;
	}

	public static final String ADDRESS_PROP = "outport.address";  //$NON-NLS-1$
	
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
			addressProp = new TextPropertyDescriptor(ADDRESS_PROP, CoreMessages.getString("OutPortElement.AddressPropCaption")); //$NON-NLS-1$
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
	
	/** Маска выходов Q0-Q7 */
	@XStreamOmitField
	private int outputData;
	

	public void setOutputData(int data) {
		outputData = data;
	}
	
	protected void doRefresh() {
		super.doRefresh();
		setLogicalOutputValue(Q0, (outputData & 0x01) != 0);
		setLogicalOutputValue(Q1, (outputData & 0x02) != 0);
		setLogicalOutputValue(Q2, (outputData & 0x04) != 0);
		setLogicalOutputValue(Q3, (outputData & 0x08) != 0);
		setLogicalOutputValue(Q4, (outputData & 0x10) != 0);
		setLogicalOutputValue(Q5, (outputData & 0x20) != 0);
		setLogicalOutputValue(Q6, (outputData & 0x40) != 0);
		setLogicalOutputValue(Q7, (outputData & 0x80) != 0);
	}
}
