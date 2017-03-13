package edu.nops.blacksphere.core.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.nops.blacksphere.core.CoreMessages;

/**
 * Перекодировка потока ввода
 * @author nops
 */
public class InputStreamDecoder {

	public static InputStream decodeInputStream(InputStream input,
			String fromCharSet, String toCharSet) {
		if (fromCharSet.equals(toCharSet))
			return input;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(input, fromCharSet));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while (br.ready()) {
				bos.write(br.readLine().getBytes(toCharSet));
				bos.write(System.getProperty("line.separator").getBytes()); //$NON-NLS-1$
			}
			return new ByteArrayInputStream(bos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return input;
		}
	}
}
