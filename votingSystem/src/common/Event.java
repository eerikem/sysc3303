package common;

import java.io.Serializable;
import java.util.HashMap;

public class Event implements Serializable{
	private static final long serialVersionUID = 1L;

	String type;
	
	HashMap<String, Serializable> map;

	public Event(String type) {
		this.type = type;
		this.map = new HashMap<String, Serializable>();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashMap<String, Serializable> getMap() {
		return map;
	}

	public void setMap(HashMap<String, Serializable> map) {
		this.map = map;
	}

	public Serializable get(String key) {
		return map.get(key);
	}

	public void put(String key, Serializable value) {
		map.put(key, value);
	}
}
