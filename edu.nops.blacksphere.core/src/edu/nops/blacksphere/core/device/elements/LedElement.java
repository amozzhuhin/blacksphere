package edu.nops.blacksphere.core.device.elements;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import edu.nops.blacksphere.core.CoreMessages;

/**
 * Класс светодиода 
 * @author nops
 */
public class LedElement extends AbstractChipElement {

	protected static final String NAME_PREFIX = "VD"; //$NON-NLS-1$
	protected static final String DEFAULT_MARK = CoreMessages.getString("LedDiodeElement.Mark"); //$NON-NLS-1$

	protected void init() {
		super.init();
		fired = false;
		setMark(DEFAULT_MARK);
	}
		
	@Override
	public String getNamePrefix() {
		return NAME_PREFIX;
	}

	@Override
	public String getFunctionName() {
		return null;
	}

	public static final int PIN_A = 1;
	public static final int PIN_K = 2;
	protected static final String[] PIN_NAMES = {"A", "K"}; //$NON-NLS-1$ //$NON-NLS-2$
		
	@Override
	public int getPinCount() {
		return PIN_NAMES.length;
	}

	@Override
	public String getPinName(int index) {
		return PIN_NAMES[index-1];
	}

	@Override
	public boolean hasFace() {
		return true;
	}
	
	@Override
	public Object getPropertyValue(Object propertyId) {
		if (propertyId.equals(FIRED_PROP))
			return isFired();
		return super.getPropertyValue(propertyId);
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if (propertyId.equals(FIRED_PROP))
			setFired(Boolean.parseBoolean((String)value));
		super.setPropertyValue(propertyId, value);
	}

	public static final String FIRED_PROP = "leddiode.fired"; //$NON-NLS-1$
	
	/** true - диод горит */
	@XStreamOmitField
	private boolean fired;

	public boolean isFired() {
		return isPowered() & fired;
	}

	protected void setFired(boolean fired) {
		boolean oldFired = this.fired;
		this.fired = fired;
		firePropertyChange(FIRED_PROP, oldFired, fired);
	}
	
	public void doRefresh() {
		super.doRefresh();
		setFired(getLogicInputValue(PIN_A)/* & !getLogicInputValue(PIN_K)*/);
	}
}
