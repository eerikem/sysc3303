package districtserver;

import common.Event;
import common.EventHandler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import servercommon.Server;
import voteserver.ElectionCandidates;
import common.Address;
import common.Connection;
import common.Connector;
import common.Voter;
import common.Event;
import common.Service;
import common.Person.Candidate;
import districtserver.DistrictServer;

public class GetCandidateEventHandler implements EventHandler{

	@Override
	public boolean handleEvent(Event e) throws IOException {

		//get the connection from the event
		Connection connection = (Connection) e.get("connection");
		//get the parent server from that connections
		DistrictServer server = (DistrictServer)connection.getService();
		// Get the connection that this Handler was called on
		
		ElectionCandidates elec = server.getCandidates();
		Event a = new Event("ANNOUNCECANDIDATES");
		a.put("can", server.getCandidates());
		connection.sendEvent(a);
		return true;
		
//		
//		
//		
//		ConcurrentHashMap<Address, Connection> connections = server.getConnections();
//		if (connections == null){
//			Service.logError("Not working");
//		}
//		else {
//			Service.logError("Size: "+ connections.size() );
//			for(Address key: connections.keySet()){
//				Service.logError("IP: " + key.port);
//			}
//		}
//		if (elec == null){
//			System.out.println("Big Problem");
//			return false;
//		}
//		Event p = new Event("ANNOUNCECANDIDATES");
//		p.put("can", elec);
//		ArrayList<Candidate> n = elec.getCandidate();
//		System.out.println("Event Log: " + elec.log);
//		if (n == null){
//			System.out.println("ArrayList is Null");
//			return false;
//		}
//		for (Candidate i : elec.getCandidate()){
//			System.out.println(i.getName());
//		}
//		for(Address key: connections.keySet()){
//			System.out.println("Sending to connection" + key.ip);
//			connections.get(key).sendEvent(p);
//		}
//		return false;
	}
}
