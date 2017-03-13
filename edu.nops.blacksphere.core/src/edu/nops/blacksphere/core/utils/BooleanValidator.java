package edu.nops.blacksphere.core.utils;

import org.eclipse.jface.viewers.ICellEditorValidator;

import edu.nops.blacksphere.core.CoreMessages;

public class BooleanValidator implements ICellEditorValidator {

	public String isValid(Object value) {
		try {
			new Boolean((String)value);
			return null;
		} catch (NumberFormatException exc) {
			return value.toString() + CoreMessages.getString("BooleanValidator.NotALogic");   //$NON-NLS-1$
		}
	}

	private static BooleanValidator instance = new BooleanValidator();
	
	public static BooleanValidator getInstance() {
		return instance; 
	}
}
