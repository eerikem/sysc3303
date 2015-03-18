package voteserver;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import common.Address;
import common.Connection;
import common.Event;
import common.EventHandler;
import common.Service;

public class ElectionStartEventHandler implements EventHandler {
	
	@Override
	public boolean handleEvent(Event e) {

		// Get the connection that this Handler was called on
		Connection connection = (Connection) e.get("connection");
		ConcurrentHashMap<Address, Connection> connections = ((MainServer) connection
				.getService()).getConnections();
		ConcurrentHashMap<String, Integer> votes = ((MainServer) connection
				.getService()).getVotes();
		Enumeration<String> candidates = votes.keys();

		// Send the candidate list to all districts (connections)
		Event e1 = new Event("STARTCOUNTING");
		e1.put("candidates", (Serializable) candidates);

		for(Connection con: connections.values())
		{
			try {
				con.sendEvent(e1);
			} catch (IOException e3) {
				Service.logError("Error Sending Event: " + e3.toString());
			}
		}
		return true;
	}
}


