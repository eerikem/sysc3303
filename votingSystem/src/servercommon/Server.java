package servercommon;

import java.util.concurrent.ConcurrentHashMap;

import common.*;

public abstract class Server extends Service {

	private String DEFAULT_cfgFILE;
	private int PORT;
	protected Acceptor acceptor;

	protected ConcurrentHashMap<Address, Connection> connections;
	protected Connection connection;
	
	public Server(String file, int port)
	{
		super(file);
		PORT = port;
		
		this.acceptor = new Acceptor(PORT, this);

		this.connections = new ConcurrentHashMap<Address, Connection>();		
	}
	
	public abstract void run();

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
