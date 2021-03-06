package districtserver;

import java.io.IOException;

import common.Address;
import common.Connection;
import common.Event;
import common.EventHandler;
import common.Voter;
import common.Service;
import common.Person.Candidate;


public class VoteEventHandler implements EventHandler {

	@Override
	public boolean handleEvent(Event e) {
		
		// Get the connection that this Handler was called on
		Connection connection = (Connection) e.get("connection");
		DistrictServer server = (DistrictServer)connection.getService();
				
		Candidate vote = (Candidate) e.get("vote");
		
		Event e1 = new Event("VOTEREPLY");
		
		if(vote == null){
			e1.put("response", "blank_vote");
			Service.logWarn("Vote attempt: blank vote");
		}
		else if(server.electionStopped()){
			e1.put("response", "election_ended");
			Service.logInfo("Election has ended");
		}
		else{
			Voter voter = (Voter) e.get("person");
			if(server.getUsers().get(voter.username).voted){
				e1.put("response", "already_voted");
			}else{
				server.getUsers().get(voter.username).voted = true;
				e1.put("response", "vote_success");
				((DistrictServer) connection.getService()).recordVote(vote);
				Service.logInfo("Registered vote for " + vote);
			}
			
		}
			
		
		
		try {
			connection.sendEvent(e1);
		} catch (IOException e3) {
			Service.logError("Error Sending Event: " + e3.toString());
		}
				
		
		return true;
	}
	
}
