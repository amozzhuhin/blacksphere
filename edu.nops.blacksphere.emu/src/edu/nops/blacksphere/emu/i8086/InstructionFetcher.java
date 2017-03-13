package edu.nops.blacksphere.emu.i8086;

import java.util.HashMap;

import edu.nops.blacksphere.emu.EmuMessages;

public class InstructionFetcher {

	private Processor processor;
	private HashMap<Integer, Instruction> instructionCache;
	private OperationFactory factory;
	
	public InstructionFetcher(Processor proc) {
		processor = proc;
		instructionCache = new HashMap<Integer, Instruction>();
		factory = new OperationFactory(proc);
	}
	
	/** Получить текущую инструкцию для выполнения */
	public Instruction fetch(int seg, int offset) {
		int poffset = (seg << 4) + offset;
		Instruction instruction = instructionCache.get(poffset);
		if (instruction == null) {
			instruction = parse(seg, offset);
			instructionCache.put(poffset, instruction);
		}
		return instruction;
	}

	// true для кодов требующих байт modrm
    private static final boolean[] modrmArray = new boolean[] { 
    	true, true, true, true, false, false, false, false, true, true, true, true, false, false, false, false,
    	true, true, true, true, false, false, false, false, true, true, true, true, false, false, false, false,
    	true, true, true, true, false, false, false, false, true, true, true, true, false, false, false, false,
    	true, true, true, true, false, false, false, false, true, true, true, true, false, false, false, false,
    	
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    	false, false, true, true, false, false, false, false, false, true, false, true, false, false, false, false,
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    	
    	true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    	
    	true, true, false, false, true, true, true, true, false, false, false, false, false, false, false, false,
    	true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false,
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    	false, false, false, false, false, false, true, true, false, false, false, false, false, false, true, true
    };
        
    // true для значений modrm, которые требуют байт sib (32 bit addressing only)
    private static final boolean[] sibArray = new boolean[] { 
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
    };

    // true для кодов требующих байт modrm
    private static final boolean[] twoByte_0f_modrmArray = new boolean[] {
    	true, true, true, true, false, false, false, false, false, false, false, true,  false, false, false, false,
    	true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false,
    	true, true, true, true, true, false, true, false, true, true, true, true, true, true, true, true,
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,

    	true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, 
    	true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, 
    	true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, 
    	true, true, true, true, true, true, true, false, false, false, false, false, true, true, true, true, 

    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, 
    	true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, 
    	false, false, false, true, true, true, false, false, false, false, false, true, true, true, true, true,
    	true, true, true, true, true, true, true, true, false, true, true, true, true, true, true, true, 

    	true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, 
     	true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, 
    	true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, 
    	true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true
    };

    // true для значений modrm, которые требуют байт sib
    private static final boolean[] twoByte_0f_sibArray = new boolean[] {
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false,
    	
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
    	false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
    };

