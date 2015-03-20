package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
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
			ArrayList<Person> people = new ArrayList<Person>();
			Pattern p = Pattern.compile("(\\w+).(\\w+)");
			while ((line = br.readLine()) != null) {
				Matcher m = p.matcher(line);
				Service.logInfo("reading line: " + line);
				if (m.find()) {
					Service.logInfo("found smthng in " + m.group(2));
					Person voter = new Person(line, m.group(1), m.group(2));
					people.add(voter);
					Client c = new Client(DEFAULT_cfgFILE, name);
					c.enableTestMode();
					tester.clients.add(c);
					c.run();
				} else {
					Service.logWarn("Trouble Matching: " + line);
				}
			}
			br.close();
		    
			for (int i = 0 ; i<people.size();i++){
				tester.clients.get(i).register(people.get(i));
			}
			
			Thread.sleep(1000);
		    
			for (int i = 0 ; i<people.size();i++){
				tester.clients.get(i).login(people.get(i).username,people.get(i).password);
			}
			
			Thread.sleep(1000);
			
			Random r = new Random();
			String[] tmp = { "Greens", "NDP", "Liberals", "Conservatives", "Comis" };
			for (int i = 0 ; i<people.size();i++){
				tester.clients.get(i).vote(tmp[r.nextInt(tmp.length)]);
			}
		    
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create and Start Client
		Client client = new Client(DEFAULT_cfgFILE, name);
		client.run();
	}
}
