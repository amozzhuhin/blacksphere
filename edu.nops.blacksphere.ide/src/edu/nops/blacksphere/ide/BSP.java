package edu.nops.blacksphere.ide;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.osgi.framework.BundleContext;

import edu.nops.blacksphere.core.utils.OutputConsole;

/**
 * Активатор управляющий жизненным циклом плагина
 * и глобальные константы плагина
 */
public class BSP extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "edu.nops.blacksphere"; //$NON-NLS-1$

	// The shared instance
	private static BSP plugin;
	
	/**
	 * The constructor
	 */
	public BSP() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static BSP getDefault() {
		return plugin;
	}
	
	/**
	 * Возвратить абсолютный путь начиная от директории плагина.
	 * @param entrie путь (можно испоьзоваь разделитель '/')
	 * @return Путь внутри плагина или null
	 */
	public static String getFilePathFromPlugin(String entrie) {
		URL url = null;
	    IPath path = null;
	    String result = null;
	    String[] sections = entrie.split("/"); //$NON-NLS-1$
	    Enumeration enu = BSP.getDefault().getBundle()
	    	.findEntries("/", sections[0], true); //$NON-NLS-1$
	    if (enu.hasMoreElements())
	    	url = (URL) enu.nextElement();
	    if (url == null)
	    	return null;
	    try {
	    	path = new Path(FileLocator.toFileURL(url).getPath());
	    	for (int i = 1; i < sections.length; i++)
	    		path = path.append(sections[i]);
	    	result = path.makeAbsolute().toOSString();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return result;
	  }

	public static ImageDescriptor getImageDescriptor(String path) {
		return ImageDescriptor.createFromFile(null,
				getFilePathFromPlugin("icons") + path); //$NON-NLS-1$
	}
	
	/** Режим отладки плагина */
	public static final boolean DEBUG = true;
	
	/** ID перспективы дезайна устройства */
	public static final String DESIGN_PERSPECTIVE_ID =
			"edu.nops.blacksphere.perspective.DesignPerspective"; //$NON-NLS-1$

	/** ID построителя проектов */
	public static final String BUILDER_ID =
			"edu.nops.blacksphere.builder.ProjectBuilder"; //$NON-NLS-1$
	
	/**	Описание проекта */
	public static final String NATURE_ID =
			"edu.nops.blacksphere.builder.ProjectNature"; //$NON-NLS-1$

	public static final String NEW_PROJECT_WIZARD_ID =
		"edu.nops.blacksphere.wizards.NewProject"; //$NON-NLS-1$
	
	public static final String NEW_INCLUDE_FILE_WIZARD_ID =
		"edu.nops.blacksphere.wizards.NewInclideFile"; //$NON-NLS-1$

	public static final String  EMULATOR_APPLICATION_ID =
		"edu.nops.blacksphere.emu.BlackSphereEmulator"; //$NON-NLS-1$
	
	/** Имя каталога и исходными файлами */
	public static final String SOURCE_FOLDER_NAME = "src"; //$NON-NLS-1$

	/** Имя каталога и исходными файлами */
	public static final String INCLUDE_FOLDER_NAME = "include"; //$NON-NLS-1$

	/** Имя каталога и исходными файлами */
	public static final String BINARY_FOLDER_NAME = "bin"; //$NON-NLS-1$

	/** Имя главного ассемблерного файла */
	public static final String FIRMWARE_SOURCE_FILE_NAME = "firmware.asm"; //$NON-NLS-1$

	/** Имя листинга проекта */
	public static final String FIRMWARE_LST_FILE_NAME = "firmware.lst"; //$NON-NLS-1$

	/** Имя получаемого бинарного файла */
	public static final String FIRMWARE_BINARY_FILE_NAME = "firmware"; //$NON-NLS-1$

	/** Имя файла описания устройства */
	public static final String DEVICE_FILE_NAME = "device.bsx"; //$NON-NLS-1$

	/** Имя включаемого файла с константами архитектуры устройства */
	public static final String DEVICE_CONSTS_FILE_NAME = "device.inc"; //$NON-NLS-1$

	/** Идентификатор маркера проблем */
	public static final String PROBLEM_MARKER_ID =
			"edu.nops.blacksphere.problemmarker"; //$NON-NLS-1$
	
	/**	Шрифт используемый в схемах */
	public static final Font schemeFont =
			new Font(Display.getDefault(), "Arial", 10, SWT.ITALIC); //$NON-NLS-1$

	/** Пикселей на миллиметр */
	public static final float DPM = 3.54f; 

	/** Высота/ширина сетки */
	public static final int GRID_SPACING = (int) (5*DPM); 
	
	public static final String BUILD_CONSOLE_ID = BSPMessages.getString("BSP.BUILD_CONSOLE_ID"); //$NON-NLS-1$

	public static final String COMMENT_CONTENT_TYPE =
		"edu.nops.blacksphere.editprs.asm.comment";  //$NON-NLS-1$

	/**
	 * Получить файл из плагина
	 * @param path путь, например, /res/intel8088/instruction_set.xml
	 * @return файл
	 */
	public static File getFile(String path) {
		URL url = BSP.getDefault().getBundle().getEntry(path);
		File file = null;
		if (url != null) {
			try {
				file = new File(FileLocator.toFileURL(url).toURI());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	/**	Получить консоли */
	public static OutputConsole getConsoles() {
		return OutputConsole.getInstance();
	}
	
	private static BSPImages images;
	
	public static ISharedImages getImages() {
		if (images == null) {
			images = new BSPImages();
		}
		return images;
	}

}