	/** Разобрать код по заданному адресу и получить операцию */
	protected Instruction parse(int seg, int offset) {
		int ioffset = offset;
		int opcode = 0;
		int opcodePrefix = 0;
		int prefices = 0;
		int modrm = -1;
		int sib = -1;
		LogicalAddressSpace mem = processor.getMemory();
		// разбор префиксов
		while (true) {
		    switch (opcode = 0xff & mem.getByte(seg, offset++)) {
		    case 0x0f: // двухбайтная команда
		    	opcodePrefix = (opcodePrefix << 8) | opcode;
		    	opcode = 0xff & mem.getByte(seg, offset++);
		    	modrm = opcode;
		    	break;
		    case 0xd8: // двухбайтные команды
		    case 0xd9:
		    case 0xda:
		    case 0xdb:
		    case 0xdc:
		    case 0xdd:
		    case 0xde:
		    case 0xdf:
		    	opcodePrefix = (opcodePrefix << 8) | opcode;
		    	opcode = 0;
		    	modrm = 0xff & mem.getByte(seg, offset++);
		    	break;
		    //сегментные префиксы
		    case 0x2e:
		    	prefices &= ~OperationFactory.PREFICES_SG;
		    	prefices |= OperationFactory.PREFICES_CS;
		    	continue;
		    case 0x3e:
		    	prefices &= ~OperationFactory.PREFICES_SG;
		    	prefices |= OperationFactory.PREFICES_DS;
		    	continue;
		    case 0x26:
		    	prefices &= ~OperationFactory.PREFICES_SG;
		    	prefices |= OperationFactory.PREFICES_ES;
		    	continue;
		    case 0x36:
		    	prefices &= ~OperationFactory.PREFICES_SG;
		    	prefices |= OperationFactory.PREFICES_SS;
		    	continue;
		    case 0x64:
		    	prefices &= ~OperationFactory.PREFICES_SG;
		    	prefices |= OperationFactory.PREFICES_FS;
		    	continue;
		    case 0x65:
		    	prefices &= ~OperationFactory.PREFICES_SG;
		    	prefices |= OperationFactory.PREFICES_GS;
		    	continue;
			// изменение разрядности
		    case 0x66:
		    	prefices = prefices ^ OperationFactory.PREFICES_OPERAND;
		    	continue;
		    case 0x67:
		    	prefices = prefices ^ OperationFactory.PREFICES_ADDRESS;
		    	continue;
			// повторение
		    case 0xf2:
		    	prefices |= OperationFactory.PREFICES_REPNE;
		    	continue;
		    case 0xf3:
		    	prefices |= OperationFactory.PREFICES_REPE;
		    	continue;
			// lock
		    case 0xf0:
		    	prefices |= OperationFactory.PREFICES_LOCK;
		    	continue;
		    default:
		    	break;
		    }
		    break;
		}

		opcode = (opcodePrefix << 8) | opcode;

		// чтение modrm и sib байтов
		switch (opcodePrefix) {
		case 0x00:
		    if (modrmArray[opcode]) {
		    	modrm = 0xff & mem.getByte(seg, offset++);
		    } else {
		    	modrm = -1;
		    }
		    if ((modrm == -1) || ((prefices & OperationFactory.PREFICES_ADDRESS) == 0)) {
		    	sib = -1;
		    } else {
		    	if (sibArray[modrm]) {
		    		sib = 0xff & mem.getByte(seg, offset++);
		    	} else {
		    		sib = -1;
		    	}
		    }
		    break;
		case 0x0f:
		    if (twoByte_0f_modrmArray[0xff & opcode]) {
		    	modrm = 0xff & mem.getByte(seg, offset++);
		    } else {
		    	modrm = -1;
		    }
		    if ((modrm == -1) || ((prefices & OperationFactory.PREFICES_ADDRESS) == 0)) {
		    	sib = -1;
		    } else {
		    	if (twoByte_0f_sibArray[modrm]) {
		    		sib = 0xff & mem.getByte(seg, offset++);
		    	} else {
		    		sib = -1;
		    	}
		    }
		    break;
		case 0xd8:
		case 0xd9:
		case 0xda:
		case 0xdb:
		case 0xdc:
		case 0xdd:
		case 0xde:
		case 0xdf:
		    if (sibArray[modrm]) {
		    	sib = 0xff & mem.getByte(seg, offset++);
		    } else {
		    	sib = -1;
		    }
		    break;
		default:
		    modrm = -1;
		    sib = -1;
		    break;
		}
		
		int displacement = 0;
		
		switch (operationHasDisplacement(prefices, opcode, modrm, sib)) {
		case 0:
		    break;
		case 1:
		    displacement = mem.getByte(seg, offset++);
		    break;
		case 2:
		    displacement = mem.getWord(seg, offset);
		    offset += 2;
		    break;
		default:
		    assert false : EmuMessages.getString("InstructionFetcher.DesplacementBigger2"); //$NON-NLS-1$
		    break;
		}

		long immediate = 0;
		
		switch (operationHasImmediate(prefices, opcode, modrm)) {
		case 0:
		    break;
		case 1:
		    immediate = mem.getByte(seg, offset++);
		    break;
		case 2:
		    immediate = mem.getWord(seg, offset);
		    offset += 2;
		    break;
		case 3:
		    immediate = ((mem.getByte(seg, offset) << 16) & 0xff0000)
		    		| ((mem.getByte(seg, offset+1) << 24) & 0xff000000)
		    		| (mem.getByte(seg, offset+2) & 0xff);
		    break;
		case 4:
		    immediate = mem.getDWord(seg, offset);
		    offset += 4;
		    break;
		case 6:
		    immediate = 0xffffffffl & ((mem.getByte(seg, offset) & 0xff)
		    		| ((mem.getByte(seg, offset+1) << 8) & 0xff00)
		    		| ((mem.getByte(seg, offset+2) << 16) & 0xff0000)
		    		| ((mem.getByte(seg, offset+3) << 24) & 0xff000000));
		    immediate |= ((mem.getByte(seg, offset+4) & 0xffl)
		    		| ((mem.getByte(seg, offset+5) << 8) & 0xff00l)) << 32;
		    offset += 6;
		    break;
		default:
		    assert false : EmuMessages.getString("InstructionFetcher.immediateBigger6"); //$NON-NLS-1$
		    break;
		}

		//write out input operands
		//writeInputOperands(prefices, opcode, modrm, sib, displacement, immediate);

		//write out calculation
		//writeOperation(prefices, opcode, modrm);

		//write out output operands
		//writeOutputOperands(prefices, opcode, modrm, sib, displacement);

		//write out flags
		//writeFlags(prefices, opcode, modrm);

		//if (isJump(opcode, modrm))
		    //return -bytesRead;
		//else
		  //  return bytesRead;
		Instruction result = new Instruction();
		result.setDisplacement(displacement);
		result.setImmediate(immediate);
		result.setModrm(modrm);
		result.setOpcode(opcode);
		result.setPrefices(prefices);
		result.setSib(sib);
		result.setSize(offset-ioffset);
		result.setOperation(factory.createOperation(result));
		return result;
	}
	
