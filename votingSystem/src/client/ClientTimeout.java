package client;

import common.Service;

public class ClientTimeout extends Thread {
	
	private Client client;
	private long timeout = 2000;

	public ClientTimeout(Client aClient)
	{
		client = aClient;
	}
	
	public void run()
	{
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			try {
				join(0);
			} catch (InterruptedException e1) {
				Service.logError("Timeout thread interrupted while joining.");
			}
		}
		Service.logError("Request timed out.");
		client.requestTimedout();
	}
}
