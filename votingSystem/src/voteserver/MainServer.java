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
	
	
	public MainServer(String file) {
		super(file, 9080);

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
		try {
			Connection connection = acceptor.accept();
			Thread t = new Thread(connection);
			t.start();
		} catch (IOException e) {
			Service.logError("Server Connection Error");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public ConcurrentHashMap<String, Integer> getVotes() {
		return votes;
	}
	

}
