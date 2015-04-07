package districtserver;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import common.Connection;
import common.Event;
import common.EventHandler;
import common.Voter;
import common.Service;

public class RegisterEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {

		// Get the connection that this Handler was called on
		Connection connection = (Connection) e.get("connection");
		ConcurrentHashMap<String, Voter> users = ((DistrictServer) connection
				.getService()).getUsers();
		
		Voter p = (Voter)e.get("person");

		Event e1 = new Event("REGISTER");
		if (users.containsKey(p.username)){
			e1.put("response", "already_registered");
			Service.logWarn(p.username + " already registered.");
		}else{
			users.put(p.username, p);
			e1.put("response", "registration_success");
			Service.logInfo("Registered " + p.username);
		}

		try {
			connection.sendEvent(e1);
		} catch (IOException e3) {
			Service.logError("Error Sending Event: " + e3.toString());
		}
		return true;
	}
}
