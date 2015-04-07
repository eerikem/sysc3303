package districtserver;

import common.Event;
import common.EventHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import servercommon.Server;
import common.Connection;
import common.Connector;
import common.Voter;
import common.Event;
import common.Service;

public class CandidateEventHandler implements EventHandler{

	@Override
	public boolean handleEvent(Event e) {

		// Get the connection that this Handler was called on
		Connection connection = (Connection) e.get("connection");
		ConcurrentHashMap<String, Voter> users = ((DistrictServer) connection
				.getService()).getUsers();
		Service.logInfo("Hello WORLD");
		return false;
	}

}
