package voteserver;

import common.Event;
import common.EventHandler;
import common.Service;

public class UnknownServerEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {
		Service.logError("Server received an UNKNOWN Event: "+e.get("event"));
		return true;
	}

}
