package edu.nops.blacksphere.core.device;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import edu.nops.blacksphere.core.CoreMessages;
import edu.nops.blacksphere.core.device.elements.AbstractElement;

/**
 * Описание устройства  
 * @author nops
 */
@XStreamAlias("device") //$NON-NLS-1$
public class Device implements IPropertySource {

	public Device() {
		descr = CoreMessages.getString("Device.DeviceDescription"); //$NON-NLS-1$
		frequency = DEFAULT_FREQUENCY;
		ram = 4096;
		rom = 4096;
	}
	
	/** Вызывается при десериализации */
	private Object readResolve() throws ObjectStreamException {
		return this; 
	}
	
	public static final String AUTHOR_PROP = "device.author"; //$NON-NLS-1$

	/** Автор устройства */
	private String author;
	
	/**
	 * Получение автора устройства
	 * @return строка автора
	 */
	public String getAuthor() {
		return author;
	}
	
	/**
	 * Установка строки идентификации автора устройства
	 * @param author информация о авторе 
	 */
	public void setAuthor(String author) {
		String oldValue = this.author;
		this.author = author;
		firePropertyChange(AUTHOR_PROP, oldValue, author);
	}

	/**
	 * Описание устройства
	 */
	private String descr;

	public String getDescr() {
		return descr;
	}
	
	public void setDescr(String descr) {
		this.descr = descr;
	}

	/** Объём ПЗУ устройства */
	private int rom;
	
	public int getROM() {
		return rom;
	}
	
	public void setROM(int rom) {
		this.rom = rom;
	}
	
	/** Объём ОЗУ устройства */
	private int ram;

	public int getRAM() {
		return ram;
	}
	
	public void setRAM(int ram) {
		this.ram = ram;
	}
	
	/** Частота процессора по умолчанию */
	public static final int DEFAULT_FREQUENCY = 5 * 100;
	
	/** Частота процессора в кГц */
	private int frequency;

	public int getFrequency() {
		return frequency;
	}
	
	public void setFrequency(int frequency) {
		this.frequency = frequency > 0 ? frequency : DEFAULT_FREQUENCY;
	}

	/** Элементы устройства */
	private List<AbstractElement> elements = new ArrayList<AbstractElement>();

	/** Идентификатор свойства добавления элемента в устрйоство */
	public static final String ELEMENT_ADDED_PROP = "Device.ElementAdded"; //$NON-NLS-1$
	/** Идентификатор свойства удаление элемента из устрйоства */
	public static final String ELEMENT_REMOVED_PROP = "Device.ElementRemoved"; //$NON-NLS-1$
	
	public List<AbstractElement> getElements() {
		return elements;
	}
	
	/**
	 * Добавить элемент в устройство
	 * @param element добавляемый элемент
	 * @return true если успешно добавлен
	 */
	public boolean addElement(AbstractElement element) {
		if (element != null && elements.add(element)) {
			firePropertyChange(ELEMENT_ADDED_PROP, null, element);
			return true;
		}
		return false;
	}

	/**
	 * Удалить элемент из устройство
	 * @param element добавляемый элемент
	 * @return true если успешно добавлен
	 */
	public boolean removeElement(AbstractElement element) {
		if (element != null && elements.remove(element)) {
			firePropertyChange(ELEMENT_REMOVED_PROP, null, element);
			return true;
		}
		return false;
	}

	/** Найти элемент по имени */
	public AbstractElement getElementByName(String name) {
		for (AbstractElement element : elements) {
			if (element.getName().equals(name))
				return element;
		}
		return null;
	}

	/**
	 * Получить свободное имя для элемента
	 * @param prefix префикс имени для элемента (DD, R, C..)
	 */
	public String getWhiteName(String prefix) {
		for (int i = 1; ; i++) {
			String name = prefix + String.valueOf(i);
			if (getElementByName(name) == null) {
				return name;
			}
		}
	}

	/** Делегат используемый для поддержки слушателей свойств */
	@XStreamOmitField
	private transient PropertyChangeSupport pcsDelegate;

	public PropertyChangeSupport getPropertyChangeSupport() {
		if (pcsDelegate == null) {
			pcsDelegate = new PropertyChangeSupport(this);
		}
		return pcsDelegate;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[0];
	}

	@Override
	public Object getPropertyValue(Object id) {
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) { }

	@Override
	public void setPropertyValue(Object id, Object value) {	}

	/** 
	 * Зарегистрировать нового слушателя свойств
	 * @param listner слушатель PropertyChangeListener
	 * @throws IllegalArgumentException если listner == null
	 */
	public synchronized void addPropertyChangeListener(
			PropertyChangeListener listner) {
		if (listner == null) {
			throw new IllegalArgumentException();
		}
		getPropertyChangeSupport().addPropertyChangeListener(listner);
	}

	/** 
	 * Исключить слушателя свойств из прослушки
	 * @param listener слушатель PropertyChangeListener
	 */
	public synchronized void removePropertyChangeListener(
			PropertyChangeListener listener) {
		if (listener != null) {
			getPropertyChangeSupport().removePropertyChangeListener(listener);
		}
	}

	/** 
	 * Сообщить слушателям свойств, что произошло изменение
	 * @param property имя свойства
	 * @param oldValue старое значение свойства
	 * @param newValue новое значение свойства
	 */
	protected void firePropertyChange(String property, Object oldValue,
			Object newValue) {
		if (getPropertyChangeSupport().hasListeners(property)) {
			getPropertyChangeSupport().firePropertyChange(property,
					oldValue, newValue);
		}
	}

	/** Обновить все элементы */
	public void refresh() {
		// Устанавливаем всем элементам флаг необновлености
		for (AbstractElement el : getElements())
			el.setDirty(true);
		// Обновляем все элементы, некоторые в следстие зависимостей
		// могут обновиться раньше, чем до них
		// дайтёт очередь в этом цикле поэтому force = false 
		for (AbstractElement el : getElements())
			el.refresh(false);
	}
	
}
