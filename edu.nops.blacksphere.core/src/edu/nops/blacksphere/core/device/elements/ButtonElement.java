package edu.nops.blacksphere.core.device.elements;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import edu.nops.blacksphere.core.CoreMessages;
import edu.nops.blacksphere.core.utils.BooleanValidator;

/**
 * Класс кнопки
 * @author nops
 */
public class ButtonElement extends AbstractChipElement {

	protected static String DEFAULT_MARK = CoreMessages.getString("ButtonElement.defaultMark");  //$NON-NLS-1$
	
	@Override
	protected void init() {
		super.init();
		setMark(DEFAULT_MARK);
		bounceTimer = null;
	}

	@Override
	public String getFunctionName() {
		return "S"; //$NON-NLS-1$
	}
	
	@Override
	public String getNamePrefix() {
		return "S"; //$NON-NLS-1$
	}

	@Override
	public int getPinCount() {
		return 2;
	}

	public static final int PIN_IN = 1;
	public static final int PIN_OUT = 2;
	protected static final String[] PIN_NAMES = {"IN", "OUT"}; //$NON-NLS-1$ //$NON-NLS-2$
	
	@Override
	public String getPinName(int index) {
		return PIN_NAMES[index-1];
	}

	/** Кнопка имеет отоблажение на лицевой панели */
	public boolean hasFace() {
		return true;
	}

	public static final String LABEL_PROP = "button.label"; //$NON-NLS-1$
	
	/** Надпись на кнопке на лицевой панели */
	private String label;

	public String getLabel() {
		return label != null ? label : ""; //$NON-NLS-1$
	}

	public void setLabel(String label) {
		String oldValue = this.label;
		this.label = label;
		firePropertyChange(LABEL_PROP, oldValue, label);
	}

	@XStreamOmitField
	private static IPropertyDescriptor[] descriptors;
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (descriptors == null) {
			descriptors = super.getPropertyDescriptors();
			int len = descriptors.length;
			descriptors = Arrays.copyOf(descriptors, len+2);
			TextPropertyDescriptor labelProp =
				new TextPropertyDescriptor(LABEL_PROP,
					CoreMessages.getString("ButtonElement.LabelPropCaption")); //$NON-NLS-1$
			labelProp.setCategory(FACE_CATEGORY);
			descriptors[len] = labelProp;
			TextPropertyDescriptor bounceProp =
				new TextPropertyDescriptor(BOUNCE_PROP,
						CoreMessages.getString("ButtonElement.bounce")); //$NON-NLS-1$
			bounceProp.setValidator(BooleanValidator.getInstance());
			descriptors[len+1] = bounceProp;
		}
		return descriptors; 
	}
	
	@Override
	public Object getPropertyValue(Object propertyId) {
		if (propertyId.equals(LABEL_PROP))
			return getLabel();
		if (propertyId.equals(BOUNCE_PROP))
			return String.valueOf(isBounce());
		return super.getPropertyValue(propertyId);
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if (propertyId.equals(LABEL_PROP))
			setLabel((String)value);
		else if (propertyId.equals(BOUNCE_PROP))
			setBounce(new Boolean((String)value));
		else super.setPropertyValue(propertyId, value);
	}
	
	/** Определеяет дребезг клавиши */
	private boolean bounce;
	/** Имя свойства определяющего дребезг */
	public static final String BOUNCE_PROP = "button.bounce"; //$NON-NLS-1$
	
	public boolean isBounce() {
		return bounce;
	}

	public void setBounce(boolean bounce) {
		boolean oldBounce = this.bounce;
		this.bounce = bounce;
		firePropertyChange(BOUNCE_PROP, oldBounce, bounce);
	}

	/**
	 * Таймер дребезга.
	 * пока он не null, происходит дребезг
	 */
	@XStreamOmitField
	private Timer bounceTimer;

	/** Источник случайных значений при дребезге */
	@XStreamOmitField
	static private Random bounceSource;
	
	static {
		bounceSource = new Random();
	}
	
	/** true - кнопка нажата */
	@XStreamOmitField
	private boolean checked;

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
		if (checked && isBounce()) {
			if (bounceTimer != null)
				bounceTimer.cancel();
			bounceTimer = new Timer();
			bounceTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					bounceTimer = null;
				}
			}, 10); // 10 мс - среднее время дребезга
		}
	}
	
	protected void doRefresh() {
		super.doRefresh();
		if (bounceTimer == null)
			setLogicalOutputValue(PIN_OUT, getLogicInputValue(PIN_IN) & isChecked());
		else
			setLogicalOutputValue(PIN_OUT, bounceSource.nextBoolean());
	}


}
