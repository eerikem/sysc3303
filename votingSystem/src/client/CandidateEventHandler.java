package client;

import common.Event;
import common.EventHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import servercommon.Server;
import voteserver.ElectionCandidates;
import common.Address;
import common.Connection;
import common.Connector;
import common.Voter;
import common.Event;
import common.Service;
import common.Person.Candidate;
import districtserver.DistrictServer;

public class CandidateEventHandler implements EventHandler{

	@Override
	public boolean handleEvent(Event e) {
		Connection connection = (Connection) e.get("connection");
		//get the parent server from that connections
		Client client = (Client)connection.getService();
		// Get the connection that this Handler was called on
		ElectionCandidates elec = (ElectionCandidates) e.get("can");
		//get the connections from the server 
		for (Candidate i : elec.getCandidate()){
			System.out.println(i.getName());
		}
		client.setCandidates(elec);
		return false;
		
	}

}
