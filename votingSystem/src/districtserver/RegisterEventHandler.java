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
		DistrictServer server = (DistrictServer) connection.getService();
		ConcurrentHashMap<String, Voter> users = server.getUsers();
		
		Voter p = (Voter)e.get("person");

		Event e1 = new Event("REGISTER");
		if (users.containsKey(p.username)){
			e1.put("response", "already_registered");
			Service.logWarn(p.username + " already registered.");
		}else{
			if(!p.district.equals(server.getDistrictName())){
				e1.put("response", "district_mismatch");
				Service.logInfo("Voter lives outside of district " + p.username);
			}else{
				users.put(p.username, p);
				e1.put("response", "registration_success");
				Service.logInfo("Registered " + p.username);
			}
		}

		try {
			connection.sendEvent(e1);
		} catch (IOException e3) {
			Service.logError("Error Sending Event: " + e3.toString());
		}
		return true;
	}
}
