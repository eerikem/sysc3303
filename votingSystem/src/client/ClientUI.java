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
import java.util.Arrays;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import common.Service;

public class ClientUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Client client;

	// UI Variables
	private static ArrayList<JRadioButton> candidateButtons;
	private JTextField userField;
	private JPasswordField passField;
	private JButton voteButton;
	private JButton loginButton;
	private ArrayList<String> candidates;
	private ButtonGroup buttonGroup;
	private JPanel votePanel;
	private JPanel loginPanel;
	
	public ClientUI(Client client) {
		super("Voting System");

		this.client = client;

		initLoginPanel();
		initVotePanel();
		
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

	private void initVotePanel(){
		// TODO get candidates from client
		String[] tmp = { "Greens", "NDP", "Liberals", "Conservatives", "Comis" };
		candidates = new ArrayList<String>(Arrays.asList(tmp));
		candidateButtons = new ArrayList<JRadioButton>();
		votePanel = new JPanel();

		buttonGroup = new ButtonGroup();
		for (String candidate : candidates) {
			JRadioButton b = new JRadioButton(candidate);
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
	
	private void enableLogin(){
		Container pane = this.getContentPane();
		pane.removeAll();
		pane.add(loginPanel);
		this.pack();
	}
	
	public void enableVoting() {
		Container pane = this.getContentPane();
		pane.removeAll();
		pane.add(votePanel);
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
					client.vote(b.getText());
					Service.logInfo("Client voted for the " + b.getText());
				}
			}
		}else if(e.getSource() == loginButton){
			handleLogin();
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
