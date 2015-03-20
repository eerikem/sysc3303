package client;

import java.io.IOException;

import common.Connection;
import common.Connector;
import common.Event;
import common.Person;
import common.Service;

public class Client extends Service {

	private static String DEFAULT_NAME = "Client";
	private static String DEFAULT_cfgFILE = "votingSystem/src/client/client.cfg";
	private static String DEFAULT_HOST = "localhost";
	private static int DEFAULT_PORT = 9090;
	private Connector connector;
	private Connection connection;

	private ClientUI clientUI;
	private String name;

	public Client(String file, String name) {
		super(file);

		this.connector = new Connector(this);
		
		this.name = name;
		clientUI = new ClientUI(this);
		clientUI.setVisible(true);
	}

	public static void main(String[] args) {

		// Default in case no args
		String name = DEFAULT_NAME;
		String cfgFile = DEFAULT_cfgFILE;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-name")) {
				name = args[++i];
			} else if (args[i].equals("-c")) {
				cfgFile = args[++i];
			}
		}

		// Create and Start Client
		Client client = new Client(cfgFile, name);
		client.run();
	}

	public void run() {
		try {
			connection = connector.connect(DEFAULT_HOST, DEFAULT_PORT);
			Thread t = new Thread(connection);
			t.start();
		} catch (IOException e) {
			Service.logError("Could not establish connection with Server.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Send a login Event to the Server.
	public void login(String user, String password) {
		try {
			Event e = new Event("LOGIN");
			e.put("username", user);
			e.put("password", password);

			connection.sendEvent(e);
		} catch (IOException e) {
			Service.logError("Error Sending Event: " + e.toString());
		}
	}
	
	public void register(Person p){
		try {
			Event e = new Event("REGISTER");
			e.put("person", p);
			connection.sendEvent(e);
		} catch (IOException e) {
			Service.logError("Error Sending Event: " + e.toString());
		}
	}

	public void loginFailed(String reason) {
		name = clientUI.getNewName(reason);
		if (name == null) {
			quit();
		} else {
			//updateClientUI();
			//login();
		}
	}


	public void quit() {
		connection.kill();
		System.exit(0);
	}

	public void vote(){
		clientUI.enableVoting();
	}
	
	public void enableReg(){
		clientUI.enableReg();
	}
	
	public void displaySuccess(String message) {
		Service.logInfo(message);
	}

	public void displayError(String reason) {
		clientUI.displayError(reason);
	}

	public void vote(String vote) {
		try {

			Event e = new Event("VOTE");
			e.put("vote", vote);
			connection.sendEvent(e);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
