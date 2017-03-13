package edu.nops.blacksphere.emu;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class EmuMessages {
	private static final String BUNDLE_NAME = "edu.nops.blacksphere.emu.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private EmuMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
