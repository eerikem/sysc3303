package server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import common.Acceptor;
import common.Address;
import common.Connection;
import common.Service;

public class Server extends Service {

	private static String DEFAULT_cfgFILE = "votingSystem/src/server/server.cfg";
	private static int PORT = 9090;
	private Acceptor acceptor;

	private ConcurrentHashMap<Address, Connection> connections;
	private ConcurrentHashMap<String, String> users;
	private ConcurrentHashMap<String, Integer> votesToUpdate;
	private ConcurrentHashMap<String, Integer> totalVotes;

	public Server(String file) {
		super(file);

		this.acceptor = new Acceptor(PORT, this);

		this.connections = new ConcurrentHashMap<Address, Connection>();
		this.users = new ConcurrentHashMap<String, String>();
		this.votesToUpdate = new ConcurrentHashMap<String,Integer>();
		this.totalVotes = new ConcurrentHashMap<String,Integer>();
	}

	public static void main(String[] args) {

		// Default in case no args
		String cfgFile = DEFAULT_cfgFILE;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-c")) {
				cfgFile = args[++i];
			}
		}

		// Create and start Server
		Server server = new Server(cfgFile);
		server.run();
	}

	public void run() {
		try {
			Connection connection = acceptor.accept();
			Thread t = new Thread(connection);
			t.start();
		} catch (IOException e) {
			Service.logError("Server Connection Error");
		}
	}

	public ConcurrentHashMap<Address, Connection> getConnections() {
		return connections;
	}
	
	public ConcurrentHashMap<String, String> getUsers(){
		return users;
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
		return true;
	}
	
	private boolean updateMainServer(){
		//send the main server all the contents of votesToUpdate hashmap 
		
		
		
		//reset hashmap
		for(String key: votesToUpdate.keySet()){
			votesToUpdate.put(key, 0);
		}
		
		return true;
	}
}
