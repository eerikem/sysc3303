package common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

import client.Client;
import districtserver.DistrictServer;

//Handles all the connection specific UDP logic
public class Connection implements Runnable, Serializable {

	private static final long serialVersionUID = 1L;
	protected DatagramSocket socket;
	protected Service service;
	protected boolean running;
	private ObjectInputStream ois;

	public Connection(DatagramSocket socket, Service service) {
		this.socket = socket;
		this.service = service;
		this.running = false;
	}

	public Event getEvent() throws IOException, ClassNotFoundException {
		byte[] buffer = new byte[1000];
		DatagramPacket request = new DatagramPacket(buffer, buffer.length);
		socket.receive(request);
		Service.logInfo("Received packet.");
		ois = new ObjectInputStream(new ByteArrayInputStream(request.getData()));
		Event e = (Event) ois.readObject();
		e.put("connection", this);
		Service.logInfo("Received event: "+ e.getType());
		return e;
	}
	
	public void sendEvent(Event e) throws IOException {
		byte[] bytes = packEvent(e);
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
		socket.send(packet);
		Service.logInfo("Sent event " + e.getType() + " to " + socket.getPort());
	}

	public void sendEvent(Event e,Address dest) throws IOException{
		byte[] bytes = packEvent(e);
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length,dest.ip,dest.port);
		socket.send(packet);
		Service.logInfo("Sent event " + e.getType() + " to " + dest.port);
	}
	
	public byte[] packEvent(Event e) throws IOException {
		e.put("source",
				new Address(InetAddress.getLocalHost(), socket.getLocalPort()));

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(e);
		return bos.toByteArray();
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			try {
				final Event e = getEvent();
				service.getThreadPool().execute(new Runnable() {
					public void run() {
						// System.out.println(Thread.currentThread().toString());
						service.getReactor().dispatch(e);
					}
				});
			} catch (IOException e) {
				if (service.getClass() == DistrictServer.class) {
					ConcurrentHashMap<Address, Connection> connections = ((DistrictServer) service)
							.getConnections();
					for (Address key : connections.keySet()) {
						if (connections.get(key) == this) {
							Service.logError(key + " has disconnected");
							connections.get(key).stop();
							connections.remove(key);
							break;
						}
					}
				} else if (service.getClass() == Client.class) {
//					if (e instanceof java.net.SocketException
//							|| e instanceof java.io.EOFException)
//						((Client) service).reconnect("Server Disconnected");
				}
			} catch (ClassNotFoundException e) {
				Service.logError(e + "");
			}
		}
	}

	public void setDest(Address dest){
		socket.connect(dest.ip,dest.port);
	}

	public void kill() {
		Event e = new Event("KILL");
		
		try {
			sendEvent(e);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		stop();
	}
	
	public void stop() {
		running = false;
		
		if (socket != null) {
			socket.close();
			socket = null;
		}
	}

	public Service getService() {
		return service;
	}
}
