package common;

import java.io.IOException;
import java.net.DatagramSocket;

import common.Connection;

//Accepts Connections from Clients
//TODO complete concurrent design
public class Acceptor {

	protected DatagramSocket serverSocket;
	protected int port;
	protected Service service;
	private Connection myConnection;

	public Acceptor(int port, Service service) {
		this.service = service;
		this.port = port;
		this.serverSocket = null;
		start();
	}

	public void start() {
		try {
			serverSocket = new DatagramSocket(port);
			myConnection = new Connection(serverSocket, service);
			Service.logInfo("Server Started");
		} catch (IOException e) {
			Service.logError("Error Starting Server: " + e);
			System.exit(0);
		}
	}

	public Connection accept() throws IOException, ClassNotFoundException {
		Event e = myConnection.getEvent();
		while (!e.getType().equals("CON_REQ")) {
			Service.logWarn("Received non-connection related event "
					+ e.getType() + " on dedicated server socket " + port);
			e = myConnection.getEvent();
		}
		Address source = (Address) e.get("source");
		DatagramSocket socket = new DatagramSocket();
		socket.connect(source.ip, source.port);
		
		e = new Event("CON_SUC");
		Service.logInfo("Spawning new connection to "+source.port);
		Connection c = new Connection(socket, service);
		c.sendEvent(e,source);
		return c;
	}

}
