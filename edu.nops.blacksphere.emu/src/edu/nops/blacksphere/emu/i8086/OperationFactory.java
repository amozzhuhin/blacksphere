package edu.nops.blacksphere.emu.i8086;

import edu.nops.blacksphere.emu.EmuMessages;

/**
 * Фабрика операций. Основной метод createOperation создаёт
 * операцию по описанию 
 * @author nops
 */
public class OperationFactory {

	private Processor proc;
	
	public OperationFactory(Processor proc) {
		this.proc = proc;
	}
	
	/** Флаги префиксов команд */
    public static final int PREFICES_SG = 0x7;
    public static final int PREFICES_ES = 0x1;
    public static final int PREFICES_CS = 0x2;
    public static final int PREFICES_SS = 0x3;
    public static final int PREFICES_DS = 0x4;
    public static final int PREFICES_FS = 0x5;
    public static final int PREFICES_GS = 0x6;
    public static final int PREFICES_OPERAND = 0x8;
    public static final int PREFICES_ADDRESS = 0x10;
    public static final int PREFICES_REPNE = 0x20;
    public static final int PREFICES_REPE = 0x40;
    public static final int PREFICES_REP = PREFICES_REPNE | PREFICES_REPE;
    public static final int PREFICES_LOCK = 0x80;
    
	/** Константы сегментных регистров в ModR/M */
	public static final int SREG_ES = 0;
	public static final int SREG_CS = 1;
	public static final int SREG_SS = 2;
	public static final int SREG_DS = 3;
	//public static final int SREG_FS = 4;
	//public static final int SREG_GS = 5;
	
	public static final int RM_AX = 0;
	public static final int RM_CX = 1;
	public static final int RM_DX = 2;
	public static final int RM_BX = 3;
	public static final int RM_SP = 4;
	public static final int RM_BP = 5;
	public static final int RM_SI = 6;
	public static final int RM_DI = 7;
		
	/** Получить идентификатор сегментного регистра по sreg в ModR/M */
	public int getSReg(Instruction instruction) {
		switch ((instruction.getModrm() & 0x38) >> 3) {
		case SREG_ES:
			return Operation.ES;
		case SREG_CS:
			return Operation.CS;
		case SREG_SS:
			return Operation.SS;
		case SREG_DS:
			return Operation.DS;
		default:
			throw new AssertionError("Not register in R/M field"); //$NON-NLS-1$
		}
	}
	
	/** Получить регистр по полю rm в ModR/M */
	public int getRMReg16(Instruction instruction) {
		if (instruction.getModrm() >> 6 == 3) {
			switch (instruction.getModrm() & 07) {
			case RM_AX:
				return Operation.AX; 
			case RM_BX:
				return Operation.BX; 
			case RM_CX:
				return Operation.CX; 
			case RM_DX:
				return Operation.DX; 
			case RM_DI:
				return Operation.DI; 
			case RM_SI:
				return Operation.SI; 
			case RM_BP:
				return Operation.BP; 
			case RM_SP:
				return Operation.SP; 
			}
		}
		throw new AssertionError("Not register in R/M field"); //$NON-NLS-1$
	}
	
	/** Получить регистр по полю rm в ModR/M */
	public int getRMReg8(Instruction instruction) {
		if (instruction.getModrm() >> 6 == 3) {
			switch (instruction.getModrm() & 07) {
			case 0:
				return Operation.AL; 
			case 1:
				return Operation.CL; 
			case 2:
				return Operation.DL; 
			case 3:
				return Operation.BL; 
			case 4:
				return Operation.AH; 
			case 5:
				return Operation.CH; 
			case 6:
				return Operation.DH; 
			case 7:
				return Operation.BH; 
			}
		}
		throw new AssertionError("Not register in R/M field"); //$NON-NLS-1$
	}
	
	private int getRMMemBase(Instruction instruction) {
		switch (instruction.getModrm() & 7) {
		case 0:
		case 1:
		case 7:
			return Operation.BX;
		case 6:
			if ((instruction.getModrm() & 0xc0) == 0)
				return Operation.REG_NONE;
		case 2:
		case 3:
			return Operation.BP;
		}
		return Operation.REG_NONE;
	}
	
	private int getRMMemIndex(Instruction instruction) {
		switch (instruction.getModrm() & 7) {
		case 0:
		case 2:
		case 4:
			return Operation.SI;
		case 1:
		case 3:
		case 5:
			return Operation.DI;
		}
		return Operation.REG_NONE;
	}

