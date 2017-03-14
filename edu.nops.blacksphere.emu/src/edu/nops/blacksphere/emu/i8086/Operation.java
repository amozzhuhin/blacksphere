package edu.nops.blacksphere.emu.i8086;

/**
 * Предок всех микроопераций   
 * @author nops
 */
public class Operation {

	/** Для увеличения производительности видимость для потомков */
	protected Processor processor;
	
	public void setProcessor(Processor proc) {
		processor = proc;
	}

	/** Получить процессор на котором выполняется операция */
	public Processor getProcessor() {
		return processor;
	}
	
	/** Подготовить операнды для операции */
	public void prepareOperands() {	}

	/** Выполненние действия операции */
	public void perform() { }

	/** Воздействие ли операция на флаги */
	public boolean isFlagChanged() {
		return false;
	}
	
	/**
	 * Подсчёт регистра флагов для последней
	 * выполеннной операции (отложенный расчёт флагов).
	 */
	public int getLastFlags() {
		return 0;
	}
	
	public static final int REG_NONE = -1;
	/** Константы регистров */
	public static final int AX = 0;
	public static final int BX = 1;
	public static final int CX = 2;
	public static final int DX = 3;

	public static final int AL = 4;
	public static final int BL = 5;
	public static final int CL = 6;
	public static final int DL = 7;

	public static final int AH = 8;
	public static final int BH = 9;
	public static final int CH = 10;
	public static final int DH = 11;

	public static final int SI = 12;
	public static final int DI = 13;
	public static final int BP = 14;
	public static final int SP = 15;

	public static final int CS = 16;
	public static final int DS = 17;
	public static final int ES = 18;
	public static final int SS = 19;

	private static String[] regNames = new String[] {
		"ax", "bx", "cx", "dx", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		"al", "bl", "cl", "dl", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		"ah", "bh", "ch", "dh", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		"si", "di", "bp", "sp", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		"cs", "ds", "es", "ss", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}; 
	
	/** Установить значение регистра */
	protected void setRegister(int regId, int value) {
		switch (regId) {
		case AX:
			processor.ax = value & 0xffff;
			break;
		case BX:
			processor.bx = value & 0xffff;
			break;
		case CX:
			processor.cx = value & 0xffff;
			break;
		case DX:
			processor.dx = value & 0xffff;
			break;
		case AL:
			processor.ax &= 0xff00;
			processor.ax |= value & 0xff;
			break;
		case BL:
			processor.bx &= 0xff00;
			processor.bx |= value & 0xff;
			break;
		case CL:
			processor.cx &= 0xff00;
			processor.cx |= value & 0xff;
			break;
		case DL:
			processor.dx &= 0xff00;
			processor.dx |= value & 0xff;
			break;
		case AH:
			processor.ax &= 0x00ff;
			processor.ax |= (value & 0xff) << 8;
			break;
		case BH:
			processor.bx &= 0x00ff;
			processor.bx |= (value & 0xff) << 8;
			break;
		case CH:
			processor.cx &= 0x00ff;
			processor.cx |= (value & 0xff) << 8;
			break;
		case DH:
			processor.dx &= 0x00ff;
			processor.dx |= (value & 0xff) << 8;
			break;
		case SI:
			processor.si = value & 0xffff;
			break;
		case DI:
			processor.di = value & 0xffff;
			break;
		case BP:
			processor.bp = value & 0xffff;
			break;
		case SP:
			processor.sp = value & 0xffff;
			break;
		case CS:
			processor.cs = value & 0xffff;
			break;
		case DS:
			processor.ds = value & 0xffff;
			break;
		case ES:
			processor.es = value & 0xffff;
			break;
		case SS:
			processor.ss = value & 0xffff;
			break;
		default:
			assert false: "Bad regId"; //$NON-NLS-1$
		}
	}

	/** Получить значение регистра */
	protected int getRegister(int regId) {
		switch (regId) {
		case AX:
			return processor.ax;
		case BX:
			return processor.bx;
		case CX:
			return processor.cx;
		case DX:
			return processor.dx;
		case AL:
			return processor.ax & 0xff;
		case BL:
			return processor.bx & 0xff;
		case CL:
			return processor.cx & 0xff;
		case DL:
			return processor.dx & 0xff;
		case AH:
			return processor.ax >> 8;
		case BH:
			return processor.bx >> 8;
		case CH:
			return processor.cx >> 8;
		case DH:
			return processor.dx >> 8;
		case SI:
			return processor.si;
		case DI:
			return processor.di;
		case BP:
			return processor.bp;
		case SP:
			return processor.sp;
		case CS:
			return processor.cs;
		case DS:
			return processor.ds;
		case ES:
			return processor.es;
		case SS:
			return processor.ss;
		default:
			assert false : "Bad regId"; //$NON-NLS-1$
			return 0;
		}
	}
	
	
	public static String getRegisterName(int regId) {
		assert (regId >= 0) | (regId < regNames.length) : "Bad regId"; //$NON-NLS-1$
		return regNames[regId];
	}
	
	public static String getFlagName(int flag) {
		switch (flag) {
		case FLAG_Z:
			return "z"; //$NON-NLS-1$
		default:
			assert false : "Bad flag"; //$NON-NLS-1$
			return ""; //$NON-NLS-1$
		}
	}
	
	/** Поличить смещение в базово-индексной адресации */
	protected int getBaseIndexOffset(int base, int index, int disp) {
		int res = disp;
		if (base != Operation.REG_NONE)
			res += getRegister(base);
		if (index != Operation.REG_NONE)
			res += getRegister(index);
		return res;
	}
	
	protected static String BaseIndexOffsetToString(int base, int index, int disp) {
		return ((base != Operation.REG_NONE) ? "[" + getRegisterName(base)  + "]" : "") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ ((index != Operation.REG_NONE) ? "[" +  getRegisterName(base) + "]" : "") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ "[" +Integer.toHexString(disp)+ "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	protected static final int FLAG_Z = 0x40;
	
	/** Установить значение флага Z */
	protected static int setFlag(int flags, int flag, boolean value) {
		if (value)
			return flags | flag;
		else
			return flags & ~flag;
			
	}
	
	protected static boolean getFlag(int flags, int flag) {
		return (flags & flag) != 0;
	}
}
