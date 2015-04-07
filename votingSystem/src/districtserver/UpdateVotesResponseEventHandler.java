package districtserver;

import java.util.HashMap;

import common.Connection;
import common.Event;
import common.EventHandler;
import common.Service;

public class UpdateVotesResponseEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {

		// Get the connection that this Handler was called on
		Service.logInfo("Received Updated Votes Response.");
		Connection connection = (Connection) e.get("connection");
		DistrictServer server = (DistrictServer) connection.getService();
		server.districtTimeout.interrupt();
		
		HashMap<String,Integer> v = (HashMap<String, Integer>) e.get("votes");
		server.serverUpdated(v);
		
		return true;
	}
}