    private static int operationHasDisplacement(int prefices, int opcode, int modrm, int sib)
    {
		switch (opcode) {
		    //modrm things
		case 0x00: //ADD  Eb, Gb
		case 0x01: //ADD Ev, Gv
		case 0x02: //ADD Gb, Eb
		case 0x03: //ADD Gv, Ev
		case 0x08: //OR   Eb, Gb
		case 0x09: //OR  Ev, Gv
		case 0x0a: //OR  Gb, Eb
		case 0x0b: //OR  Gv, Ev
		case 0x10: //ADC  Eb, Gb
		case 0x11: //ADC Ev, Gv
		case 0x12: //ADC Gb, Eb
		case 0x13: //ADC Gv, Ev
		case 0x18: //SBB  Eb, Gb
		case 0x19: //SBB Ev, Gv
		case 0x1a: //SBB Gb, Eb
		case 0x1b: //SBB Gv, Ev
		case 0x20: //AND  Eb, Gb
		case 0x21: //AND Ev, Gv
		case 0x22: //AND Gb, Eb
		case 0x23: //AND Gv, Ev
		case 0x28: //SUB  Eb, Gb
		case 0x29: //SUB Ev, Gv
		case 0x2a: //SUB Gb, Eb
		case 0x2b: //SUB Gv, Ev
		case 0x30: //XOR  Eb, Gb
		case 0x31: //XOR Ev, Gv
		case 0x32: //XOR Gb, Eb
		case 0x33: //XOR Gv, Ev
		case 0x38: //CMP  Eb, Gb
		case 0x39: //CMP  Ev, Gv
		case 0x3a: //CMP Gb, Eb
		case 0x3b: //CMP Gv, Ev
		case 0x62: //BOUND Gv, Ma
		case 0x69: //IMUL Gv, Ev, Iv
		case 0x6b: //IMUL Gv, Ev, Ib
		case 0x80: //IMM G1 Eb, Ib
		case 0x81: //IMM G1 Ev, Iv
		case 0x82: //IMM G1 Eb, Ib
		case 0x83: //IMM G1 Ev, Ib
		case 0x84: //TEST Eb, Gb
		case 0x85: //TEST Ev, Gv
		case 0x86: //XCHG Eb, Gb
		case 0x87: //XCHG Ev, Gv
		case 0x88: //MOV  Eb, Gb
		case 0x89: //MOV  Ev, Gv
		case 0x8a: //MOV Gb, Eb
		case 0x8b: //MOV Gv, Ev
		case 0x8c: //MOV Ew, Sw
		case 0x8d: //LEA Gv, M
		case 0x8e: //MOV Sw, Ew
		case 0x8f: //POP Ev
		case 0xc0: //SFT G2 Eb, Ib
		case 0xc1: //SFT G2 Ev, Ib
		case 0xc4: //LES Gv, Mp
		case 0xc5: //LDS Gv, Mp
		case 0xc6: //MOV G11 Eb, Ib
		case 0xc7: //MOV G11 Ev, Iv
		case 0xd0: //SFT G2 Eb, 1
		case 0xd1: //SFT G2 Ev, 1
		case 0xd2: //SFT G2 Eb, CL
		case 0xd3: //SFT G2 Ev, CL
		case 0xf6: //UNA G3 Eb, ?
		case 0xf7: //UNA G3 Ev, ?
		case 0xfe: //INC/DEC G4 Eb
		case 0xff: //INC/DEC G5
	
		case 0xf00: //Grp 6
		case 0xf01: //Grp 7
		      
		case 0xf20: //MOV Rd, Cd
		case 0xf22: //MOV Cd, Rd
	
		case 0xf40: //CMOVO
		case 0xf41: //CMOVNO
		case 0xf42: //CMOVC
		case 0xf43: //CMOVNC
		case 0xf44: //CMOVZ
		case 0xf45: //CMOVNZ
		case 0xf46: //CMOVBE
		case 0xf47: //CMOVNBE
		case 0xf48: //CMOVS
		case 0xf49: //CMOVNS
		case 0xf4a: //CMOVP
		case 0xf4b: //CMOVNP
		case 0xf4c: //CMOVL
		case 0xf4d: //CMOVNL
		case 0xf4e: //CMOVLE
		case 0xf4f: //CMOVNLE
	
		case 0xf90: //SETO
		case 0xf91: //SETNO
		case 0xf92: //SETC
		case 0xf93: //SETNC
		case 0xf94: //SETZ
		case 0xf95: //SETNZ
		case 0xf96: //SETBE
		case 0xf97: //SETNBE
		case 0xf98: //SETS
		case 0xf99: //SETNS
		case 0xf9a: //SETP
		case 0xf9b: //SETNP
		case 0xf9c: //SETL
		case 0xf9d: //SETNL
		case 0xf9e: //SETLE
		case 0xf9f: //SETNLE
	
		case 0xfa3: //BT Ev, Gv
		case 0xfa4: //SHLD Ev, Gv, Ib
		case 0xfa5: //SHLD Ev, Gv, CL
		case 0xfab: //BTS Ev, Gv
		case 0xfac: //SHRD Ev, Gv, Ib
		case 0xfad: //SHRD Ev, Gv, CL
		      
		case 0xfaf: //IMUL Gv, Ev
		      
		case 0xfb0: //CMPXCHG Eb, Gb
		case 0xfb1: //CMPXCHG Ev, Gv
		case 0xfb2: //LSS Mp
		case 0xfb3: //BTR Ev, Gv
		case 0xfb4: //LFS Mp
		case 0xfb5: //LGS Mp
		case 0xfb6: //MOVZX Gv, Eb
		case 0xfb7: //MOVZX Gv, Ew
	
		case 0xfba: //Grp 8 Ev, Ib
		case 0xfbb: //BTC Ev, Gv
		case 0xfbc: //BSF Gv, Ev
		case 0xfbd: //BSR Gv, Ev
		case 0xfbe: //MOVSX Gv, Eb
		case 0xfbf: //MOVSX Gv, Ew
		case 0xfc0: //XADD Eb, Gb
		case 0xfc1: //XADD Ev, Gv
	            return modrmHasDisplacement(prefices, modrm, sib);
	
	            //From Input
	        case 0xd800:
	        case 0xd900:
	        case 0xda00:
	        case 0xdb00:
	        case 0xdc00:
	        case 0xdd00:
	        case 0xde00:
	        case 0xdf00:
	            if ((modrm & 0xc0) != 0xc0)
	                return modrmHasDisplacement(prefices, modrm, sib);
	            else
	                return 0;
	
		    //special cases
		case 0xa0: //MOV AL, Ob
		case 0xa2: //MOV Ob, AL
		case 0xa1: //MOV eAX, Ov
		case 0xa3: //MOV Ov, eAX
		    if ((prefices & OperationFactory.PREFICES_ADDRESS) != 0)
			return 4;
		    else
			return 2;
	
		default: return 0;
		}

    }
    
