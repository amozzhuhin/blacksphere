package edu.nops.blacksphere.core.utils;

import org.eclipse.jface.viewers.ICellEditorValidator;

import edu.nops.blacksphere.core.CoreMessages;

public class NumberCellEditorValidator implements ICellEditorValidator {

	public String isValid(Object value) {
		try {
			new Integer((String)value);
			return null;
		} catch (NumberFormatException exc) {
			return value.toString() + CoreMessages.getString("NumberCellEditorValidator.NaN");   //$NON-NLS-1$
		}
	}

	private static NumberCellEditorValidator instance = new NumberCellEditorValidator();
	
	public static NumberCellEditorValidator getInstance() {
		return instance; 
	}
}