	private int getModReg8(Instruction instruction) {
		switch ((instruction.getModrm() >> 3) & 07) {
		case 0:
			return Operation.AL;
		case 1:
			return Operation.CL;
		case 2:
			return Operation.DL;
		case 3:
			return Operation.BL;
		case 4:
			return Operation.AH;
		case 5:
			return Operation.CH;
		case 6:
			return Operation.DH;
		case 7:
			return Operation.BH;
		}
		return Operation.REG_NONE;
	}
	
	private boolean isRMMem(Instruction instruction) {
		return (instruction.getModrm() & 0xc0) != 0xc0;
	}
	
    public Operation createOperation(Instruction instruction) {
    	Operation op = null;
		switch (instruction.getOpcode()) {
		case 0xea: //jmp far
			op = new JumpOperation(false, false, instruction.getImmediate());
			break;
		case 0xeb: //jmp near (byte) relative 
			instruction.setImmediate(instruction.getImmediate() | 0xffffff00);
			op = new JumpOperation(true, true, instruction.getImmediate());
			break;
		case 0xe9: //jmp near (word) relative
			instruction.setImmediate(instruction.getImmediate() | 0xffff0000);
			op = new JumpOperation(true, true, instruction.getImmediate());
			break;
		case 0xb8: //mov ax,imm
			op = new MoveI2ROperation(Operation.AX, (int) instruction.getImmediate());
			break;
		case 0xbc: //mov sp,imm
			op = new MoveI2ROperation(Operation.SP, (int) instruction.getImmediate());
			break;
		case 0x8e: //mov Sreg,r16
			op = new MoveR2ROperation(getSReg(instruction), getRMReg16(instruction));
			break;
		case 0xc6: //mov r/m8, imm8
			if (isRMMem(instruction))
				op = new MoveI2MOperation(getRMMemBase(instruction),
						getRMMemIndex(instruction),
						instruction.getDisplacement(),
						(int) instruction.getImmediate() & 0xff);
			break;
		case 0x88: //mov r/m8, r8
			if (isRMMem(instruction))
				op = new MoveR2M8Operation(getModReg8(instruction),
						getRMMemBase(instruction),
						getRMMemIndex(instruction),
						instruction.getDisplacement());
			else
				op = new MoveR2ROperation(getRMReg8(instruction), getModReg8(instruction));
			break;
		case 0x8a: // mov r8,r/m8
			if (isRMMem(instruction))
				op = new MoveM82ROperation(getRMMemBase(instruction),
					getRMMemIndex(instruction),
					instruction.getDisplacement(),
					getModReg8(instruction));
			break;
		case 0xa0: //mov AL,moffs8*
			op = new MoveM82ROperation(
					Operation.REG_NONE,
					Operation.REG_NONE,
					instruction.getDisplacement(),Operation.AL);
			break;
		case 0xa2: //mov moffs8*,AL
			op = new MoveR2M8Operation(Operation.AL,
					Operation.REG_NONE,
					Operation.REG_NONE,
					instruction.getDisplacement());
			break;
		case 0xb0: // mov al,imm8
			op = new MoveI2ROperation(Operation.AL, (byte) instruction.getImmediate());
			break;
		case 0xb1: // mov cl,imm8
			op = new MoveI2ROperation(Operation.CL, (byte) instruction.getImmediate());
			break;
		case 0xb9:
			op = new MoveI2ROperation(Operation.CX, (short) instruction.getImmediate());
			break;
		case 0xbb: // mov r16,imm16
			op = new MoveI2ROperation(Operation.BX, (short) instruction.getImmediate());
			break;
		case 0xe4: //in al,imm
			op = new InOperation((int) instruction.getImmediate());
			break;
		case 0xe6: //out imm,al
			op = new OutOperation((int) instruction.getImmediate());
			break;
		case 0xe8: //call near (word) relative
			op = new CallOperation(true, true, (int) instruction.getImmediate() & 0xffff);
			break;
		case 0xc3: // ret near 0
			op = new RetOperation(true, 0);
			break;
		case 0x30: // xor r/m8, r8
			if (isRMMem(instruction))
				op = new XorR2MOperation(getRMMemBase(instruction),
						getRMMemIndex(instruction),
						instruction.getDisplacement(), getModReg8(instruction));
			else
				op = new XorR2ROperation(getRMReg8(instruction), getModReg8(instruction));
			break;
		case 0x20: // and r/m8, r8
			if (isRMMem(instruction))
				op = new AndR2M8Operation(getRMMemBase(instruction),
						getRMMemIndex(instruction),
						instruction.getDisplacement(), getModReg8(instruction));
			else
				op = new AndR2ROperation(getModReg8(instruction), getRMReg8(instruction));
			break;
		case 0x24: // and al,imm
			op = new AndI2ROperation(Operation.AL, (byte)instruction.getImmediate());
			break;
		case 0xa8: // test al, imm8
			op = new TestRegImmOperation(Operation.AL, (int) instruction.getImmediate());
			break;
		case 0x74: // je/jz imm8
			op = new JumpByFlagOperation(Operation.FLAG_Z, true, (byte) instruction.getImmediate());
			break;
		case 0x75: // jne/jnz imm8
			op = new JumpByFlagOperation(Operation.FLAG_Z, false, (byte) instruction.getImmediate());
			break;
		case 0xfe: // inc/dec rm8
			if ((instruction.getModrm() & 0x8) != 0) {
				if (isRMMem(instruction))
					op = new DecMem8Operation(getRMMemBase(instruction),
							getRMMemIndex(instruction),
							instruction.getDisplacement());
			} else {
				if (isRMMem(instruction))
					op = new IncMem8Operation(getRMMemBase(instruction),
							getRMMemIndex(instruction),
							instruction.getDisplacement());
			}
			break;			
		case 0xd7: // xlat
			op = new XlatOperation();
			break;
		case 0xd2: // shr r/m8, cl
			if ((instruction.getModrm() & 0x38) == 0x28)
				if (!isRMMem(instruction))
					op = new ShrRegByClOperation(getRMReg8(instruction));
			break;
		case 0x38: // cmp r/m8, r8
			if (isRMMem(instruction)) {}
			else
				op = new CmpR2ROperation(getModReg8(instruction), getRMReg8(instruction));
			break;
		case 0xe2: // loop rel8
			op = new LoopOperation((byte) instruction.getImmediate());
			break;
		}
		if (op != null)
			op.setProcessor(proc);
		return op;
	}
	
