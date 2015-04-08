package voteserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import common.Person.Candidate;

public class ElectionCandidates implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public String log;
	private ArrayList<Candidate> candidates;
	private HashMap<String,ArrayList<Candidate>> districts; 
	
	public ElectionCandidates(String logfile) {
		log = logfile;
		districts = new HashMap<String,ArrayList<Candidate>>();
		candidates = new ArrayList<Candidate>();
		try {
			parseLog();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(Candidate c: candidates){
			if(districts.containsKey(c.getDistrict())){
				districts.get(c.getDistrict()).add(c);
			} else {
				ArrayList<Candidate> list = new ArrayList<Candidate>();
				list.add(c);
				districts.put(c.getDistrict(),list);
			}
		}
	}
	
	public ArrayList<Candidate> parseLog() throws FileNotFoundException {
	       //Scanner Example - read file line by line in Java using Scanner
        FileInputStream fis = new FileInputStream(log);
        Scanner scanner = new Scanner(fis);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String []ret = line.split(",");
            candidates.add(new Candidate(ret[0].trim(),ret[1].trim(),ret[2].trim()));            
        }
        scanner.close();
		return null;
	}

	public ArrayList<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidate(ArrayList<Candidate> candidates) {
		this.candidates = candidates;
	}
	
	public HashMap<String,ArrayList<Candidate>> getDistricts(){
		return districts;
	}
}
