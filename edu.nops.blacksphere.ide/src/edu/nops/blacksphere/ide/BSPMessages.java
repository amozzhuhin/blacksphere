package edu.nops.blacksphere.ide;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class BSPMessages {
	private static final String BUNDLE_NAME = "edu.nops.blacksphere.ide.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private BSPMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
