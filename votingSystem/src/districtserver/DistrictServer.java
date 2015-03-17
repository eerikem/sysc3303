package districtserver;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import servercommon.Server;
import common.Connection;
import common.Connector;
import common.Service;

public class DistrictServer extends Server {

	private ConcurrentHashMap<String, String> users;
	private static int MAIN_SERVER_PORT = 9090;
	private Connector connector;
	private Connection mainConnection;

	public DistrictServer(String file) {
		super(file, 9090);
		
		connector = new Connector(this);
		
		this.users = new ConcurrentHashMap<String, String>();
	}

	public static void main(String[] args) {

		// Default in case no args
		String cfgFile = "votingSystem/src/server/server.cfg";

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
			Connection connection = acceptor.accept();
			Thread t = new Thread(connection);
			t.start();
		} catch (IOException e) {
			Service.logError("Server Connection Error");
		}
	}
	
	public ConcurrentHashMap<String, String> getUsers(){
		return users;
	}
}
