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

	public Server(String file) {
		super(file);

		this.acceptor = new Acceptor(PORT, this);

		this.connections = new ConcurrentHashMap<Address, Connection>();
		this.users = new ConcurrentHashMap<String, String>();
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
}
