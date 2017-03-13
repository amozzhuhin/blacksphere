package edu.nops.blacksphere.core.device.elements;

import java.util.Arrays;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import edu.nops.blacksphere.core.CoreMessages;

/**
 * Элемент "метка", отображающий произвольный текст 
 * @author nops
 */
public class LabelElement extends AbstractElement {

	/** Идентификатор свойства текста метки */
	public static String TEXT_PROP = LabelElement.class + ".text"; //$NON-NLS-1$

	/** Текст метки */
	String text;

	/** Установить текст метки */
	public void setText(String text) {
		String oldText = this.text; 
		this.text = text;
		firePropertyChange(TEXT_PROP, oldText, text);
	}

	/** Подучить текст метки */
	public String getText() {
		return text != null ? text : getName();
	}

	@XStreamOmitField
	private static IPropertyDescriptor[] descriptors;
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (descriptors == null) {
			descriptors = super.getPropertyDescriptors();
			int len = descriptors.length;
			descriptors = Arrays.copyOf(descriptors, len+1);
			descriptors[len] = new TextPropertyDescriptor(TEXT_PROP, CoreMessages.getString("LabelElement.TextPropCaption")); //$NON-NLS-1$
		}
		return descriptors; 
	}

	@Override
	public Object getPropertyValue(Object propertyId) {
		if (propertyId.equals(TEXT_PROP))
			return getText();
		return super.getPropertyValue(propertyId);
	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if (propertyId.equals(TEXT_PROP))
			setText((String)value);
		super.setPropertyValue(propertyId, value);
	}

	@Override
	public String getNamePrefix() {
		return "LABEL"; //$NON-NLS-1$
	}
	
	
}
