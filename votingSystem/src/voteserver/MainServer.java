package voteserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import common.Address;
import common.Connection;
import common.Event;
import common.Service;
import common.Person.Candidate;
import servercommon.Server;

public class MainServer extends Server {

	private static ConcurrentHashMap<Candidate, Integer> votes;
	private static boolean votingEnabled;
	private int numberVotes;
	private MainServerGUI serverGUI;
<<<<<<< HEAD
	private static ElectionCandidates elec; 
	
=======
	private ElectionCandidates election;
	private HashMap<Connection, String> districts;

>>>>>>> origin/demo
	public MainServer(String file) {
		super(file, 9080);
		votingEnabled = false;
		numberVotes = 0;
		ElectionCandidates elec = null;
		votes = new ConcurrentHashMap<>();
		districts = new HashMap<Connection, String>();
		election = new ElectionCandidates("votingSystem/src/voteserver/log.txt");
		serverGUI = new MainServerGUI(this);
		serverGUI.setVisible(true);
	}

	public ElectionCandidates getCandidates(){
		return elec;
	}
	public static void main(String[] args) throws FileNotFoundException {

		// Default in case no args
		String cfgFile = "votingSystem/src/voteserver/server.cfg";
<<<<<<< HEAD
		elec = new ElectionCandidates("votingSystem/src/voteserver/log.txt");
=======
>>>>>>> origin/demo

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
			e.printStackTrace();
		}
	}

	public void run() throws FileNotFoundException {
		while (true) {
			try {
				Connection connection = acceptor.accept();
				// This connect is an arrayList
				connections.put(connection.getDest(), connection);
				assignDistrict(connection);
				Thread t = new Thread(connection);
				t.start();
			} catch (IOException e) {
				Service.logError("Server Connection Error");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void assignDistrict(Connection c) {
		if (connections.size() <= election.getDistricts().size()) {
			ArrayList<String> keys = new ArrayList<String>(election
					.getDistricts().keySet());
			String key = keys.get(connections.size() - 1);
			districts.put(c, key);
		} else {
			Service.logError("Too many districts connected");
			try {
				Event e = new Event("KILL");
				e.put("district", "stop_running");
				c.sendEvent(e);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void startElection() throws FileNotFoundException {
		votingEnabled = true;
		for (Address key : connections.keySet()) {
			Connection c = connections.get(key);
			sendStartEvent(c);
		}
		PeriodicPostThread predict = new PeriodicPostThread(this);
		predict.start();
	}
<<<<<<< HEAD
	
	@SuppressWarnings("unused")
	public ArrayList<Candidate> ReadAndSendCandidateList() throws FileNotFoundException {
		try {
			Event e = new Event("ANNOUNCECANDIDATES");
			e.put("can", this.elec);
			for(Address key: connections.keySet()){
				connections.get(key).sendEvent(e);
			}
		} catch (IOException e) {
			Service.logError("Error Sending Event: " + e.toString());
=======

	private void sendStartEvent(Connection c) {
		Event e = new Event("STARTELECTION");
		e.put("candidates", election.getDistricts().get(districts.get(c)));
		try {
			c.sendEvent(e);
		} catch (IOException e1) {
			e1.printStackTrace();
>>>>>>> origin/demo
		}
	}

	public void stopElection() {
		votingEnabled = false;

		try {
			Event e = new Event("STOPELECTION");
			for (Address key : connections.keySet()) {
				connections.get(key).sendEvent(e);
			}
		} catch (IOException e) {
			Service.logError("Error Sending Event: " + e.toString());
		}
	}

	public boolean updateVotes(HashMap<Candidate, Integer> hashMap) {
		for (Candidate key : hashMap.keySet()) {
			for (Candidate key2 : election.getCandidates()) {
				if (key.getName().equals(key2.getName())) {
					if (votes.contains(key2)) {
						votes.put(key2, votes.get(key2) + hashMap.get(key));
					} else {
						votes.put(key2, hashMap.get(key));
					}
					numberVotes += hashMap.get(key);
					Service.logInfo(key.getName() + " now has "
							+ votes.get(key2) + " votes");
				}
			}
		}
		serverGUI.updateResults();
		return true;

	}

	public void sendCandidates2() {
		// Send the candidate list to all districts (connections)
		/*
		 * Event e1 = new Event("STARTCOUNTING"); e1.put("candidates",
		 * candidates);
		 * 
		 * for(Connection con: connections.values()) { try { con.sendEvent(e1);
		 * } catch (IOException e3) { Service.logError("Error Sending Event: " +
		 * e3.toString()); }
		 */
	}

	public ConcurrentHashMap<Candidate, Integer> getVotes() {
		return votes;
	}

	public String getDistrict(Connection c){
		return districts.get(c);
	}
	
	public boolean getVotingEnabled() {
		return votingEnabled;
	}

	public int getNumberVotes() {
		return numberVotes;
	}

	public ArrayList<String> getParties() {
		ArrayList<String> parties = new ArrayList<String>();
		for (Candidate c : election.getCandidates()) {
			if (!parties.contains(c.getParty())) {
				parties.add(c.getParty());
			}
		}
		return parties;
	}

	public ElectionCandidates getElection() {
		return election;
	}

}
