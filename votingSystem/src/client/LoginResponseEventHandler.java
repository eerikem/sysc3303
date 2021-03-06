package client;

import java.util.ArrayList;

import common.Connection;
import common.Event;
import common.EventHandler;
import common.Voter;
import common.Service;
import common.Person.Candidate;
import client.Client;

public class LoginResponseEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {

		// Get the connection that this Handler was called on
		Connection connection = (Connection) e.get("connection");
		Client client = (Client) connection.getService();
		client.clientTimeout.interrupt();
		String response = (String) e.get("response");

		switch (response) {
		case "blank_user":
			Service.logInfo("Please enter a username");
			break;
		case "login_success":
			client.setPerson((Voter) e.get("person"));
			client.setCandidates((ArrayList<Candidate>) e.get("candidates"));
			client.vote();
			break;
		case "incorrect_password":
			Service.logInfo("Bad password");
			break;
		case "registered_user":
			Service.logInfo("registered with server.");
			break;
		case "user_unregistered":
			Service.logInfo("Please register with server.");
			client.enableReg();
			break;
		case "already_voted":
			Service.logInfo("You have already voted.");
			break;
		case "district_mismatch":
			Service.logInfo("Attempt to log into incorrect district");
			break;
		case "election_not_started":
			Service.logInfo("Election has not started");
			break;
		case "election_ended":
			Service.logInfo("Election has ended");
			break;	
		default:
			Service.logWarn("Unknown Login Response: " + response);
			break;
		}

		return true;
	}
}
