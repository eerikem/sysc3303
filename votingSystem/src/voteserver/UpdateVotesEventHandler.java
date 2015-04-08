package voteserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import common.Event;
import common.EventHandler;
import common.Service;
import common.Address;
import common.Connection;
import common.Person.Candidate;
import districtserver.DistrictServer;

public class UpdateVotesEventHandler implements EventHandler{
	
	@Override
	public boolean handleEvent(Event e) {

		// Get the connection that this Handler was called on
		Connection connection = (Connection) e.get("connection");		

		ConcurrentHashMap<Candidate, Integer> votes = (ConcurrentHashMap<Candidate, Integer>) e.get("votes");
		
		HashMap<Candidate, Integer> hashMap = new HashMap<Candidate, Integer>(votes);
		((MainServer) connection.getService()).updateVotes(hashMap);

		//Send reply event
		Event e1 = new Event("VOTESUPDATED");
		e1.put("votes", hashMap);
		
		try {
			connection.sendEvent(e1);
		} catch (IOException e2) {
			Service.logError("Could not handshake votes back to district server.");
		}

		return true;
	}
}
