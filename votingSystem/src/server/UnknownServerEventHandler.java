package server;

import java.io.IOException;

import common.Connection;
import common.Event;
import common.EventHandler;
import common.Service;


public class UnknownServerEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {

		Connection connection = (Connection) e.get("connection");

		Event e1 = new Event("UNKNOWN");
		e1.put("event", e.getType());
		e1.put("response", "success");
		Service.logWarn("Server received unknown event: "+e.getType()+"\nAdd handler to server.cfg");

		try {
			connection.sendEvent(e1);
		} catch (IOException e3) {
			Service.logError("Error Sending Event: " + e3.toString());
		}

		return true;
	}

}
