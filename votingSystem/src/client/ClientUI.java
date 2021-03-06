package client;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import common.Voter;
import common.Service;
import common.Person.Candidate;

public class ClientUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Client client;

	// UI Variables
	private ArrayList<JRadioButton> candidateButtons;
	private JTextField userField;
	private JPasswordField passField;
	private JTextField nameField;
	private JTextField addressField;
	private JButton voteButton;
	private JButton loginButton;
	private JButton registerButton;
	private ButtonGroup buttonGroup;
	private JPanel votePanel;
	private JPanel loginPanel;
	private JPanel regPanel;
	private HashMap<JRadioButton, Candidate> buttonMap;
	
	public ClientUI(Client client) {
		super("Voting System");

		this.client = client;

		initLoginPanel();
		initRegPanel();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(300, 150);
		setResizable(true);
		setLayout(new FlowLayout());

		WindowListener exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				logout();
			}
		};
		addWindowListener(exitListener);

		enableLogin();
	}

	private void initRegPanel(){
		regPanel = new JPanel();
		nameField = new JTextField();
		nameField.setColumns(17);
		nameField.setFont(new Font("Monospaced", Font.PLAIN, 14));
		
		
		addressField = new JTextField();
		addressField.setColumns(17);
		addressField.setFont(new Font("Monospaced", Font.PLAIN, 14));
		
		//TODO delete default address
		addressField.setText("Ottawa");
		
		regPanel.add(new JLabel("name"));
		regPanel.add(nameField);
		
		regPanel.add(new JLabel("address"));
		regPanel.add(addressField);
		
		registerButton = new JButton("Register");
		registerButton.addActionListener(this);
		regPanel.add(registerButton);

		regPanel.setLayout(new FlowLayout());
	}
	
	private void initVotePanel(ArrayList<Candidate> can){
		candidateButtons = new ArrayList<JRadioButton>();
		buttonMap = new HashMap<JRadioButton, Candidate>();
		votePanel = new JPanel();
		buttonGroup = new ButtonGroup();
		for (Candidate candidate : can) {
<<<<<<< HEAD
			JRadioButton b = new JRadioButton(candidate.getName() + " " + candidate.getEntity());
=======
			JRadioButton b = new JRadioButton(candidate.getName() + " " + candidate.getParty());
			buttonMap.put(b, candidate);
>>>>>>> origin/demo
			candidateButtons.add(b);
			buttonGroup.add(b);
			votePanel.add(b);
			b.addActionListener(this);
		}

		voteButton = new JButton("Vote");
		voteButton.addActionListener(this);
		votePanel.add(voteButton);

		votePanel.setLayout(new FlowLayout());
	}
	
	private void initLoginPanel(){
		loginPanel = new JPanel();

		userField = new JTextField();
		userField.setColumns(10);
		userField.setFont(new Font("Monospaced", Font.PLAIN, 14));

		passField = new JPasswordField();
		passField.setFont(new Font("Monospaced", Font.PLAIN, 14));
		passField.setColumns(10);
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);

		loginPanel.add(userField);
		loginPanel.add(passField);
		loginPanel.add(loginButton);
	}
	
	public void enableReg(){
		Container pane = this.getContentPane();
		pane.add(regPanel);
		this.pack();
	}
	
	public void enableLogin(){
		Container pane = this.getContentPane();
		pane.removeAll();
		pane.add(loginPanel);
		this.pack();
	}
	
	public void enableVoting() {
		if (client.getCandidates() == null){
			JOptionPane.showMessageDialog(this, "Election Has Not Started. Please Try again later");
			return;
		}
		initVotePanel(client.getCandidates());
		Container pane = this.getContentPane();
		pane.removeAll();
		buttonGroup.clearSelection();
		pane.add(votePanel);
		this.pack();
	}
	
	public void disableVoting(){
		Container pane = this.getContentPane();
		pane.removeAll();
		pane.add(loginPanel);
		userField.setText("");
		passField.setText("");
		nameField.setText("");
		this.pack();
	}

	public String getNewName(String reason) {
		return JOptionPane.showInputDialog(ClientUI.this, "Error " + reason
				+ "! Enter a new name : ", reason, JOptionPane.WARNING_MESSAGE);
	}

	public void logout() {
		client.quit();
	}

	// Displays an error message to the Client.
	public void displayError(String reason) {
		JOptionPane.showMessageDialog(ClientUI.this, reason, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == voteButton) {
			for (JRadioButton b : candidateButtons) {
				if (b.isSelected()) {
					client.vote(buttonMap.get(b));
					Service.logInfo("Client voted for the " + b.getText());
					disableVoting();
				}
			}
		}else if(e.getSource() == loginButton){
			handleLogin();
		}else if(e.getSource() == registerButton){
			handleReg();
		}
	}
	
	private void handleReg(){
		String user = userField.getText().trim();
		String pass = new String(passField.getPassword());
		String name = nameField.getText().trim();
		String address = addressField.getText().trim();
		
		if(!user.isEmpty() && !pass.isEmpty() && pass!=null && !name.isEmpty()){
			Voter p = new Voter(name, user, pass, address);
			client.register(p);
		}
	}
	
	private void handleLogin(){
		String user = userField.getText().trim();
		String pass = new String(passField.getPassword());
		if(!user.isEmpty() && !pass.isEmpty() && pass!=null){
			client.login(user,pass);
		}else{
			Service.logError("Improper credentials");
		}
	}
	
}
