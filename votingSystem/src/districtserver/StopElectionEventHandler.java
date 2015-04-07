package districtserver;

import common.Connection;
import common.Event;
import common.EventHandler;

public class StopElectionEventHandler implements EventHandler {
	
	@Override
	public boolean handleEvent(Event e) {
		Connection connection = (Connection) e.get("connection");
		DistrictServer server = (DistrictServer) connection.getService();
		server.stopElection();
		
		return true;
	}

}