    private static int modrmHasDisplacement(int prefices, int modrm, int sib)
    {
	if ((prefices & OperationFactory.PREFICES_ADDRESS) != 0) {
	    //32 bit address size
	    switch(modrm & 0xc0) {
	    case 0x00:
		switch (modrm & 0x7) {
		case 0x4:
		    if ((sib & 0x7) == 0x5)
			return 4;
		    else
			return 0;
		case 0x5: return 4;
		}
		break;
	    case 0x40: return 1; //IB
	    case 0x80: return 4; //ID
	    }
	} else {
	    //16 bit address size
	    switch(modrm & 0xc0) {
	    case 0x00:
		if ((modrm & 0x7) == 0x6)
		    return 2;
		else
		    return 0;
	    case 0x40: return 1; //IB
	    case 0x80: return 2; //IW
	    }
	}

	return 0;
    }

    private static int operationHasImmediate(int prefices, int opcode, int modrm)
    {
	switch (opcode) {
	case 0x04: //ADD AL, Ib
	case 0x0c: //OR  AL, Ib
	case 0x14: //ADC AL, Ib
	case 0x1c: //SBB AL, Ib
	case 0x24: //AND AL, Ib
	case 0x2c: //SUB AL, Ib
	case 0x34: //XOR AL, Ib
	case 0x3c: //CMP AL, Ib
	case 0x6a: //PUSH Ib
	case 0x6b: //IMUL Gv, Ev, Ib
	case 0x70: //Jcc Jb
	case 0x71:
	case 0x72:
	case 0x73:
	case 0x74:
	case 0x75:
	case 0x76:
	case 0x77:
	case 0x78:
	case 0x79:
	case 0x7a:
	case 0x7b:
	case 0x7c:
	case 0x7d:
	case 0x7e:
	case 0x7f:
	case 0x80: //IMM G1 Eb, Ib
	case 0x82: //IMM G1 Eb, Ib
	case 0x83: //IMM G1 Ev, Ib
	case 0xa8: //TEST AL, Ib
	case 0xb0: //MOV AL, Ib
	case 0xb1: //MOV CL, Ib
	case 0xb2: //MOV DL, Ib
	case 0xb3: //MOV BL, Ib
	case 0xb4: //MOV AH, Ib
	case 0xb5: //MOV CH, Ib
	case 0xb6: //MOV DH, Ib
	case 0xb7: //MOV BH, Ib
	case 0xc0: //SFT G2 Eb, Ib
	case 0xc1: //SFT G2 Ev, Ib
	case 0xc6: //MOV G11 Eb, Ib
	case 0xcd: //INT Ib
	case 0xd4: //AAM Ib
	case 0xd5: //AAD Ib
	case 0xe0: //LOOPNZ Jb
	case 0xe1: //LOOPZ Jb
	case 0xe2: //LOOP Jb
	case 0xe3: //JCXZ Jb
	case 0xe4: //IN  AL, Ib
	case 0xe5: //IN eAX, Ib
	case 0xe6: //OUT Ib, AL
	case 0xe7: //OUT Ib, eAX
	case 0xeb: //JMP Jb
	case 0xfa4: //SHLD Ev, Gv, Ib
	case 0xfac: //SHRD Ev, Gv, Ib
	case 0xfba: //Grp 8 Ev, Ib
	    return 1;

	case 0xc2: //RET Iw
	case 0xca: //RETF Iw
	    return 2;

	case 0xc8: //ENTER Iw, Ib
	    return 3;

	case 0x05: //ADD eAX, Iv
	case 0x0d: //OR  eAX, Iv
	case 0x15: //ADC eAX, Iv
	case 0x1d: //SBB eAX, Iv
	case 0x25: //AND eAX, Iv
	case 0x2d: //SUB eAX, Iv
	case 0x35: //XOR eAX, Iv
	case 0x3d: //CMP eAX, Iv
	case 0x68: //PUSH Iv
	case 0x69: //IMUL Gv, Ev, Iv
	case 0x81: //IMM G1 Ev, Iv
	case 0xa9: //TEST eAX, Iv
	case 0xb8: //MOV eAX, Iv
	case 0xb9: //MOV eCX, Iv
	case 0xba: //MOV eDX, Iv
	case 0xbb: //MOV eBX, Iv
	case 0xbc: //MOV eSP, Iv
	case 0xbd: //MOV eBP, Iv
	case 0xbe: //MOV eSI, Iv
	case 0xbf: //MOV eDI, Iv
	case 0xc7: //MOV G11 Ev, Iv
	case 0xe8: //CALL Jv
	case 0xe9: //JMP  Jv
	case 0xf80: //JO Jv
	case 0xf81: //JNO Jv
	case 0xf82: //JC Jv
	case 0xf83: //JNC Jv
	case 0xf84: //JZ Jv
	case 0xf85: //JNZ Jv
	case 0xf86: //JNA Jv
	case 0xf87: //JA Jv
	case 0xf88: //JS Jv 
	case 0xf89: //JNS Jv
	case 0xf8a: //JP Jv 
	case 0xf8b: //JNP Jv
	case 0xf8c: //JL Jv 
	case 0xf8d: //JNL Jv
	case 0xf8e: //JNG Jv 
	case 0xf8f: //JG Jv	
	    if ((prefices & OperationFactory.PREFICES_OPERAND) != 0)
		return 4;
	    else
		return 2;

	case 0x9a: //CALLF Ap
	case 0xea: //JMPF Ap
	    if ((prefices & OperationFactory.PREFICES_OPERAND) != 0)
		return 6;
	    else
		return 4;

	case 0xf6: //UNA G3 Eb, ?
	    switch (modrm & 0x38) {
	    case 0x00: //TEST Eb, Ib
		return 1;
	    default:
		return 0;
	    }

	case 0xf7: //UNA G3 Ev, ?
	    switch (modrm & 0x38) {
	    case 0x00: //TEST Ev, Iv
		if ((prefices & OperationFactory.PREFICES_OPERAND) != 0)
		    return 4;
		else
		    return 2;
	    default:
		return 0;
	    }
	}
	return 0;
    }
}
