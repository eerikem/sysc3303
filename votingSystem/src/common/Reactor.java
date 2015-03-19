package common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Reactor {

	private static String DEFAULT_FILE = "votingSystem/src/common/services.cfg";
	private HashMap<String, EventHandler> map;
	private Properties properties;

	public Reactor(String file) {
		map = new HashMap<String, EventHandler>();
		properties = new Properties();
		try {
			properties.load(new FileInputStream(file));
			init();
		} catch (FileNotFoundException e) {
			Service.logError("File not found.");
			System.exit(0);
		} catch (IOException e) {
			Service.logError(e.toString());
			System.exit(0);
		} catch (ClassNotFoundException e) {
			Service.logError(e.toString());
			System.exit(0);
		} catch (InstantiationException e) {
			Service.logError(e.toString());
			System.exit(0);
		} catch (IllegalAccessException e) {
			Service.logError(e.toString());
			System.exit(0);
		}
	}

	public Reactor() {
		this(DEFAULT_FILE);
	}

	// Initialize the possible events, expecting a set of key-value pairs from cfg file.
	public void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		String className;
		for (Object key : properties.keySet()) {
			className = (String) properties.get(key);
			@SuppressWarnings("unchecked")
			Class<EventHandler> clazz = (Class<EventHandler>) Class.forName(className);
			EventHandler h = clazz.newInstance();
			registerHandler((String) key, h);
		}
	}

	public void registerHandler(String type, EventHandler handler) {
		map.put(type, handler);
	}

	public void removeHandler(String type) {
		map.remove(type);
	}

	// Dispatches Event e to the appropriate EventHandler.
	public boolean dispatch(Event e) {
		EventHandler h = map.get(e.getType());
		if (h == null)
			h = map.get("UNKNOWN");
		return h.handleEvent(e);
	}
}
