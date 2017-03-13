package edu.nops.blacksphere.core.device.elements;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import edu.nops.blacksphere.core.CoreMessages;

/**
 * Соединение между двумя элементами
 * @author nops
 */
public class WireElement extends AbstractElement {

	public static String SOURCE_PROP = "wire.source"; //$NON-NLS-1$
	public static String SOURCE_PIN_PROP = "wire.sourcePin"; //$NON-NLS-1$
	public static String TARGET_PROP = "wire.target"; //$NON-NLS-1$
	public static String TARGET_PIN_PROP = "wire.targetProp"; //$NON-NLS-1$
	
	/** Элемент начала соединения */
	private AbstractChipElement source;
	/** Номер вывода начала соединения */
	private int sourcePin;
	
	/** Элемент конца соединения */
	private AbstractChipElement target;
	/** Номер вывода конца соединения */
	private int targetPin;
	
	public void setSource(AbstractChipElement newSource, int pin) {
		if (source != null)
			source.removeWire(this);
		source = newSource;
		sourcePin = pin;
		source.addWire(this);
	}

	public AbstractChipElement getSource() {
		return source;
	}
	
	public void setSourcePin(int pinNumber) {
		sourcePin = pinNumber;
	}
	
	public int getSourcePin() {
		return sourcePin;
	}
	
	public void setTarget(AbstractChipElement newTarget, int pin) {
		if (target != null)
			target.removeWire(this);
		target = newTarget;
		targetPin = pin;
		target.addWire(this);
	}

	public AbstractChipElement getTarget() {
		return target;
	}
	
	public void setTargetPin(int pinNumber) {
		targetPin = pinNumber;
	}
	
	public int getTargetPin() {
		return targetPin;
	}
	
	public void disconnect() {
		if (source != null)
			source.removeWire(this);
		if (target != null)
			target.removeWire(this);
	}

	public void connect() {
		if (source != null)
			source.addWire(this);
		if (target != null)
			target.addWire(this);
	}

	/**
	 * Получить логическое значение всего соединения.
	 * Определяется элементом начала соединения.
	 */
	public boolean getLogicalValue() {
		return getSource().getLogicalOutputValue(getSourcePin());
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[4];
		descriptors[0] = new TextPropertyDescriptor(SOURCE_PROP, CoreMessages.getString("WireElement.source")); //$NON-NLS-1$
		descriptors[1] = new TextPropertyDescriptor(SOURCE_PIN_PROP, CoreMessages.getString("WireElement.sourcePin")); //$NON-NLS-1$
		descriptors[2] = new TextPropertyDescriptor(TARGET_PROP, CoreMessages.getString("WireElement.target")); //$NON-NLS-1$
		descriptors[3] = new TextPropertyDescriptor(TARGET_PIN_PROP, CoreMessages.getString("WireElement.targetPin")); //$NON-NLS-1$
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object propertyId) {
		if (propertyId.equals(SOURCE_PROP))
			return getSource() != null ? getSource().getName() : ""; //$NON-NLS-1$
		if (propertyId.equals(SOURCE_PIN_PROP))
			return getSourcePin();
		if (propertyId.equals(TARGET_PROP))
			return getTarget() != null ? getTarget().getName() : ""; //$NON-NLS-1$
		if (propertyId.equals(TARGET_PIN_PROP))
			return getTargetPin();
		return null;
	}
	
	
	
}
