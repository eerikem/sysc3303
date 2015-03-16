package common;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Address implements Serializable{
	private static final long serialVersionUID = 1L;
	public InetAddress ip;
	public int port;
	
	public Address(String host, int port){
		try {
			this.ip = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.port = port;
	}
	
	public Address(InetAddress host, int port){
		this.ip = host;
		this.port = port;
	}
	
	public boolean equals(Address a){
		if(ip.getAddress()== a.ip.getAddress()&&port == a.port)
			return true;
		return false;
	}
}
