package voteserver;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import client.ClientUI;
import common.Address;
import common.Connection;
import common.Event;
import common.Service;
import servercommon.Server;

public class MainServer extends Server {

	private static ConcurrentHashMap<String, Integer> votes;
	private MainServerGUI serverGUI;
	
	
	public MainServer(String file) {
		super(file, 9080);

		votes = new ConcurrentHashMap<>();
		serverGUI = new MainServerGUI(this);
		serverGUI.setVisible(true);
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
		try {
			Event e = new Event("STARTELECTION");
			for (Address key : connections.keySet()){
				connections.get(key).sendEvent(e);
			}
		} catch (IOException e) {
			Service.logError("Error Sending Event: " + e.toString());
		}
	}
	
	public void stopElection(){
		
	}
	
	public boolean updateVotes(ConcurrentHashMap<String,Integer> v){
		
		for(String key: v.keySet()){
			if(votes.containsKey(key)){
				votes.put(key, votes.get(key) + v.get(key));
			}
			else{
				votes.put(key, v.get(key));
			}
			serverGUI.updateResults();
			Service.logInfo(key + " now has " + votes.get(key) + " votes");
		}
		return true;
		
	}
	
	public ConcurrentHashMap<String, Integer> getVotes() {
		return votes;
	}
	

}
