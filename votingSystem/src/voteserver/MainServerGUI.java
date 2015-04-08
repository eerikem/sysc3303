package voteserver;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import common.Service;
import common.Person.Candidate;
import client.Client;



public class MainServerGUI extends JFrame implements ActionListener{
	private HashMap<Candidate,JLabel> candidateNames;
	private HashMap<String,JLabel> candidateResults;
	private HashMap<String,JLabel> partyResults;
	private HashMap<String,JLabel> partyNames;
	private MainServer server;
	private JPanel resultsPanel;
	private JPanel buttonPanel;
	private JButton startElectionButton;
	private JButton stopElectionButton;
	private HashMap<String,JPanel> districtPanels;
	
	public MainServerGUI(MainServer server) {
		super("Results Screen");
		setLayout(new BorderLayout());
		this.server = server;
		initResultsPanel(server.getElection());
		add(resultsPanel, BorderLayout.SOUTH);
		//button panel
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		startElectionButton = new JButton("Start Election");
		stopElectionButton = new JButton("Stop Election");
		buttonPanel.add(startElectionButton);
		startElectionButton.addActionListener(this);
		buttonPanel.add(stopElectionButton);
		stopElectionButton.addActionListener(this);
		add(buttonPanel, BorderLayout.NORTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(300, 300);
		setSize(300,200);
		setResizable(true);
		add(resultsPanel);
		WindowListener exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
			}
		};
		addWindowListener(exitListener);
}
	
	private void initResultsPanel(ElectionCandidates electionCandidates){
		resultsPanel = new JPanel();
<<<<<<< HEAD
		resultsPanel.setLayout(new GridLayout(tmp.length,2));
		candidates = new ArrayList<String>(Arrays.asList(tmp));
		candidateLabel = new ArrayList<JLabel>();
		candidateResults = new ArrayList<JLabel>();
		ArrayList<Candidate> cand = server.getCandidates().getCandidate();
		int i = 0;
		for (Candidate c : cand) {
			candidateLabel.add(new JLabel(c.getName() + " " + c.getEntity()));
			resultsPanel.add(candidateLabel.get(i));
			candidateResults.add(new JLabel("null"));
			resultsPanel.add(candidateResults.get(i));
			i++;
=======
		resultsPanel.setLayout(new GridLayout(server.getParties().size(), 2));

		candidateNames = new HashMap<Candidate, JLabel>();
		candidateResults = new HashMap<String, JLabel>();
		partyResults = new HashMap<String, JLabel>();
		partyNames = new HashMap<String, JLabel>();
		
		districtPanels = new HashMap<String,JPanel>();
		
		for(Candidate c : electionCandidates.getCandidates()){
			candidateNames.put(c, new JLabel(c.getName()+" "+c.getParty()));
			candidateResults.put(c.getName(), new JLabel("0"));
		}
		
		for(String party : server.getParties()){
			partyNames.put(party, new JLabel(party));
			partyResults.put(party, new JLabel("0"));
		}
		
		HashMap<String, ArrayList<Candidate>> districts = electionCandidates.getDistricts();
		for(String district: districts.keySet()){
			JPanel panel = new JPanel();
			ArrayList<Candidate> candidates = districts.get(district);
			panel.setLayout(new GridLayout(candidates.size(), 2));
			for(Candidate c:candidates){
				panel.add(candidateNames.get(c));
				panel.add(candidateResults.get(c.getName()));
			}
			districtPanels.put(district,panel);
		}
		
		for (String party: server.getParties()) {
			resultsPanel.add(partyNames.get(party));
			resultsPanel.add(partyResults.get(party));
>>>>>>> origin/demo
		}
	}


	public void updateResults(){
		ConcurrentHashMap<Candidate, Integer> results = server.getVotes();
		HashMap<String,Integer> totals = new HashMap<String,Integer>();
		for(String party:server.getParties()){
			totals.put(party, new Integer(0));
		}
		
		for(Candidate c:results.keySet()){
			if(candidateResults.containsKey(c.getName())){
				candidateResults.get(c.getName()).setText(
						String.valueOf(results.get(c).toString()));
				int i = totals.get(c.getParty()).intValue();
				totals.put(c.getParty(),new Integer(i+1));
			}
		}
		
		for (String party:server.getParties()){
			partyResults.get(party).setText(totals.get(party).toString());
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startElectionButton) {
			try {
				server.startElection();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(e.getSource() == stopElectionButton){
			server.stopElection();
		}
	}
}