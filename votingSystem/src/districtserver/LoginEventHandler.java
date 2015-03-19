package districtserver;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import common.Connection;
import common.Event;
import common.EventHandler;
import common.Service;

public class LoginEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {

		// Get the connection that this Handler was called on
		Connection connection = (Connection) e.get("connection");
		ConcurrentHashMap<String, String> users = ((DistrictServer) connection
				.getService()).getUsers();

		String username = (String) e.get("username");
		String password = (String) e.get("password");

		// Send Login Reply
		Event e1 = new Event("LOGINREPLY");
		if (username == null || username.equals("")) {
			e1.put("response", "blank_user");
			Service.logWarn("Login attempt: blank user");
		} else if (users.containsKey(username)) {
			if (password.equals(users.get(username))) {
				e1.put("response", "login_success");
				Service.logInfo(username + " logged in.");
			} else {
				e1.put("response", "incorrect_password");
				Service.logInfo(username + " wrong password.");
			}
		}else{
			e1.put("response", "registered_user");
			users.put(username, password);
			Service.logInfo("Registered " + username + " " + password);
		}

		try {
			connection.sendEvent(e1);
		} catch (IOException e3) {
			Service.logError("Error Sending Event: " + e3.toString());
		}
		return true;
	}
}
