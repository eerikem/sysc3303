package voteserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ElectionCandidates {
	
	String log;
	ArrayList<Candidate> candidate;
	public ElectionCandidates(String logfile) throws FileNotFoundException{
		log = logfile;
		parseLog();
	}
	
	public ArrayList<Candidate> parseLog() throws FileNotFoundException {
	       //Scanner Example - read file line by line in Java using Scanner
        FileInputStream fis = new FileInputStream(log);
        Scanner scanner = new Scanner(fis);
        while(scanner.hasNextLine()){
            System.out.println(scanner.nextLine());
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
}
