package common;

import java.io.Serializable;

public class Person implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String name;
	public String username;
	public String password;
	public Address district;
	public boolean voted;
	
	public Person(String name, String username, String password){
		this.name = name;
		this.username = username;
		this.password = password;
		this.voted = false;
	}
	
	public boolean hasVoted()
	{
		return voted;
	}
	
}
