package voteserver;

import common.Event;
import common.EventHandler;

import java.io.IOException;
import common.Connection;
import common.Service;

public class GetCandidateEventHandler implements EventHandler{

	@Override
	public boolean handleEvent(Event e) {
		Connection connection = (Connection) e.get("connection");
		MainServer server = (MainServer) connection.getService();

		Event e1 = new Event("ANNOUNCECANDIDATES");
		
		String district = server.getDistrict(connection);
		e1.put("candidates", server.getElection().getDistricts().get(district));
			try {
				connection.sendEvent(e1);
			} catch (IOException e3) {
				Service.logError("Error Sending Event: " + e3.toString());
			}
		return true;
	}
}
