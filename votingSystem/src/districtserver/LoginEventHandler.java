package districtserver;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import common.Connection;
import common.Event;
import common.EventHandler;
import common.Voter;
import common.Service;

public class LoginEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {
		// Get the connection that this Handler was called on
		Connection connection = (Connection) e.get("connection");
		DistrictServer server = (DistrictServer) connection.getService();
		Boolean start = server.electionStarted();
		Boolean stop = server.electionStopped();
		ConcurrentHashMap<String, Voter> users = server.getUsers();

		String username = (String) e.get("username");
		String password = (String) e.get("password");

		// Send Login Reply
		Event e1 = new Event("LOGINREPLY");
		if (stop) {
			e1.put("response", "election_ended");
			Service.logInfo("Election has eneded");
		} else if (username == null || username.equals("")) {
			e1.put("response", "blank_user");
			Service.logWarn("Login attempt: blank user");
		} else if (users.containsKey(username)) {
			if (!password.equals(users.get(username).password)) {
				e1.put("response", "incorrect_password");
				Service.logInfo(username + " wrong password.");
			} else if (!start) {
				e1.put("response", "election_not_started");
				Service.logInfo("Attempted Login before start of election");
			} else if (users.get(username).hasVoted()) {
				e1.put("response", "already_voted");
				Service.logInfo(username + " already voted.");
			} else {
				e1.put("response", "login_success");
				e1.put("candidates", server.getCandidates());
				e1.put("person", users.get(username));
				Service.logInfo(username + " logged in.");
			}
		} else {
			e1.put("response", "user_unregistered");
			// users.put(username, password);
			Service.logInfo("Received login event from " + username);
		}

		try {
			connection.sendEvent(e1);
		} catch (IOException e3) {
			Service.logError("Error Sending Event: " + e3.toString());
		}
		return true;
	}
}
