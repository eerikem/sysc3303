package common;

import java.net.DatagramSocket;
import java.net.SocketException;

import common.Connection;

//Initiates Connection from Client to Server
public class Connector {

	protected Service service;

	public Connector(Service service) {
		this.service = service;
	}

	public Connection connect(String host, int port) throws SocketException{
		DatagramSocket socket = new DatagramSocket();
		Connection c = new Connection(socket, service);
		c.setDest(new Address(host,port));
		return c;
	}
}
