package edu.nops.blacksphere.core.device;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;

import edu.nops.blacksphere.core.CoreMessages;
import edu.nops.blacksphere.core.device.elements.*;

/**
 * Сохранение и загрузка модели устройства
 * @author nops
 */
public class DeviceTransform {

	private static XStream xstream;
	
	/** Настройка общих параметров XStream */
	protected static XStream getXStream() {
		if (xstream == null) {
			xstream = new XStream();
			xstream.autodetectAnnotations(true);
			// все аннотации класса устройтсва необходимо прочитать заранее
			// иначе могут быть проблемы
			xstream.processAnnotations(Device.class);
		}
		
		return xstream;
	}
	
	/**
	 * Получить описание устройства из потока
	 * @param is поток ввода
	 * @return описание устройства
	 */
	public static Device loadDevice(Reader reader) {
		return (Device) getXStream().fromXML(reader);
	}
	
	/**
	 * Сохранить устройство в поток
	 * @param device описание устройства
	 * @param os потов вывода
	 */
	public static void saveDevice(Device device, Writer writer) {
		getXStream().toXML(device, writer);
	}
	
	/**
	 * Сохранить константы устройства в поток
	 * @param device описание устройства
	 * @param os потов вывода
	 */
	public static void saveDeviceConsts(Device device, OutputStream os) {
		OutputStreamWriter writer = new OutputStreamWriter(os);
		try {
			writer.write(
				CoreMessages.getString("DeviceTransform.deviceIncludeHeader") + "\n\n" //$NON-NLS-1$ //$NON-NLS-2$
			);
			writer.write(CoreMessages.getString("DeviceTransform.romSizeComment") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write("ROM_SIZE equ " + device.getROM() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(CoreMessages.getString("DeviceTransform.ramSizeComment") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write("RAM_SIZE equ " + device.getRAM() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write(CoreMessages.getString("DeviceTransform.firmwareSizeComment") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write("FIRMWARE_SIZE equ ROM_SIZE + RAM_SIZE\n"); //$NON-NLS-1$
			writer.write(CoreMessages.getString("DeviceTransform.bootStartComment") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.write("BOOT_START equ FIRMWARE_SIZE - 16\n\n"); //$NON-NLS-1$
			writer.write(CoreMessages.getString("DeviceTransform.addressInPortsComment") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			for(AbstractElement el : device.getElements()) {
				if (el instanceof InPortElement)
					writer.write(el.getName() + "_ADDR equ " //$NON-NLS-1$
							+ ((InPortElement)el).getAddress() + "\n"); //$NON-NLS-1$
			}
			writer.write("\n"); //$NON-NLS-1$
			writer.write(CoreMessages.getString("DeviceTransform.addressOutPortsComment") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			for(AbstractElement el : device.getElements()) {
				if (el instanceof OutPortElement)
					writer.write(el.getName() + "_ADDR equ " //$NON-NLS-1$
							+ ((OutPortElement)el).getAddress() + "\n"); //$NON-NLS-1$
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
