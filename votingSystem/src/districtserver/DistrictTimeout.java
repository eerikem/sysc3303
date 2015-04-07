package districtserver;

import common.Service;

public class DistrictTimeout extends Thread {
	
	private long timeout = 2000;
	
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
	}

}
