package voteserver;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import common.Address;
import common.Connection;
import common.Event;
import common.Service;
import servercommon.Server;

public class MainServer extends Server {

	private static ConcurrentHashMap<String, Integer> votes;
	private static boolean votingEnabled;
	private int numberVotes;
	
	
	public MainServer(String file) {
		super(file, 9080);
		votingEnabled = false;
		numberVotes = 0;
		votes = new ConcurrentHashMap<>();
	}

	public static void main(String[] args) {

		// Default in case no args
		String cfgFile = "votingSystem/src/voteserver/server.cfg";
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-c")) {
				cfgFile = args[++i];
			}
		}

		// Create and start Server
		MainServer server = new MainServer(cfgFile);
		
		
		server.run();
		
	}

	public void run() {
		startElection();
		while (true) {
			try {
				Connection connection = acceptor.accept();
				connections.put(connection.getDest(), connection);
				Thread t = new Thread(connection);
				t.start();
			} catch (IOException e) {
				Service.logError("Server Connection Error");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startElection()
	{
		votingEnabled = true;
		/*try {
			Event e = new Event("STARTELECTION");
			for (Address key : connections.keySet()){
				connections.get(key).sendEvent(e);
			}
		} catch (IOException e) {
			Service.logError("Error Sending Event: " + e.toString());
		}*/
		PeriodicPostThread predict = new PeriodicPostThread(this);
		predict.start();
	}
	
	public boolean updateVotes(ConcurrentHashMap<String,Integer> v){
		
		for(String key: v.keySet()){
			if(votes.containsKey(key)){
				votes.put(key, votes.get(key) + v.get(key));
			}
			else{
				votes.put(key, v.get(key));
			}
			numberVotes += v.get(key);
			Service.logInfo(key + " now has " + votes.get(key) + " votes");
		}
		return true;
		
	}
	
	public ConcurrentHashMap<String, Integer> getVotes() {
		return votes;
	}
	
	public boolean getVotingEnabled()
	{
		return votingEnabled;
	}
	
	public int getNumberVotes()
	{
		return numberVotes;
	}
	

}
