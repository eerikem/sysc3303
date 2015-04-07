package voteserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

import common.Person.Candidate;

public class ElectionCandidates implements Serializable {
	
	public String log;
	private ArrayList<Candidate> candidate;
	public ElectionCandidates(String logfile) throws FileNotFoundException{
		log = logfile;
		candidate = new ArrayList<Candidate>();
		parseLog();
	}
	
	public ArrayList<Candidate> parseLog() throws FileNotFoundException {
	       //Scanner Example - read file line by line in Java using Scanner
        FileInputStream fis = new FileInputStream(log);
        Scanner scanner = new Scanner(fis);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String []ret = line.split(",");
            candidate.add(new Candidate(ret[0],ret[1],ret[2]));            
        }
        scanner.close();
		return null;
	}
	public static void main(String args[]){
		try {
			ElectionCandidates el = new ElectionCandidates("votingSystem/src/voteserver/log.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public ArrayList<Candidate> getCandidate() {
		return candidate;
	}

	public void setCandidate(ArrayList<Candidate> candidate) {
		this.candidate = candidate;
	}
}