    /** Операция пересылки константы в регистр */
	private class MoveI2ROperation extends Operation {
		
		private int imm;
		private int reg;
		
		public MoveI2ROperation(int reg, int immediate) {
			this.reg = reg;
			this.imm = immediate;
		}
		
		@Override
		public void perform() {
			setRegister(reg, imm);
		}
		
		@Override
		public String toString() {
			return "mov " + getRegisterName(reg) + ",0x" + Long.toHexString(imm); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}
	
    /** Операция пересылки регистра в регистр */
	private class MoveR2ROperation extends Operation {
		
		private int src;
		private int dst;
		
		public MoveR2ROperation(int dst, int src) {
			this.dst = dst;
			this.src = src;
		}
		
		@Override
		public void perform() {
			setRegister(dst, getRegister(src));
		}

		@Override
		public String toString() {
			return "mov " + getRegisterName(dst) + "," + getRegisterName(src); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}
	
    /** Операция пересылки константы в память */
	private class MoveI2MOperation extends Operation {
		
		private int base;
		private int index;
		private int disp;
		private int imm;
		
		public MoveI2MOperation(int base, int index, int disp, int imm) {
			this.base = base;
			this.index = index;
			this.disp = disp;
			this.imm = imm;
		}
		
		@Override
		public void perform() {
			processor.getMemory().setByte(processor.ds,
					getBaseIndexOffset(base, index, disp), imm);
		}

		@Override
		public String toString() {
			return "mov " //$NON-NLS-1$
				+ BaseIndexOffsetToString(base, index, disp)
				+ "," + Integer.toHexString(imm); //$NON-NLS-1$
		}

	}
	
    /** Операция пересылки константы в память */
	private class MoveR2M8Operation extends Operation {
		
		private int base;
		private int index;
		private int disp;
		private int reg;
		
		public MoveR2M8Operation(int reg, int base, int index, int disp) {
			this.base = base;
			this.index = index;
			this.disp = disp;
			this.reg = reg;
		}
		
		@Override
		public void perform() {
			processor.getMemory().setByte(processor.ds,
					getBaseIndexOffset(base, index, disp),
					getRegister(reg));
		}

		@Override
		public String toString() {
			return "mov " //$NON-NLS-1$
				+ BaseIndexOffsetToString(base, index, disp)
				+ "," + getRegisterName(reg); //$NON-NLS-1$
		}

	}
	
