package voteserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import common.Address;
import common.Connection;
import common.Event;
import common.Service;
import common.Person.Candidate;
import servercommon.Server;

public class MainServer extends Server {

	private static ConcurrentHashMap<String, Integer> votes;
	private static boolean votingEnabled;
	private int numberVotes;
	private MainServerGUI serverGUI;
	
	
	public MainServer(String file) {
		super(file, 9080);
		votingEnabled = false;
		numberVotes = 0;
		votes = new ConcurrentHashMap<>();
		serverGUI = new MainServerGUI(this);
		serverGUI.setVisible(true);
	}

	public static void main(String[] args) {

		// Default in case no args
		String cfgFile = "votingSystem/src/voteserver/server.cfg";
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-c")) {
				cfgFile = args[++i];
			}
		}

		// Create and start Server
		MainServer server = new MainServer(cfgFile);
		
		
		try {
			server.run();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void run() throws FileNotFoundException {
//		startElection();
		while (true) {
			try {
				Connection connection = acceptor.accept();
				//This connect is an arrayList
				connections.put(connection.getDest(), connection);
				Thread t = new Thread(connection);
	
				t.start();
			} catch (IOException e) {
				Service.logError("Server Connection Error");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startElection() throws FileNotFoundException
	{
		votingEnabled = true;
		SendCandidateList(ReadCandidateList());		
		try {
			Event e = new Event("STARTELECTION");
			for (Address key : connections.keySet()){
				connections.get(key).sendEvent(e);
			}
		} catch (IOException e) {
			Service.logError("Error Sending Event: " + e.toString());
		}
		PeriodicPostThread predict = new PeriodicPostThread(this);
		predict.start();
	}
	
	@SuppressWarnings("unused")
	public ArrayList<Candidate> ReadCandidateList() throws FileNotFoundException {
		ElectionCandidates elec = new ElectionCandidates("votingSystem/src/voteserver/log.txt");
		try {
			Event e = new Event("ANNOUNCECANDIDATES");
			e.put("votes", elec);
			for(Address key: connections.keySet()){
				connections.get(key).sendEvent(e);
			}
		} catch (IOException e) {
			Service.logError("Error Sending Event: " + e.toString());
		}
		return null; //elec.parseLog();
	}
	
	public void SendCandidateList( ArrayList<Candidate> cand) throws FileNotFoundException{
		//This is where we need to pass it over the wire and consume it via the district server 
		ArrayList<Candidate> candidates = ReadCandidateList();
		//define a new devent called candidate List 
//		for (Connection i : connections){
//			i.send(candidate);
//		}
		return;
	}
	
	public void stopElection(){
		
	}
	
	public boolean updateVotes(ConcurrentHashMap<String,Integer> v){
		
		for(String key: v.keySet()){
			if(votes.containsKey(key)){
				votes.put(key, votes.get(key) + v.get(key));
			}
			else{
				votes.put(key, v.get(key));
			}
			numberVotes += v.get(key);
			Service.logInfo(key + " now has " + votes.get(key) + " votes");
		}
		serverGUI.updateResults();
		return true;
		
	}
	
	public ConcurrentHashMap<String, Integer> getVotes() {
		return votes;
	}
	
	public boolean getVotingEnabled()
	{
		return votingEnabled;
	}
	
	public int getNumberVotes()
	{
		return numberVotes;
	}
	

}
