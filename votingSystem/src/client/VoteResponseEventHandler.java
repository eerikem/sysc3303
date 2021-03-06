package client;

//import common.Connection;
import common.Connection;
import common.Service;
import common.Event;
import common.EventHandler;

public class VoteResponseEventHandler implements EventHandler{
	
	@Override
	public boolean handleEvent(Event e) {

		// Get the connection that this Handler was called on
		Connection connection = (Connection) e.get("connection");
		Client client = (Client) connection.getService();
		client.clientTimeout.interrupt();
		String response = (String) e.get("response");

		switch (response) {
		case "blank_vote":
			Service.logInfo("Please vote for a party");
			break;
		case "vote_success":
			Service.logInfo("Vote successfully recorded");
			break;
		case "already_voted":
			Service.logInfo("User has already voted");
			break;
		default:
			Service.logWarn("Unknown Vote Response " + response);
			break;
		}

		return true;
	}
}
