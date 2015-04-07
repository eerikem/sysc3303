package client;

import common.Connection;
import common.Event;
import common.EventHandler;
import common.Person;
import common.Service;
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
			client.setPerson((Person) e.get("person"));
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
		default:
			Service.logWarn("Unknown Login Response");
			break;
		}

		return true;
	}
}
