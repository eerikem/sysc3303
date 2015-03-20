package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.Person;
import common.Service;

public class ClientTester {

	private static String testFile = "votingSystem/src/client/test.txt";
	private static String DEFAULT_cfgFILE = "votingSystem/src/client/client.cfg";
	public ArrayList<Client> clients;
	
	public ClientTester(){
		clients = new ArrayList<Client>();
	}
	
	public static void main(String[] args) {
		String name = "Tester Client";

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-t")) {
				testFile = args[++i];
			}
		}
		
		ClientTester tester = new ClientTester();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(testFile));
			String line;
			Pattern p = Pattern.compile("(\\w+).(\\w+)");
			while ((line = br.readLine()) != null) {
				Service.logInfo("reading line: " + line);
				Matcher m = p.matcher(line);
				Service.logInfo("reading line: " + line);
				if (m.find()) {
					Service.logInfo("found smthng in" + line);
					Person voter = new Person(name, m.group(1), m.group(2));
					Client c = new Client(name, DEFAULT_cfgFILE);
					tester.clients.add(c);
					c.register(voter);
				} else {
					Service.logWarn("Trouble Matching: " + line);
				}
			}
			br.close();
		    
		    
		    
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create and Start Client
		Client client = new Client(DEFAULT_cfgFILE, name);
		client.run();
	}
}
