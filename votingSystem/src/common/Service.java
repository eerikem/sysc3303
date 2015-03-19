package common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Service {

	private static int THREAD_POOL_COUNT = 5;

	protected Reactor reactor;
	protected String protocol;
	protected ExecutorService threadPool;

	public Service(String file) {
		this.reactor = new Reactor(file);
		this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_COUNT);
	}

	public Reactor getReactor() {
		return reactor;
	}

	/**
	 * Logs Info to console.
	 * 
	 * @param message
	 */
	public static void logInfo(String message) {
		Date date = new Date();
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(date);
		System.out.println(timestamp + " [INFO] " + message);
	}

	/**
	 * Logs Warning to console.
	 * 
	 * @param message
	 */
	public static void logWarn(String message) {
		Date date = new Date();
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(date);
		System.out.println(timestamp + " [WARN] " + message);
	}

	/**
	 * Logs Error to console.
	 * 
	 * @param message
	 */
	public static void logError(String message) {
		Date date = new Date();
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(date);
		System.err.println(timestamp + " [ERROR] " + message);
	}

	public ExecutorService getThreadPool() {
		return threadPool;
	}
}
