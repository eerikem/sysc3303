package voteserver;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import common.Connection;
import common.Service;
import servercommon.Server;

public class MainServer extends Server {

	private ConcurrentHashMap<String, Integer> votes;
	private static String cfgFile = "votingSystem/src/voteserver/server.cfg";
	
	public MainServer(String file) {
		super(file, 9080);

		this.votes = new ConcurrentHashMap<>();
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
		}
	}

}
