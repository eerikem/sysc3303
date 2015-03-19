package common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPConnection {
	private DatagramSocket socket;
	private int port;
	protected Service service;
	
	public UDPConnection(int port, Service service){
		this.service = service;
		this.port = port;
		this.socket = null;
		
	}
	
	public void start(){

		try {
			socket = new DatagramSocket(port);
			Service.logInfo("Started Listening on port: " + port);
		} catch (SocketException e) {
			Service.logError("Error Starting Server: " + e);
			System.exit(0);
		}
	}
	
	public void sendEvent(Event e) throws IOException{
		e.put("sourceAddress", socket.getInetAddress());
		e.put("srcPort", socket.getLocalPort());

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(e);
		byte[] bytes = bos.toByteArray();
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length,
				(InetAddress)e.get("sourceAddress"), (Integer)e.get("srcPort"));
		socket.send(packet);
	}
}
