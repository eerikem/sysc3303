package client;

import common.Connection;
import common.Event;
import common.EventHandler;
import common.Service;
import client.Client;

public class RegisterResponseEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {

		// Get the connection that this Handler was called on
		Connection connection = (Connection) e.get("connection");
		Client client = (Client) connection.getService();

		String response = (String) e.get("response");

		switch (response) {
		case "already_registered":
			Service.logInfo("User already registered");
			break;
		case "registration_success":
			Service.logInfo("registered with server.");
			//TODO seperate login GUI?
			//client.enableLogin();
			break;
		default:
			Service.logWarn("Unknown Login Response");
			break;
		}

		return true;
	}
}
