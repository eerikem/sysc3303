package voteserver;

import java.util.concurrent.ConcurrentHashMap;

import common.Event;
import common.EventHandler;
import common.Service;
import common.Address;
import common.Connection;


import districtserver.DistrictServer;

public class UpdateVotesEventHandler implements EventHandler{
	
	@Override
	public boolean handleEvent(Event e) {

		// Get the connection that this Handler was called on
		Connection connection = (Connection) e.get("connection");
		

		ConcurrentHashMap<String, Integer> votes = (ConcurrentHashMap<String, Integer>) e.get("votes");
		
		((MainServer) connection.getService()).updateVotes(votes);

		

		return true;
	}
}
