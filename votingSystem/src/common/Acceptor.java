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

	public Acceptor(int port, Service service) {
		this.service = service;
		this.port = port;
		this.serverSocket = null;

		start();
	}

	public void start() {
		try {
			serverSocket = new DatagramSocket(port);
			Service.logInfo("Server Started");
		} catch (IOException e) {
			Service.logError("Error Starting Server: " + e);
			System.exit(0);
		}
	}

	public Connection accept() throws IOException{
		Connection c = new Connection(serverSocket, service);
	return c;
	}
}
