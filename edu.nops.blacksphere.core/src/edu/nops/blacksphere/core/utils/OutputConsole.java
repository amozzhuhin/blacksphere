package edu.nops.blacksphere.core.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * Консоли для вывода информации
 * Обращение к консолям происзодит по имени
 * @author nops
 */
public class OutputConsole {

	/** Потоки открытых консолей */
	private Hashtable<String, MessageConsoleStream> streams;
	
	private OutputConsole() {
		streams = new Hashtable<String, MessageConsoleStream>();
	}

	private static OutputConsole instance;
	
	public static OutputConsole getInstance() {
		if (instance == null)
			instance = new OutputConsole();
		return instance;
	}
	
	/** Открыть новую консоль */
	protected MessageConsoleStream openConsole(String consoleId) {
		MessageConsole console = new MessageConsole(consoleId, null);
		MessageConsoleStream stream = console.newMessageStream();
		streams.put(consoleId, stream);
		ConsolePlugin.getDefault().getConsoleManager()
			.addConsoles(new IConsole[] {console});
		return stream;
	}

	public void close(String consoleId) {
		MessageConsoleStream stream = streams.get(consoleId);
		if (stream != null) {
			streams.remove(consoleId);
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** Закрыть все открытые консоли */
	public void closeAll() {
		Enumeration<MessageConsoleStream> e = streams.elements();
		while (e.hasMoreElements())
			try {
				e.nextElement().close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		streams.clear();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		closeAll();
	}

	public void print(String consoleId, String message) {
		MessageConsoleStream stream = streams.get(consoleId);
		if (stream == null)
			stream = openConsole(consoleId);
		stream.print(message);
	}
}
