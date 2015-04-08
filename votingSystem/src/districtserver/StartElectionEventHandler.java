package districtserver;

import java.util.ArrayList;

import common.Connection;
import common.Event;
import common.EventHandler;
import common.Service;
import common.Person.Candidate;

public class StartElectionEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {
		Connection connection = (Connection) e.get("connection");
		DistrictServer server = (DistrictServer) connection.getService();
		
		ArrayList<Candidate> elec = (ArrayList<Candidate>)e.get("candidates");
		
		if (elec == null){
			Service.logError("Missing candidate list");
			return false;
		}
		
		server.setCandidates(elec);
		server.startElection();
		
		return true;
	}
}
	
