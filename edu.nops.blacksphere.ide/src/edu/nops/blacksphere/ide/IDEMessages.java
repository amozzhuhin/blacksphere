package edu.nops.blacksphere.ide;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class IDEMessages {
	private static final String BUNDLE_NAME = "edu.nops.blacksphere.ide.wizards.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private IDEMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