    /** Операция пересылки константы в память */
	private class MoveM82ROperation extends Operation {
		
		private int base;
		private int index;
		private int disp;
		private int reg;
		
		public MoveM82ROperation(int base, int index, int disp, int reg) {
			this.base = base;
			this.index = index;
			this.disp = disp;
			this.reg = reg;
		}
		
		@Override
		public void perform() {
			setRegister(reg,
					processor.getMemory().getByte(processor.ds,
					getBaseIndexOffset(base, index, disp)));
		}

		@Override
		public String toString() {
			return "mov " //$NON-NLS-1$
				+ getRegisterName(reg)
				+ "," + BaseIndexOffsetToString(base, index, disp); //$NON-NLS-1$
		}

	}
	
	/** Операция передачи управления */
	private class JumpOperation extends Operation {
	
		private boolean near;
		private boolean relative;
		private long dest;
		
		public JumpOperation(boolean near, boolean relative, long dest) {
			this.near = near;
			this.relative = relative;
			this.dest = dest;
		}
		
		@Override
		public void perform() {
			if (near) {
				if (relative)
					processor.ip += (int) dest;
				else
					processor.ip = (int) dest;
			} else {
				processor.ip = (int) (dest & 0xffff);
				processor.cs = (int) ((dest >> 16) & 0xffff);
			}
			processor.ip &= 0xffff;
			processor.cs &= 0xffff;
		}

		@Override
		public String toString() {
			return "jmp " + (near ? "near" : "far") + (relative ? " rel" : "") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
				+ " 0x" + Long.toHexString(dest); //$NON-NLS-1$
		}
		
		
	}
	
    /** Операция ввода с порта */
	private class InOperation extends Operation {
		
		/** Номер порта */
		private int addr;
		
		public InOperation(int addr) {
			this.addr = addr;
		}
		
		@Override
		public void perform() {
			int val =  processor.getPortValue(addr);
			if (val != 0)
				new Integer(0);
			setRegister(Operation.AL, val);
		}
		
		@Override
		public String toString() {
			return "in al,0x" + Integer.toHexString(addr); //$NON-NLS-1$
		}

	}

	/** Операция вывода на порта */
	private class OutOperation extends Operation {
		
		/** Номер порта */
		private int addr;
		
		public OutOperation(int addr) {
			this.addr = addr;
		}
		
		@Override
		public void perform() {
			processor.setPortValue(addr, getRegister(Operation.AL));
		}
		
