package common;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

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
		c.setDest(new Address(host,port));
		
		Event e = new Event("CON_REQ");
		c.sendEvent(e);
		e = c.getEvent();
		while(e.getType()!="CON_SUC"){
			e = c.getEvent();
		}
		c.setDest((Address)e.get("source"));
		
		return c;
	}
}
