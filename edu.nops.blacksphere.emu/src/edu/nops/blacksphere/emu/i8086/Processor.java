package edu.nops.blacksphere.emu.i8086;

import java.util.Hashtable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.core.device.elements.AbstractElement;
import edu.nops.blacksphere.core.device.elements.InPortElement;
import edu.nops.blacksphere.core.device.elements.OutPortElement;
import edu.nops.blacksphere.core.utils.OutputConsole;
import edu.nops.blacksphere.emu.BSPE;
import edu.nops.blacksphere.emu.EmuMessages;

/**
 * Процессор intel8088/8086
 * @author nops
 */
public class Processor {

	private Device device;
	
	/** Регистры */
	public int ax,bx,cx,dx;
	public int si,di,bp,sp;
	public int cs,ds,es,ss;
	public int ip;
	public int flags;
	
	private InstructionFetcher instructionFetcher;
	private ScheduledThreadPoolExecutor stepExecutor;
	private StepThread stepThread;
	/** Последняя инструкция изменившая флаги или null */
	private Instruction lastFlagInstruction;
	
	private LogicalAddressSpace memory;
	
	/** Таблица портов ввода для быстрого доступа */
	private Hashtable<Integer, InPortElement> inPorts;
	private Hashtable<Integer, OutPortElement> outPorts;
	
	public Processor(Device device, LogicalAddressSpace memory) {
		this.device = device;
		this.memory = memory;
		// Создание таблиц "номер порта" -> "порт"
		inPorts = new Hashtable<Integer, InPortElement>();
		outPorts = new Hashtable<Integer, OutPortElement>();
		for (AbstractElement el : device.getElements()) {
			if (el instanceof InPortElement)
				inPorts.put(((InPortElement)el).getAddress(), (InPortElement)el);
			else if (el instanceof OutPortElement)
				outPorts.put(((OutPortElement)el).getAddress(), (OutPortElement)el);
		}
		instructionFetcher = new InstructionFetcher(this);
		stepThread = new StepThread();
		stepExecutor = new ScheduledThreadPoolExecutor(1);
	}

	public LogicalAddressSpace getMemory() {
		return memory;
	}
	
	/** Поток выполнения одной инструкции */
	private class StepThread extends Thread {
		@Override
		public void run() {
			step();
		}
	}
	
	/** Выполнить перезапуск процессора */
	public void reset() {
		stop();
		start();
	}
	
	/** Среднее количество тактов выполнения одной команды */
	protected static final int AVERAGE_OPERATION_TIME = 5;
	
	/** Запуск процессора */
	public void start() {
		ax = bx = cx = dx = 0;
		si = di = bp = sp = 0;
		ds = es = ss = 0;
		cs = 0xFFFF; ip = 0;
		lastFlagInstruction = null;
		device.refresh();
		stepExecutor.scheduleAtFixedRate(stepThread, (int) 10e5,
				(int) (AVERAGE_OPERATION_TIME*10e3 / device.getFrequency()),
				TimeUnit.MICROSECONDS);
		OutputConsole.getInstance().print(BSPE.EMU_CONSOLE_ID,
				EmuMessages.getString("Processor.processorStarted")); //$NON-NLS-1$
	}
	
	/** Остановка процессора */
	public void stop() {
		stepExecutor.shutdown();
		OutputConsole.getInstance().print(BSPE.EMU_CONSOLE_ID,
				EmuMessages.getString("Processor.processorStoped")); //$NON-NLS-1$
	}
	
	/** Получить значение порта по адресу */
	public int getPortValue(int address) {
		InPortElement port = inPorts.get(address);
		if (port != null) {
			//device.refresh();
			return port.getInputData();
		} else {
			OutputConsole.getInstance().print(BSPE.EMU_CONSOLE_ID,
					EmuMessages.getString("Processor.accessToUnusedInPort") //$NON-NLS-1$
					+ Integer.toHexString(address) + ")\n"); //$NON-NLS-1$
			return 0;
		}
	}

	/** Установить значение порта */
	public void setPortValue(int address, int value) {
		OutPortElement port = outPorts.get(address);
		if (port != null) {
			port.setOutputData(value);
			device.refresh();
		} else {
			OutputConsole.getInstance().print(BSPE.EMU_CONSOLE_ID,
					EmuMessages.getString("Processor.accessToUnusedOutPort") //$NON-NLS-1$
					+ Integer.toHexString(address) + ")\n"); //$NON-NLS-1$
		}
	}

	/** Выполнить текущую инструкцию */
	protected void step() {
		try {
			Instruction instruction = instructionFetcher.fetch(cs, ip);
			Operation op = instruction.getOperation();
			if (op != null) {
				/*OutputConsole.getInstance().print(BSPE.EMU_CONSOLE_ID,
						"Выполнение: " + op + "\n");*/
				ip += instruction.getSize();
				op.prepareOperands();
				op.perform();
				if (op.isFlagChanged())
					lastFlagInstruction = instruction;
				/*OutputConsole.getInstance().print(BSPE.EMU_CONSOLE_ID,
						"Состояние процессора: " + this + "\n");*/
			} else {
				OutputConsole.getInstance().print(BSPE.EMU_CONSOLE_ID,
						EmuMessages.getString("Processor.unknownInstruction") //$NON-NLS-1$
						+ Integer.toHexString(cs)  + ":" + Integer.toHexString(ip) //$NON-NLS-1$
						+ EmuMessages.getString("Processor.operationCode") //$NON-NLS-1$
						+ Integer.toHexString(instruction.getOpcode()) + ")\n"); //$NON-NLS-1$
				stop();
			}
		} catch(Exception e) {
			OutputConsole.getInstance().print(BSPE.EMU_CONSOLE_ID,
					EmuMessages.getString("Processor.processorError") + e.getMessage() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
			e.printStackTrace();
			stop();
		}
	}
	
	public int getFlags() {
		if (lastFlagInstruction != null) {
			flags = lastFlagInstruction.getOperation().getLastFlags();
			lastFlagInstruction = null;
		}
		return flags;
	}

	@Override
	public String toString() {
		return 
		  "ax=" + Integer.toHexString(ax) //$NON-NLS-1$
		  + " bx=" + Integer.toHexString(bx) //$NON-NLS-1$
		  + " cx=" + Integer.toHexString(cx) //$NON-NLS-1$
		  + " dx=" + Integer.toHexString(dx) //$NON-NLS-1$
		  + " si=" + Integer.toHexString(si) //$NON-NLS-1$
		  + " di=" + Integer.toHexString(di) //$NON-NLS-1$
		  + " bp=" + Integer.toHexString(bp) //$NON-NLS-1$
		  + " sp=" + Integer.toHexString(sp) //$NON-NLS-1$
		  + " cs=" + Integer.toHexString(cs) //$NON-NLS-1$
		  + " ds=" + Integer.toHexString(ds) //$NON-NLS-1$
		  + " es=" + Integer.toHexString(es) //$NON-NLS-1$
		  + " ss=" + Integer.toHexString(ss) //$NON-NLS-1$
		  + " ip=" + Integer.toHexString(ip) //$NON-NLS-1$
		;
	}
	
	
}
