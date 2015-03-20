package voteserver;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import common.Service;
import client.Client;



public class MainServerGUI extends JFrame implements ActionListener{
	private ArrayList<JLabel> candidateLabel;
	private ArrayList<JLabel> candidateResults;
	private ArrayList<String> candidates;
	private MainServer server;
	private JPanel resultsPanel;
	private JPanel buttonPanel;
	private JButton startElectionButton;
	private JButton stopElectionButton;
	public MainServerGUI(MainServer server) {
		super("Results Screen");
		setLayout(new BorderLayout());
		this.server = server;
		initResultsPanel();
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
	
	private void initResultsPanel(){
		String[] tmp = { "Greens", "NDP", "Liberals", "Conservatives", "Comis" };
		resultsPanel = new JPanel();
		resultsPanel.setLayout(new GridLayout(tmp.length,2));
		candidates = new ArrayList<String>(Arrays.asList(tmp));
		candidateLabel = new ArrayList<JLabel>();
		candidateResults = new ArrayList<JLabel>();
		for (int i=0; i<tmp.length; i++) {
			candidateLabel.add(new JLabel(tmp[i]));
			resultsPanel.add(candidateLabel.get(i));
			candidateResults.add(new JLabel("null"));
			resultsPanel.add(candidateResults.get(i));
		}
	}


	public void updateResults(){
		ConcurrentHashMap<String, Integer> results = server.getVotes();
		for(int i=0; i < candidateLabel.size(); i++){
			if(results.containsKey(candidateLabel.get(i).getText()));{
				candidateResults.get(i).setText(
						String.valueOf(results.get(candidateLabel.get(i).getText())));
			}	
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startElectionButton) {
			server.startElection();
		}else if(e.getSource() == stopElectionButton){
			server.stopElection();
		}
	}
}