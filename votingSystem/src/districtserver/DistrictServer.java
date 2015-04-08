package districtserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import servercommon.Server;
import voteserver.ElectionCandidates;
import common.Connection;
import common.Connector;
import common.Voter;
import common.Event;
import common.Service;

public class DistrictServer extends Server {

	private ConcurrentHashMap<String, Voter> users;
	private ConcurrentHashMap<String, Integer> votesToUpdate;
	private ConcurrentHashMap<String, Integer> totalVotes;
	private static int DISTRICT_SERVER_PORT = 9090;
	private static int MAIN_SERVER_PORT = 9080;
	private static String DEFAULT_HOST_MAIN = "localhost";
	private Connector connector;
	private Connection mainConnection;
	private ElectionCandidates elec;
	
	protected DistrictTimeout districtTimeout;

	private boolean electionStart = false;
	private boolean electionStop = false;
	private int numVotes = 0;
	private static int MAX_NUM_VOTES = 2;


	public DistrictServer(String file) {
		super(file, DISTRICT_SERVER_PORT);
		Service.logInfo("binding to port "+DISTRICT_SERVER_PORT);
		connector = new Connector(this);
		this.users = new ConcurrentHashMap<String, Voter>();
		this.votesToUpdate = new ConcurrentHashMap<String,Integer>();
		this.totalVotes = new ConcurrentHashMap<String,Integer>();
	}

	public static void main(String[] args) {
		// Default in case no args
		String cfgFile = "votingSystem/src/districtserver/server.cfg";
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-c")) {
				cfgFile = args[++i];
			}
		}
		// Create and start Server
		DistrictServer server = new DistrictServer(cfgFile);
		server.run();
	}

	public void run() {
		try {
			mainConnection = connector.connect(DEFAULT_HOST_MAIN, MAIN_SERVER_PORT);
			Thread t = new Thread(mainConnection);
			t.start();
		} catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
		}
		while (true) {
			try {
				Connection connection = acceptor.accept();
				Thread t = new Thread(connection);
				connections.put(connection.getDest(), connection);
				Event a = new Event("ANNOUNCECANDIDATES");
				connection.sendEvent(a);
				t.start();
			} catch (IOException e) {
				Service.logError("Server Connection Error");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	

	public boolean recordVote(String vote){
		if(votesToUpdate.containsKey(vote)){
			votesToUpdate.put(vote, votesToUpdate.get(vote) + 1);
			totalVotes.put(vote, votesToUpdate.get(vote) + 1);
		}
		else{
			votesToUpdate.put(vote, 1);
			totalVotes.put(vote, 1);
		}
		numVotes++;
		Service.logInfo(votesToUpdate.get(vote) + " votes for "+ vote);
		if(numVotes >= MAX_NUM_VOTES){
			this.updateMainServer();
			numVotes = 0;
		}
		return true;
	}
	
	private boolean updateMainServer(){
		//send the main server all the contents of votesToUpdate hashmap 
		try {
			Event e = new Event("UPDATEVOTES");
			e.put("votes", votesToUpdate);
			
			sendEvent(e);
		} catch (IOException e) {
			Service.logError("Error Sending Event: " + e.toString());
		}
				
		//reset hashmap moved to serverUpdated
		
		return true;
	}
	

	public void serverUpdated(HashMap<String, Integer> updatedVotes)
	{
		for(String key: updatedVotes.keySet()){
			votesToUpdate.put(key, votesToUpdate.get(key)- updatedVotes.get(key));
		}
	}

	public void startElection(){
		electionStart = true;
	}
	
	public void stopElection(){
		electionStop = true;
	}
	
	public boolean getElectionStart(){
		return electionStart;
	}
	
	public boolean getElectionStop(){
		return electionStop;
	}
	
	public Connection getMainConnection(){
		return mainConnection;
	}
	
	public ConcurrentHashMap<String, Voter> getUsers(){
		return users;
	}
	
	private void sendEvent(Event e) throws IOException
	{
		mainConnection.sendEvent(e);
		districtTimeout = new DistrictTimeout();
		districtTimeout.start();
	}
	
	public ElectionCandidates getCandidates(){
		return elec;
	}

	public void setCandidate(ElectionCandidates elec2) {
		elec = elec2;
	}
}
