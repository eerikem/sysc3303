package client;

import common.Event;
import common.EventHandler;

import java.util.ArrayList;

import common.Connection;
import common.Person.Candidate;

public class CandidateEventHandler implements EventHandler{

	@Override
	public boolean handleEvent(Event e) {
		Connection connection = (Connection) e.get("connection");
		//get the parent server from that connections
		Client client = (Client)connection.getService();
		// Get the connection that this Handler was called on
		ArrayList<Candidate> elec = (ArrayList<Candidate>) e.get("candidates");
		client.setCandidates(elec);
		return false;
		
	}
}
