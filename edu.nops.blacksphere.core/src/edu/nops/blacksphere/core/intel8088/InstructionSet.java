package edu.nops.blacksphere.core.intel8088;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Класс содержащий список описания инструкций
 * @author nops
 */
public class InstructionSet {
	
	public class Instruction {

		/** Имя инструкции */
		@XStreamAsAttribute
		protected String command;
		
		public String getCommand() {
			return command;
		}
		
		/** Описание инструкции */
		// TODO добавить перевод на другой язык
		//@XStreamConverter(BundleConverter.class)
		@XStreamAsAttribute
		protected String description;
		
		public String getDescription() {
			return description;
		}
		
	}

	@XStreamImplicit(itemFieldName="instruction") //$NON-NLS-1$
	ArrayList<Instruction> instructions = new ArrayList<Instruction>();
	
	public int getInstructionCount() {
		return instructions.size();
	}
	
	public Instruction getInstruction(int index) {
		return instructions.get(index);
	}
}
