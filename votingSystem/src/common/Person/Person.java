package common.Person;

import java.io.Serializable;

public abstract class Person implements Serializable {
	
	/**
	 * Person is the highest class which users, candidates, newspeople or any other human can extend from
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String entity;
	public String key;
	//Very generically determine their name, an entity to define them, and a KeyCode (i.e. A City, or a Password)
	//A key value pair with a user tag "name", or user name like
	public Person(String Name, String Entity, String KeyCode){
		name = Name;
		entity = Entity;
		key = KeyCode;
	}
	
	public String getName(){
		return name;
	}
	
	public String getEntity(){
		return entity;
	}
	public String getKey(){
		return key;
	}
}
