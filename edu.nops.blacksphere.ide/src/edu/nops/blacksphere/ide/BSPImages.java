package edu.nops.blacksphere.ide;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;

/**
 * Класс описыващий получение картинок для расширения
 * @author nops
 */
public class BSPImages implements ISharedImages {

	/** Идентификатор картинки соединения (размер 16x16) */
	public static final String IMG_CONNECTION_16 = "components/connection16.png"; //$NON-NLS-1$
	/** Идентификатор картинки соединения (размер 24x24) */
	public static final String IMG_CONNECTION_24 = "components/connection24.png"; //$NON-NLS-1$
	
	/** Идентификатор картинки порта вывода (размер 16x16) */
	public static final String IMG_OUTPORT_16 = "components/outport16.png"; //$NON-NLS-1$
	/** Идентификатор картинки порта вывода (размер 24x24) */
	public static final String IMG_OUTPORT_24 = "components/outport24.png"; //$NON-NLS-1$
	
	/** Идентификатор картинки порта ввода (размер 16x16) */
	public static final String IMG_INPORT_16 = "components/inport16.png"; //$NON-NLS-1$
	/** Идентификатор картинки порта ввода (размер 24x24) */
	public static final String IMG_INPORT_24 = "components/inport24.png"; //$NON-NLS-1$
	
	/** Идентификатор картинки клеммы (размер 16x16) */
	public static final String IMG_POWER_16 = "components/power16.png"; //$NON-NLS-1$
	/** Идентификатор картинки клеммы (размер 24x24) */
	public static final String IMG_POWER_24 = "components/power24.png"; //$NON-NLS-1$
	
	/** Идентификатор картинки общего провода (размер 16x16) */
	public static final String IMG_GROUND_16 = "components/ground16.png"; //$NON-NLS-1$
	/** Идентификатор картинки обзего провода (размер 24x24) */
	public static final String IMG_GROUND_24 = "components/ground24.png"; //$NON-NLS-1$
	
	/** Идентификатор картинки светодиода (размер 16x16) */
	public static final String IMG_LED_DIODE_16 = "components/led_diode16.png"; //$NON-NLS-1$
	/** Идентификатор картинки свтодиода (размер 24x24) */
	public static final String IMG_LED_DIODE_24 = "components/led_diode24.png"; //$NON-NLS-1$
	
	/** Идентификатор картинки метки текста (размер 16x16) */
	public static final String IMG_LABEL_16 = "components/label16.png"; //$NON-NLS-1$
	/** Идентификатор картинки метки текста (размер 24x24) */
	public static final String IMG_LABEL_24 = "components/label24.png"; //$NON-NLS-1$

	/** Идентификатор картинки порта вывода (размер 16x16) */
	public static final String IMG_BUTTON_16 = "components/button16.png"; //$NON-NLS-1$
	/** Идентификатор картинки порта вывода (размер 24x24) */
	public static final String IMG_BUTTON_24 = "components/button24.png"; //$NON-NLS-1$

	/** Идентификатор картинки порта вывода (размер 16x16) */
	public static final String IMG_KEYBOARD_16 = "components/keyboard16.png"; //$NON-NLS-1$
	/** Идентификатор картинки порта вывода (размер 24x24) */
	public static final String IMG_KEYBOARD_24 = "components/keyboard24.png"; //$NON-NLS-1$

	/** Идентификатор картинки 7-сегментного индикатора (размер 16x16) */
	public static final String IMG_LED_7_SEG_16 = "components/led7seg16.png"; //$NON-NLS-1$
	/** Идентификатор картинки 7-сегментного индикатора (размер 24x24) */
	public static final String IMG_LED_7_SEG_24 = "components/led7seg24.png"; //$NON-NLS-1$

	/** Идентификатор картинки 7-сегментного индикатора (размер 16x16) */
	public static final String IMG_LED_MATRIX_16 = "components/ledmatrix16.png"; //$NON-NLS-1$
	/** Идентификатор картинки 7-сегментного индикатора (размер 24x24) */
	public static final String IMG_LED_MATRIX_24 = "components/ledmatrix24.png"; //$NON-NLS-1$

	/** Идентификатор картинки АЦП (размер 16x16) */
	public static final String IMG_ADC_16 = "components/adc16.png"; //$NON-NLS-1$
	/** Идентификатор картинки АЦП (размер 24x24) */
	public static final String IMG_ADC_24 = "components/adc24.png"; //$NON-NLS-1$

	/** Идентификатор картинки НЕ (размер 16x16) */
	public static final String IMG_NOT_16 = "components/not16.png"; //$NON-NLS-1$
	/** Идентификатор картинки НЕ (размер 24x24) */
	public static final String IMG_NOT_24 = "components/not24.png"; //$NON-NLS-1$

	/** Идентификатор картинки 3И-НЕ (размер 16x16) */
	public static final String IMG_AND_3_NOT_16 = "components/and3not16.png"; //$NON-NLS-1$
	/** Идентификатор картинки 3И-НЕ (размер 24x24) */
	public static final String IMG_AND_3_NOT_24 = "components/and3not24.png"; //$NON-NLS-1$

	/** Картинки диода на лицевой панели */
	public static final String IMG_FACE_RED_LED = "face/led_red.png"; //$NON-NLS-1$
	public static final String IMG_FACE_GRAY_LED = "face/led_gray.png"; //$NON-NLS-1$
	
	public static final String IMG_SAVE_AS_IMAGE = "actions/save_as_image.gif"; //$NON-NLS-1$
	
	private String iconsPath;
	
	public BSPImages() {
		iconsPath = BSP.getFilePathFromPlugin("res/images"); //$NON-NLS-1$
		imageRegistry = new ImageRegistry();
	}
	
	/** Кеш картинок */
	ImageRegistry imageRegistry;

	protected void loadImage(String symbolicName) {
		ImageDescriptor descriptior = ImageDescriptor.createFromFile(null,
				iconsPath + "/" + symbolicName); //$NON-NLS-1$
		imageRegistry.put(symbolicName, descriptior);
	}
	
	@Override
	public Image getImage(String symbolicName) {
		Image result = imageRegistry.get(symbolicName);
		if (result == null) {
			loadImage(symbolicName);
			result = imageRegistry.get(symbolicName);
		}
		return result;
	}

	@Override
	public ImageDescriptor getImageDescriptor(String symbolicName) {
		ImageDescriptor result = imageRegistry.getDescriptor(symbolicName);
		if (result == null) {
			loadImage(symbolicName);
			result = imageRegistry.getDescriptor(symbolicName);
		}
		return result;
	}

}
