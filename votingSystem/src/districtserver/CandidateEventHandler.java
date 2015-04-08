package districtserver;

import common.Event;
import common.EventHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import common.Address;
import common.Connection;
import common.Service;
import common.Person.Candidate;

public class CandidateEventHandler implements EventHandler{

	@Override
	public boolean handleEvent(Event e) throws IOException {
		Connection connection = (Connection) e.get("connection");
		DistrictServer server = (DistrictServer)connection.getService();
		ArrayList<Candidate> elec = (ArrayList<Candidate>) e.get("candidates");
		ConcurrentHashMap<Address, Connection> connections = server.getConnections();
		if (connections == null){
			Service.logWarn("No clients connected");
		}
		
		if (elec == null){
			Service.logError("Missing candidate list");
			return false;
		}
		
		return true;
	}
}
