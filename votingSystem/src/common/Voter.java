package common;

import java.io.Serializable;

public class Voter implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String name;
	public String username;
	public String password;
	public String district;
	public boolean voted;
	
	public Voter(String name, String username, String password, String district){
		this.name = name;
		this.username = username;
		this.password = password;
		this.voted = false;
		this.district = district;
	}
	
	public boolean hasVoted()
	{
		return voted;
	}
	
}
