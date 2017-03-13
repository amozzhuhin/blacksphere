package edu.nops.blacksphere.emu.i8086;

/**
 * Физическое аддресное пространство
 * @author nops
 */
public class PhysicalAddressSpace {

	/** Массив значений в ОЗУ (тип int выбран для ускорения обращения) */
	private int[] ram;
	/** Массив значений в ПЗУ (тип int выбран для ускорения обращения) */
	private int[] rom;
	/** Маска адреса для отрезания не существующей части памяти */
	private int mask;
	
	/**
	 * Создать физическое адресное пространство
	 * @param ramSize размер ОЗУ в Кб
	 * @param romSize размер ПЗУ в Кб
	 */
	public PhysicalAddressSpace(int ramSize, int romSize) {
		ram = new int[ramSize << 10];
		rom = new int[romSize << 10];
		// формирование маски для обрезания адресов
		mask = ramSize + romSize - 1;
		for(int i=0; i < 10; i++) {
			mask <<= 1;
			mask |= 1;
		}
	}

	/**
	 * Получить байт по физическому адресу
	 * @param offset физический адрес
	 * @return значение байта по запрашиваемому адресу
	 */
	public int getByte(int offset) {
		offset &= mask;
		if (offset < ram.length)
			return ram[offset];
		else
			return rom[offset - ram.length];
	}
	
	public void setByte(int offset, int data) {
		//TODO в случае записи в ПЗУ, нужно что-то делать
		// но в период инициализации разрешается писать в ПЗУ
		offset &= mask;
		if (offset < ram.length)
			ram[offset] = data;
		else
			rom[offset - ram.length] = data;
	}
}
