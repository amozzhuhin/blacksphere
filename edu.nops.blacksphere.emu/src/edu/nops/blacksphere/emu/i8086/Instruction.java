package edu.nops.blacksphere.emu.i8086;

/**
 * Описание инструкции процессора
 * @author nops
 */
public class Instruction {
	/** Поля при разборе инструкции */
	private int prefices;
	private int opcode;
	private int modrm;
	private int sib;
	private int displacement;
	private long immediate;
	/** Размер инструкции в байтах */
	private int size;
	/** Операция выполняемая командой */
	private Operation operation;
	
	public int getDisplacement() {
		return displacement;
	}
	public void setDisplacement(int displacement) {
		this.displacement = displacement;
	}
	public long getImmediate() {
		return immediate;
	}
	public void setImmediate(long immediate) {
		this.immediate = immediate;
	}
	public int getModrm() {
		return modrm;
	}
	public void setModrm(int modrm) {
		this.modrm = modrm;
	}
	public int getOpcode() {
		return opcode;
	}
	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}
	public int getPrefices() {
		return prefices;
	}
	public void setPrefices(int prefices) {
		this.prefices = prefices;
	}
	public int getSib() {
		return sib;
	}
	public void setSib(int sib) {
		this.sib = sib;
	}
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	@Override
	public String toString() {
		return "prefices: 0x" + Integer.toHexString(prefices) //$NON-NLS-1$
			+ " opcode: 0x" + Integer.toHexString(opcode) //$NON-NLS-1$
			+ " modr/m: 0x" + Integer.toHexString(modrm) //$NON-NLS-1$
			+ " sib: 0x" + Integer.toHexString(sib) //$NON-NLS-1$
			+ " displacement: 0x" + Integer.toHexString(displacement) //$NON-NLS-1$
			+ " immediate: 0x" + Long.toHexString(immediate) //$NON-NLS-1$
			+ " size: 0x" + Integer.toHexString(size); //$NON-NLS-1$
	}
	
	
}
