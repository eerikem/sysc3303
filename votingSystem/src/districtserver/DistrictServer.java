package districtserver;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import servercommon.Server;
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
		Service.logInfo(votesToUpdate.get(vote) + " votes for "+ vote);
		this.updateMainServer();
		return true;
	}
	
	private boolean updateMainServer(){
		//send the main server all the contents of votesToUpdate hashmap 
		try {
			Event e = new Event("UPDATEVOTES");
			e.put("votes", votesToUpdate);
			
			mainConnection.sendEvent(e);
		} catch (IOException e) {
			Service.logError("Error Sending Event: " + e.toString());
		}
				
		//reset hashmap
		for(String key: votesToUpdate.keySet()){
			votesToUpdate.put(key, 0);
		}
		
		return true;
	}
	
	public Connection getMainConnection(){
		return mainConnection;
	}
	
	public ConcurrentHashMap<String, Voter> getUsers(){
		return users;
	}
}
