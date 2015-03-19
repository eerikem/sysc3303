package common;

import java.io.IOException;
import java.net.DatagramSocket;

import common.Connection;

//Initiates Connection from Client to Server
public class Connector {

	protected Service service;

	public Connector(Service service) {
		this.service = service;
	}

	public Connection connect(String host, int port) throws ClassNotFoundException, IOException{
		DatagramSocket socket = new DatagramSocket();
		Connection c = new Connection(socket, service);
		Service.logInfo("Listening on port "+socket.getLocalPort());
		Address server = new Address(host,port);
		
		Event e = new Event("CON_REQ");
		c.sendEvent(e,server);
		e = c.getEvent();
		while(!e.getType().equals("CON_SUC")){
			e = c.getEvent();
		}
		server = (Address)e.get("source");
		c.setDest(server);
		Service.logInfo("Set connection socket to "+server.port);
		return c;
	}
}
