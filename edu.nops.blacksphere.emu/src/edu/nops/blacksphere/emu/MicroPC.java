package edu.nops.blacksphere.emu;

import java.io.IOException;
import java.io.InputStream;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.emu.i8086.LogicalAddressSpace;
import edu.nops.blacksphere.emu.i8086.PhysicalAddressSpace;
import edu.nops.blacksphere.emu.i8086.Processor;

/**
 * Класс управляющий процессом эмуляциеи
 * @author nops
 */
public class MicroPC {

	private Device device;
	private InputStream firmware;
	private Processor processor;
	private LogicalAddressSpace logicMemory;

	public MicroPC(Device device, InputStream firmware) {
		this.device = device;
		this.firmware = firmware;
		init();
	}
	
	public void init() {
		logicMemory = new LogicalAddressSpace(new PhysicalAddressSpace(4, 4));
		// загрузка прошивки устройства
		try {
			firmware.skip(4*1024);
			int offset = 0;
			while (firmware.available() > 0) {
				int data = firmware.read();
				if (data != 0) {
					logicMemory.setByte(0x100, offset, data);
				}
				offset++;
			}
			firmware.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		processor = new Processor(device, logicMemory);
		processor.start();
	}
	
}
