package edu.nops.blacksphere.core.device.elements;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ObjectStreamException;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import edu.nops.blacksphere.core.CoreMessages;

/**
 * Предок всех элементов (не обязательно микросхем) устройства.
 * Реализует модель представления и базовые свойства элементов устройства:
 * <ul>
 *   <li>имя
 *   <li>позиция на схеме
 *   <li>позиция на лицевой панели
 *   <li>включённость элемента (подано ли питание)
 * </ul>
 * Предоставляет базовый алгоритм обновления состояния жлемента в процессе  эмуляции.
 * @author nops
 */
public class AbstractElement implements IPropertySource,
		Cloneable {
	
	public AbstractElement() {
		init();
	}
	
	/** Вызывается сразу после десериализации */
	private Object readResolve() throws ObjectStreamException {
		init();
		return this; 
	}
	
	protected void init() {
		powered = false;
		dirty = true;
		pcsDelegate = new PropertyChangeSupport(this);
	}
	
	/** Имя элемента (DD1, DD2, R1, C1, ...) */
	@XStreamAsAttribute
	private String name;

	/** Устновка имени элемента */
	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		firePropertyChange(NAME_PROP, oldName, name);
	}

	/** Получить имя элемента */
	public String getName() {
		return name;
	}
	
	/** 
	 * Префик имени для автоматической генерации имён
	 * элементов (DD1, DD2, DD3, .., VD1, VD2, VD3, ...)
	 */
	public String getNamePrefix() {
		return ""; //$NON-NLS-1$
	}

	/** Маркировка элемента */
	private String mark;
	
	/** Устновка марки элемента */
	public void setMark(String mark) {
		String oldMark = this.mark;
		this.mark = mark;
		firePropertyChange(MARK_PROP, oldMark, mark);
	}

	/**
	 * Получить маркировку элемента (К1810ИР82, К573РФ2, ...)
	 * @return строковое имя функции, выполняемой элементом 
	 */
	public String getMark() {
		return mark != null ? mark : ""; //$NON-NLS-1$
	}

	/**
	 * Определяет подключенность питания к элементу
	 * true - элемент работает
	 */
	@XStreamOmitField
	private boolean powered;

	public boolean isPowered() {
		return powered;
	}

	public void setPowered(boolean powered) {
		boolean oldValue = this.powered;
		this.powered = powered;
		dirty = true;
		firePropertyChange(POWER_PROP, oldValue, powered);
	}

	/** Позиция элемента на схеме */
	private Point schemeLocation;

	/** Установить позицию элемента на схеме */
	public void setSchemeLocation(Point location) {
		Point oldSchemeLocation = schemeLocation; 
		schemeLocation = location;
		firePropertyChange(SCHEME_LOCATION_PROP, oldSchemeLocation, schemeLocation);
	}

	/** Получить позицию элемента на схеме */
	public Point getSchemeLocation() {
		return schemeLocation;
	}

	/**
	 * Позиция элемента на лицевой панели
	 * Может быть null, если элемент не присутствует на лицевой панели
	 */
	private Point faceLocation;
	
	/** Установить позицию элемента на схеме */
	public void setFaceLocation(Point location) {
		Point oldFaceLocation = faceLocation; 
		faceLocation = location;
		firePropertyChange(FACE_LOCATION_PROP, oldFaceLocation, faceLocation);
	}

	/** Получить позицию элемента на схеме */
	public Point getFaceLocation() {
		return faceLocation;
	}

	/**
	 * Опредилить имеет ли элемент отображение на лицевой панели.
	 * Здесь всегда возвращается false. Если класс-потомок имеет отображение,
	 * то он должен переопределить этот метод.
	 */
	public boolean hasFace() {
		return false;
	}
	
	/** Клонирование элементов */
	@Override
	public Object clone() throws CloneNotSupportedException {
		try {
			AbstractElement clone = (AbstractElement) super.clone();
			clone.schemeLocation = schemeLocation;
			clone.name = name;
			return clone;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CloneNotSupportedException(e.getMessage());
		}
	}
	
	/** Имя категории "Схема" для описаний свойств */
	public static final String SCHEME_CATEGORY = CoreMessages.getString("AbstractElement.SchemeCategory"); //$NON-NLS-1$
	/** Имя категории "Лицевая панель" для описаний свойств */
	public static final String FACE_CATEGORY = CoreMessages.getString("AbstractElement.FaceCategory"); //$NON-NLS-1$
	
	/** Идентификатор свойства используемый при изменении позиции на схеме */
	public static final String SCHEME_LOCATION_PROP = "AbstractElement.schemeLocation"; //$NON-NLS-1$
	/** Идентификатор свойства используемый при изменении позиции на схеме */
	public static final String FACE_LOCATION_PROP = "AbstractElement.faceLocation"; //$NON-NLS-1$
	/** Идентификатор свойства используемый при изменении имени жлемента */
	public static final String NAME_PROP = "AbstractElement.name"; //$NON-NLS-1$
	/** Идентификатор свойства используемый при изменении включённости элемента */
	public static final String MARK_PROP = "AbstractElement.mark"; //$NON-NLS-1$
	/** Идентификатор свойства используемый при изменении включённости элемента */
	public static final String POWER_PROP = "AbstractElement.powered"; //$NON-NLS-1$
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	@Override
	public Object getEditableValue() {
		return this;
	}

	/** 
	 * Массив описания отображаемых свойств
	 * Один IPropertyDescriptor для каждого редактируемого свойства
	 * @see #getPropertyDescriptors()
	 * @see #getPropertyValue(Object)
	 * @see #setPropertyValue(Object, Object)
	 */
	@XStreamOmitField
	private static IPropertyDescriptor[] descriptors;

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (descriptors == null) {
			TextPropertyDescriptor schemeLocationProp =
				new TextPropertyDescriptor(SCHEME_LOCATION_PROP, CoreMessages.getString("AbstractElement.SchemeLocationTitle")); //$NON-NLS-1$
			schemeLocationProp.setCategory(SCHEME_CATEGORY);
			TextPropertyDescriptor faceLocationProp =
				new TextPropertyDescriptor(FACE_LOCATION_PROP, CoreMessages.getString("AbstractElement.FaceLocationTitle")); //$NON-NLS-1$
			faceLocationProp.setCategory(FACE_CATEGORY);
			descriptors = new IPropertyDescriptor[] {
					schemeLocationProp,
					faceLocationProp,
					new TextPropertyDescriptor(NAME_PROP, CoreMessages.getString("AbstractElement.namePropTitle")), //$NON-NLS-1$
					new TextPropertyDescriptor(MARK_PROP, CoreMessages.getString("AbstractElement.markPropTitle")) //$NON-NLS-1$
			};
		}
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object propertyId) {
		if (propertyId.equals(SCHEME_LOCATION_PROP)) 
			return getSchemeLocation();
		if (propertyId.equals(FACE_LOCATION_PROP)) 
			return getFaceLocation();
		if (propertyId.equals(NAME_PROP))
			return getName();
		if (propertyId.equals(MARK_PROP))
			return getMark();
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {	}

	@Override
	public void setPropertyValue(Object propertyId, Object value) {
		if (propertyId.equals(NAME_PROP)) {
			setName((String)value);
		} if (propertyId.equals(MARK_PROP)) {
			setMark((String)value);
		}
	}

	/** Делегат используемый для реализации поддержки оповещения изменения свойств */
	@XStreamOmitField
	private transient PropertyChangeSupport pcsDelegate;

	/** 
	 * Добавить слушателя изменения свойств этого объекта
	 * @param l слушатель
	 * @throws IllegalArgumentException если параметр равен null
	 */
	public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
		if (l == null)
			throw new IllegalArgumentException();
		pcsDelegate.addPropertyChangeListener(l);
	}

	/** 
	 * Исключить слушателя из прослушки
	 * @param l слушатель
	 */
	public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
		if (l != null)
			pcsDelegate.removePropertyChangeListener(l);
	}
	
	/** 
	 * Сообщить слушателям об изменении свойства
	 * @param property идентификатор свойства
	 * @param oldValue старое значения свойства
	 * @param newValue новое значение свойства
	 */
	protected void firePropertyChange(String property, Object oldValue,
			Object newValue) {
		if (pcsDelegate.hasListeners(property))
			pcsDelegate.firePropertyChange(property,
					oldValue, newValue);
	}

	/** true - когда элемент не требует обновления */
	@XStreamOmitField
	private	boolean dirty;
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty; 
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	/**
	 * "Ленивое" обновление 
	 * @param force - обязательное выполнение обновления
	 */
	public void refresh(boolean force) {
		// TODO вынести в отделные классы: действия не место в модели
		if (force | dirty) {
			dirty = true;
			doRefresh();
			dirty = false;
		}
	}
	
	/** 
	 * Реальные действия обновления элемента в процессе эмуляции схемы.  
	 * Потомки должны реализовать этот метод.
	 */
	protected void doRefresh() { }
}