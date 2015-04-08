package common.Person;

import java.io.Serializable;


public class Candidate implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String party;
	private String district;
	
	public Candidate(String name, String party, String district) {
		this.name = name;
		this.party = party;
		this.district = district;
	}
	
	public String getName(){
		return name;
	}
	public String getParty(){
		return party;
	}
	public String getDistrict(){
		return district;
	}
}
