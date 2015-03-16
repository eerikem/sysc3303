package client;

import common.Connection;
import common.Event;
import common.EventHandler;

public class UnknownClientEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {

		// Get the connection/client that this Handler was called on
		Connection connection = (Connection) e.get("connection");
		Client client = (Client) connection.getService();

		client.displayError("Server received an UNKNOWN Event: "+e.get("event"));

		return true;
	}

}
