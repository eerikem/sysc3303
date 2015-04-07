package client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.Voter;
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
			ArrayList<Voter> people = new ArrayList<Voter>();
			Pattern p = Pattern.compile("(\\w+).(\\w+)");
			while ((line = br.readLine()) != null) {
				Matcher m = p.matcher(line);
				Service.logInfo("reading line: " + line);
				if (m.find()) {
					Service.logInfo("found smthng in " + m.group(2));
					Voter voter = new Voter(line, m.group(1), m.group(2));
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
		    
			Random t = new Random();
			for (int j = 0; j < 90; j++) {
				
				for (int i = 0; i < people.size(); i++) {
					Voter voter = people.get(i);
					voter.username = voter.username.concat(Integer.toString(i)+1000*t.nextInt(20));
					tester.clients.get(i).register(voter);
				}

				Thread.sleep(500);

				for (int i = 0; i < people.size(); i++) {
					tester.clients.get(i).login(people.get(i).username,
							people.get(i).password);
				}

				Thread.sleep(500);

				Random r = new Random();
				String[] tmp = { "Greens", "NDP", "Liberals", "Conservatives",
						"Comis" };
				for (int i = 0; i < people.size(); i++) {
					tester.clients.get(i).vote(tmp[r.nextInt(tmp.length)]);
					Thread.sleep(20);
				}

			}

			Thread.sleep(500);

			for (Client c : tester.clients) {
				c.quit();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