		@Override
		public String toString() {
			return "out 0x" + Integer.toHexString(addr) + ",al"; //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

	/** Операция вывода на порта */
	private class CallOperation extends Operation {
		
		private boolean near;
		private boolean relative;
		private long dest;
		
		public CallOperation(boolean near, boolean relative, long dest) {
			this.near = near;
			this.relative = relative;
			this.dest = dest;
		}
		
		@Override
		public void perform() {
			if (near) {
				processor.sp -= 2;
				processor.getMemory().setWord(processor.ss, processor.sp, processor.ip);
				if (relative)
					processor.ip += (int) dest;
/*				else
					processor.ip = (int) dest;
			} else {
				processor.ip = (int) (dest & 0xffff);
				processor.cs = (int) ((dest >> 16) & 0xffff);*/
			}
			processor.ip &= 0xffff;
			processor.cs &= 0xffff;
		}
		
		@Override
		public String toString() {
			return "call " + (near ? "near" : "far") + (relative ? " rel" : "") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			   +" 0x" + Long.toHexString(dest); //$NON-NLS-1$
		}

	}

	/** Операция вывода на порта */
	private class RetOperation extends Operation {
		
		private boolean near;
		private int args;
		
		public RetOperation(boolean near, int args) {
			this.near = near;
			this.args = args;
		}
		
		@Override
		public void perform() {
			if (near) {
				int dest = processor.getMemory().getWord(processor.ss, processor.sp);
				processor.sp += 2;
				processor.ip = dest;
			}
		}
		
		@Override
		public String toString() {
			return "ret " + Integer.toHexString(args); //$NON-NLS-1$
		}

	}
	
	/** Операция XOR регистра в регистр */
	private class XorR2ROperation extends Operation {
		
		private int src;
		private int dst;
		
		public XorR2ROperation(int dst, int src) {
			this.dst = dst;
			this.src = src;
		}
		
		@Override
		public void perform() {
			setRegister(dst, getRegister(dst) ^ getRegister(src));
		}

		@Override
		public String toString() {
			return "xor " + getRegisterName(dst) + "," + getRegisterName(src); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

	/** Операция XOR память-регистра */
	private class XorR2MOperation extends Operation {
		
		private int base;
		private int index;
		private int disp;
		private int reg;
		
		private int oldSrc, oldDst;
		
		public XorR2MOperation(int base, int index, int disp, int reg) {
			this.base = base;
			this.index = index;
			this.disp = disp;
			this.reg = reg;
		}
		
		@Override
		public void perform() {
			processor.getMemory().setByte(processor.ds,
					getBaseIndexOffset(base, index, disp),
					oldSrc ^ oldDst);
		}

		@Override
		public void prepareOperands() {
			oldSrc = processor.getMemory().getByte(processor.ds, getBaseIndexOffset(base, index, disp));
			oldDst = getRegister(reg);
			//System.out.print("xor " + oldSrc + "," + oldDst);
		}

		@Override
		public String toString() {
			return "xor " + BaseIndexOffsetToString(base, index, disp) //$NON-NLS-1$
				+ "," + getRegisterName(reg); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

	/** Операция AND память-регистра */
	private class AndR2M8Operation extends Operation {
		
		private int base;
		private int index;
		private int disp;
		private int reg;

		private int oldSrc, oldDst;
		
		public AndR2M8Operation(int base, int index, int disp, int reg) {
			this.base = base;
			this.index = index;
			this.disp = disp;
			this.reg = reg;
		}
		
		@Override
		public void perform() {
			processor.getMemory().setByte(processor.ds,
					getBaseIndexOffset(base, index, disp),
					oldSrc & oldSrc);
		}

		@Override
		public void prepareOperands() {
			oldSrc = processor.getMemory().getByte(processor.ds, getBaseIndexOffset(base, index, disp));
			oldDst = getRegister(reg);
			//System.out.print("and " + oldSrc + "," + oldDst + '\n');
		}

		@Override
		public String toString() {
			return "and " + BaseIndexOffsetToString(base, index, disp) //$NON-NLS-1$
				+ "," + getRegisterName(reg); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}
	
	/** Операция AND регистр-регистр */
	private class AndR2ROperation extends Operation {
		
		private int src, dst;

		private int oldSrc, oldDst;
		
		public AndR2ROperation(int src, int dst) {
			this.src = src;
			this.dst = dst;
		}
		
		@Override
		public void perform() {
			setRegister(dst, oldDst & oldSrc);
		}

		@Override
		public void prepareOperands() {
			oldSrc = getRegister(src);
			oldDst = getRegister(dst);
		}

		@Override
		public String toString() {
			return "and " + getRegisterName(dst) //$NON-NLS-1$
				+ "," + getRegisterName(src); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/** Операция AND константа-регистра */
	private class AndI2ROperation extends Operation {
		
		private int reg, imm;

		private int oldReg;
		
		public AndI2ROperation(int reg, int imm) {
			this.reg = reg;
			this.imm = imm;
		}
		
		@Override
		public void perform() {
			setRegister(reg, oldReg & imm);
		}

		@Override
		public void prepareOperands() {
			oldReg = getRegister(reg);
		}

		@Override
		public String toString() {
			return "and " + getRegisterName(reg) //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-1$
				+ "," + imm; //$NON-NLS-1$
		}
	}
	
	private class TestRegImmOperation extends Operation {
	
		private int reg;
		private int imm;
		
		private int lastRegValue;
		
		public TestRegImmOperation(int reg, int imm) {
			this.reg = reg;
			this.imm = imm;
		}

		@Override
		public int getLastFlags() {
			int flags = processor.flags;
			flags = setFlag(flags, FLAG_Z, (lastRegValue & imm) == 0);
			return flags;
		}

		@Override
		public boolean isFlagChanged() {
			return true;
		}

		@Override
		public void prepareOperands() {
			lastRegValue = getRegister(reg);
		}

		@Override
		public String toString() {
			return "test " + getRegisterName(reg) + "," + imm; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	private class JumpByFlagOperation extends Operation {
		
		private int flag;
		private boolean value;
		private int addr;
		
		public JumpByFlagOperation(int flag, boolean value, int addr) {
			this.flag = flag;
			this.value = value;
			this.addr = addr;
		}
		
		@Override
		public void perform() {
			if (getFlag(processor.getFlags(), flag) == value)
				processor.ip += addr;
		}
		
		@Override
		public String toString() {
			return "j" + (value ? "" : "n") + getFlagName(flag) + " " + Integer.toString(addr); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}

	}
	
	private class IncMem8Operation extends Operation {
		private int base;
		private int index;
		private int disp;
		private int lastValue;
		
		public IncMem8Operation(int base, int index, int disp) {
			this.base = base;
			this.index = index;
			this.disp = disp;
		}
		
		@Override
		public void perform() {
			processor.getMemory().setByte(processor.ds,
					getBaseIndexOffset(base, index, disp),
					(byte) (lastValue+1));
		}
		
		@Override
		public void prepareOperands() {
			lastValue = processor.getMemory().getByte(processor.ds,
					getBaseIndexOffset(base, index, disp));
		}

		@Override
		public int getLastFlags() {
			int flags = processor.flags;
			setFlag(flags, FLAG_Z, lastValue+1 == 0);
			return flags;
		}

		@Override
		public boolean isFlagChanged() {
			return true;
		}
		
		@Override
		public String toString() {
			return "inc " + BaseIndexOffsetToString(base, index, disp); //$NON-NLS-1$
		}

	}
	
	private class DecMem8Operation extends Operation {
		private int base;
		private int index;
		private int disp;
		private int lastValue;
		
		public DecMem8Operation(int base, int index, int disp) {
			this.base = base;
			this.index = index;
			this.disp = disp;
		}
		
		@Override
		public void perform() {
			processor.getMemory().setByte(processor.ds,
					getBaseIndexOffset(base, index, disp),
					(byte) (lastValue-1));
		}
		
		@Override
		public void prepareOperands() {
			lastValue = processor.getMemory().getByte(processor.ds,
					getBaseIndexOffset(base, index, disp));
		}

		@Override
		public int getLastFlags() {
			int flags = processor.flags;
			setFlag(flags, FLAG_Z, lastValue-1 == 0);
			return flags;
		}

		@Override
		public boolean isFlagChanged() {
			return true;
		}
		
		@Override
		public String toString() {
			return "dec " + BaseIndexOffsetToString(base, index, disp); //$NON-NLS-1$
		}
	}
	
	/** Операция XLAT */
	private class XlatOperation extends Operation {
		
		public XlatOperation() {}
		
		@Override
		public void perform() {
			int val = processor.getMemory().getByte(
					processor.ds,
					processor.bx + (processor.ax & 0xff));
			if (val != 0)
				new Integer(0);
			setRegister(Operation.AL, 
					val);
		}

		@Override
		public String toString() {
			return "xlat"; //$NON-NLS-1$
		}
	}
	
	
	/** Операция Shr по сl */
	private class ShrRegByClOperation extends Operation {
		
		private int reg;
		
		public ShrRegByClOperation(int reg) {
			this.reg = reg;
		}
		
		@Override
		public void perform() {
			setRegister(reg,
					getRegister(reg) >> getRegister(Operation.CL));
		}

		@Override
		public String toString() {
			return "shr " + getRegisterName(reg) + ",cl"; //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/** Операция cmp */
	private class CmpR2ROperation extends Operation {
		
		private int reg1,reg2;
		private int lastReg1, lastReg2;
		
		public CmpR2ROperation(int reg1, int reg2) {
			this.reg1 = reg1;
			this.reg2 = reg2;
		}
		
		@Override
		public void perform() {	}

		
		@Override
		public int getLastFlags() {
			int flags = proc.flags;
			flags = setFlag(flags, FLAG_Z, lastReg1 == lastReg2);
			return flags;
		}

		@Override
		public boolean isFlagChanged() {
			return true;
		}

		@Override
		public void prepareOperands() {
			lastReg1 = getRegister(reg1);
			lastReg2 = getRegister(reg2);
		}

		@Override
		public String toString() {
			return "cmp " + getRegisterName(reg1) + "," + getRegisterName(reg2); //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/** Операция loop */
	private class LoopOperation extends Operation {
		
		private int addr;
		
		public LoopOperation(int addr) {
			this.addr = addr;
		}
		
		@Override
		public void perform() {
			if (--proc.cx != 0) {
				proc.ip += addr;
				proc.ip &= 0xffff;
			}
		}

		
		@Override
		public String toString() {
			return "loop " + Integer.toHexString(addr); //$NON-NLS-1$
		}
	}

}
