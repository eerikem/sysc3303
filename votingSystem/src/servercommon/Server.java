package servercommon;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import common.*;

public abstract class Server extends Service {

	private String DEFAULT_cfgFILE;
	private static int PORT;
	protected Acceptor acceptor;

	protected static ConcurrentHashMap<Address, Connection> connections;
	protected static Connection connection;
	
	public Server(String file, int port)
	{
		super(file);
		PORT = port;
		
		this.acceptor = new Acceptor(PORT, this);

		this.connections = new ConcurrentHashMap<Address, Connection>();		
	}
	
	public void run() {
		try {
			connection = acceptor.accept();
			Thread t = new Thread(connection);
			t.start();
		} catch (IOException e) {
			Service.logError("Server Connection Error");
		}
	}

	public ConcurrentHashMap<Address, Connection> getConnections() {
		return connections;
	}

	public String getConfig() {
		return DEFAULT_cfgFILE;
	}

	public void setConfig(String dEFAULT_cfgFILE) {
		DEFAULT_cfgFILE = dEFAULT_cfgFILE;
	}
	
	
}
