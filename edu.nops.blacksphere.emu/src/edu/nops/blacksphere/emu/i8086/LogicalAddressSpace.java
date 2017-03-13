package edu.nops.blacksphere.emu.i8086;

public class LogicalAddressSpace {

	private PhysicalAddressSpace physicSpace;
	
	public LogicalAddressSpace(PhysicalAddressSpace physicSpace) {
		this.physicSpace = physicSpace;
	}

	/** Получить значение байта в памяти по указанному адресу */
	public int getByte(int segment, int offset) {
		int poffset = (segment << 4) + offset;
		int data = physicSpace.getByte(poffset);
		return data;
	}
	
	/** Установить значение байта в памяти по указанному адресу */
	public void setByte(int segment, int offset, int data) {
		int poffset = (segment << 4) + offset;
		physicSpace.setByte(poffset, data);
	}

	/** Получить значение слова в памяти по указанному адресу */
	public int getWord(int segment, int offset) {
		int poffset = (segment << 4) + offset;
		return (physicSpace.getByte(poffset) & 0xff)
				| ((physicSpace.getByte(poffset+1) << 8) & 0xff00);
	}
	
	/** Установить значение слова в памяти по указанному адресу */
	public void setWord(int segment, int offset, int data) {
		int poffset = (segment << 4) + offset;
		physicSpace.setByte(poffset, data & 0xff);
		physicSpace.setByte(poffset+1, (data >> 8) & 0xff);
	}
	
	/** Получить значение двойного слова в памяти по указанному адресу */
	public int getDWord(int segment, int offset) {
		int poffset = (segment << 4) + offset;
		return (physicSpace.getByte(poffset) & 0xff)
				| ((physicSpace.getByte(poffset+1) << 8) & 0xff00)
				| ((physicSpace.getByte(poffset+2) << 16) & 0xff0000)
				| ((physicSpace.getByte(poffset+3) << 24) & 0xff000000);
	}
	
}
