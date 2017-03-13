package edu.nops.blacksphere.core.device.elements;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Предок всех элементов (микросхем) устройства.
 * Организует работу с выводами микросхем.
 * Выводы нумеруются начиная с 1
 * @author nops
 */
public abstract class AbstractChipElement extends AbstractElement {
	
	@Override
	protected void init() {
		super.init();
		if (inputWires == null)
			inputWires = new ArrayList<WireElement>();
		if (outputWires == null)
			outputWires = new ArrayList<WireElement>();
	}
	
	public String getNamePrefix() {
		return "DD"; //$NON-NLS-1$
	}
	
	/**
	 * Получить функцию, выполняемая элементом (CPU, RG, DC, ...)
	 * @return строковое имя функции, выполняемой элементом 
	 */
	public abstract String getFunctionName(); 
	
	/**
	 * Получить количество контактов
	 * @return количество контактов
	 */
	public abstract int getPinCount();

	/**
	 * Получить имя контакта
	 * @param index номер контакта 
	 * @return имя контакта
	 */
	public abstract String getPinName(int index);
	
	/**
	 * Номера контактов по секциям расположенные слева на изображении
	 * микросхемы сверху вниз
	 * @return номера по секциям (или null)
	 */
	public int[][] getInputPinSections() {
		return new int[0][0];
	}

	/**
	 * Номера контактов по секциям расположенные справа на изображении
	 * микросхемы сверху вниз
	 * @return номера по секциям (или null)
	 */
	public int[][] getOutputPinSections() {
		return new int[0][0];
	}

	public static final String INPUT_CONNECTIONS_PROP = "chip.inputWires";  //$NON-NLS-1$
	public static final String OUTPUT_CONNECTIONS_PROP = "chip.outputWires";  //$NON-NLS-1$
	
	/** Список входящих проводников */
	private List<WireElement> inputWires;

	public List<WireElement> getInputWires() {
		return inputWires;
	}
	
	/** Список исходящих проводников */
	private List<WireElement> outputWires;

	public List<WireElement> getOutputWires() {
		return outputWires;
	}
	
	public void addWire(WireElement wire) {
		if (wire.getSource() == this) {
			getOutputWires().add(wire);
			firePropertyChange(OUTPUT_CONNECTIONS_PROP, null, wire);
		} else if (wire.getTarget() == this) {
			getInputWires().add(wire);
			firePropertyChange(INPUT_CONNECTIONS_PROP, null, wire);
		}

	}
	
	public void removeWire(WireElement wire) {
		if (getInputWires().remove(wire))
			firePropertyChange(INPUT_CONNECTIONS_PROP, null, wire);
		if (getOutputWires().remove(wire))
			firePropertyChange(OUTPUT_CONNECTIONS_PROP, null, wire);
	}
	
	protected void doRefresh() {
		super.doRefresh();
		logicInputValues = new boolean[getPinCount()];
		logicOutputValues = new boolean[logicInputValues.length];
		readInputs();
	}
	
	/** Логические значения входов */
	@XStreamOmitField
	private boolean[] logicInputValues;
	/** Логические значения выходов */
	@XStreamOmitField
	private boolean[] logicOutputValues;
	
	/** Заполнение значений всех */
	private void readInputs() {
		// TODO неподключённые - логическая 1
		/*for(int i = 0; i < logicInputValues.length; i++)
			logicInputValues[i] = true;*/
		// подключённые - определяются соединением
		for (WireElement wire : getInputWires())
			logicInputValues[wire.getTargetPin()-1] = wire.getLogicalValue();
	}
	
	/** Получить значение логического входа по его номеру */
	public boolean getLogicInputValue(int pin) {
		return logicInputValues[pin-1];
	}

	/** Установка значения выхода */
	protected void setLogicalOutputValue(int pin, boolean value) {
		logicOutputValues[pin-1] = value;
	}
	
	/** Получить значение логического выхода по его номеру */
	public boolean getLogicalOutputValue(int pin) {
		refresh(false);
		return logicOutputValues[pin-1];
	}
	
	/**
	 * Получить значение-маску по заданным контактам
	 * @param pins номера портов в порядке от младшего бита к старшему
	 * @return маска
	 */
	protected int getLogicInputsMask(int[] pins) {
		int result = 0;
		int mask = 1;
		for (int i = 0; i < pins.length; i++) {
			result |= getLogicInputValue(pins[i]) ? mask : 0x00;
			mask <<= 1;
		}
		return result;
	}
	
	/**
	 * Установить выходы по маске
	 * @param value битовая маска значений
	 * @param pins номера выводов в порядке от младшего бита к старшему
	 */
	protected void setLogicOutputsMask(int value, int[] pins) {
		for (int i = 0; i < pins.length; i++) {
			setLogicalOutputValue(pins[i], (value & 1) != 0);
			value >>= 1;
		}
	}
}

