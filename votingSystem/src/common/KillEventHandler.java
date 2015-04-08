package common;

import common.Connection;
import common.Event;
import common.EventHandler;
import common.Service;
import districtserver.DistrictServer;


public class KillEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {

		Connection connection = (Connection) e.get("connection");
		if (!(e.get("district")==null)){
			DistrictServer server = (DistrictServer)connection.getService();
			server.stopServer();
		}
		Service.logInfo("Killing connection to Client "+connection.socket.getPort());
		connection.stop();
		return true;
	}

}
