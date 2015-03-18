package common;

import common.Connection;
import common.Event;
import common.EventHandler;
import common.Service;


public class KillEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {

		Connection connection = (Connection) e.get("connection");
		Service.logInfo("Killing connection to Client "+connection.socket.getPort());
		connection.stop();
		return true;
	}

}
