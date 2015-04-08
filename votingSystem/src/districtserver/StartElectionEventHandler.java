package districtserver;

import common.Connection;
import common.Event;
import common.EventHandler;

public class StartElectionEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {
		Connection connection = (Connection) e.get("connection");
		DistrictServer server = (DistrictServer) connection.getService();
		
		server.startElection();
		
		return true;
	}
}
	
