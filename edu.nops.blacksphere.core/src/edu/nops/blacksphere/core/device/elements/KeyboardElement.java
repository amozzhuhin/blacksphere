package edu.nops.blacksphere.core.device.elements;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import edu.nops.blacksphere.core.CoreMessages;
import edu.nops.blacksphere.core.utils.NumberCellEditorValidator;

public class KeyboardElement extends AbstractChipElement {
	
	@Override
	protected void init() {
		super.init();
	}

	public static final String COLUMNS_COUNT_PROP = "keyboard.columnsCount";   //$NON-NLS-1$
	public static final String ROWS_COUNT_PROP = "keyboard.rowsCount"; //$NON-NLS-1$
	public static final String LABELS_COUNT_PROP = "keyboard.labels"; //$NON-NLS-1$

	/** Количество колонок в клавиатуре */
	private int columnsCount = 3;
	/** Количество строк в клавиатуре */
	private int rowsCount = 4;
	
	public int getColumnsCount() {
		return columnsCount;
	}

	public void setColumnsCount(int columnsCount) {
		// Изменять можно только если клавиатура не включена
		if (!isPowered() && columnsCount > 0 && columnsCount <= 32) {
			// Отсоединение проводников, для которых не останется контактов
			for (WireElement wire : new ArrayList<WireElement>(getInputWires())) {
				if (wire.getTargetPin() >= rowsCount + columnsCount)
					wire.disconnect();
			}
			int oldColumsCount = this.columnsCount;
			this.columnsCount = columnsCount;
			pressed = new boolean [rowsCount][columnsCount];
			firePropertyChange(COLUMNS_COUNT_PROP, oldColumsCount, columnsCount);
		}
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		// Изменять можно только если клавиатуране включена
		if (!isPowered() && rowsCount > 0 && rowsCount <= 32) {
			// Отсоединение проводников, для которых не останется контактов
			for (WireElement wire : new ArrayList<WireElement>(getInputWires())) {
				if (wire.getTargetPin() >= rowsCount)
					wire.disconnect();
			}
			int oldRowsCount = this.rowsCount;
			this.rowsCount = rowsCount;
			pressed = new boolean [rowsCount][columnsCount];
			firePropertyChange(ROWS_COUNT_PROP, oldRowsCount, rowsCount);
		}
	}

	@Override
	public String getFunctionName() {
		return ""; //$NON-NLS-1$
	}

	@Override
	public String getNamePrefix() {
		return "KBD"; //$NON-NLS-1$
	}

	@Override
	public int getPinCount() {
		return rowsCount+columnsCount;
	}

	@Override
	public String getPinName(int index) {
		if (index <= rowsCount)
			return "R" + String.valueOf(index-1); //$NON-NLS-1$
		else
			return "C" + String.valueOf(index - rowsCount-1); //$NON-NLS-1$
	}
	
	@Override
	public int[][] getInputPinSections() {
		int[][] inputSections  = new int[1][rowsCount];
		for (int i = 0; i < rowsCount; i++)
			inputSections[0][i] = i+1;
		return inputSections;
	}

	@Override
	public int[][] getOutputPinSections() {
		int[][] outputSections  = new int[1][columnsCount];
		for (int i = 0; i < columnsCount; i++)
			outputSections[0][i] = rowsCount+i+1;
		return outputSections;
	}

	@XStreamOmitField
	private static IPropertyDescriptor[] descriptors;
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (descriptors == null) {
			IPropertyDescriptor[] superDescriptors = super.getPropertyDescriptors();
			int len = superDescriptors.length;
			descriptors = Arrays.copyOf(superDescriptors, len+3);
			TextPropertyDescriptor colsCountProp = new TextPropertyDescriptor(COLUMNS_COUNT_PROP, CoreMessages.getString("KeyboardElement.columnsCount")); //$NON-NLS-1$
			colsCountProp.setValidator(NumberCellEditorValidator.getInstance());
			descriptors[len] = colsCountProp;
			TextPropertyDescriptor rowsCountProp = new TextPropertyDescriptor(ROWS_COUNT_PROP, CoreMessages.getString("KeyboardElement.rowsCount")); //$NON-NLS-1$
			rowsCountProp.setValidator(NumberCellEditorValidator.getInstance());
			descriptors[len+1] = rowsCountProp;
			TextPropertyDescriptor labelsProp = new TextPropertyDescriptor(LABELS_COUNT_PROP, CoreMessages.getString("KeyboardElement.labels")); //$NON-NLS-1$
			labelsProp.setCategory(FACE_CATEGORY);
			descriptors[len+2] = labelsProp;
		}
		return descriptors; 
	}

	@Override
	public Object getPropertyValue(Object propertyId) {
		if (propertyId.equals(COLUMNS_COUNT_PROP))
			return String.valueOf(getColumnsCount());
		if (propertyId.equals(ROWS_COUNT_PROP))
			return String.valueOf(getRowsCount());
		if (propertyId.equals(LABELS_COUNT_PROP))
			return getLabels();
		return super.getPropertyValue(propertyId);
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if (propertyId.equals(COLUMNS_COUNT_PROP))
			setColumnsCount(Integer.parseInt((String)value));
		else if (propertyId.equals(ROWS_COUNT_PROP))
			setRowsCount(Integer.parseInt((String)value));
		else if (propertyId.equals(LABELS_COUNT_PROP))
			setLabels((String)value);
		else super.setPropertyValue(propertyId, value);
	}

	@Override
	public boolean hasFace() {
		return true;
	}

	/** Метки кнопок, разделённые запятыми */
	private String labels = "0,1,2,3,4,5,6,7,8,9,10,11"; //$NON-NLS-1$

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		String oldLabels = this.labels;
		this.labels = labels;
		firePropertyChange(LABELS_COUNT_PROP, oldLabels, labels);
	}
	
	@XStreamOmitField
	private boolean[][] pressed;
	
	/** Установить нажаточть кнопки */
	public void setPressed(int row, int col, boolean pressed) {
		this.pressed[row][col] = pressed;
	}

	@Override
	protected void doRefresh() {
		super.doRefresh();
		int outputs = 0xffffffff;
		int inputPin = 1;
		for (int i = 0; i < getRowsCount(); i++) {
			int outputMask = 0xfffffffe;
			if (!getLogicInputValue(inputPin++))
				for (int j = 0; j < getColumnsCount(); j++) {
					if (pressed[i][j])
						outputs &= outputMask;
					outputMask <<= 1;
					outputMask |= 1;
				}
		}
		int[] pins = new int[getColumnsCount()];
		for (int i = 0; i < pins.length; i++)
			pins[i] = getRowsCount()+i+1;
		setLogicOutputsMask(outputs, pins);
	}
	
}
