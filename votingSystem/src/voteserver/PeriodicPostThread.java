package voteserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class PeriodicPostThread extends Thread{
	
	private MainServer main;
	private HashMap<String, Integer> votePoint;
	private ArrayList<HashMap<String, Integer>> votePlot;
	private int prevNumVotes;
	
	public PeriodicPostThread(MainServer server)
	{
		main = server;
		votePoint = new HashMap<>(main.getVotes());
		votePlot = new ArrayList<>();
		votePlot.add(votePoint);
		prevNumVotes = main.getNumberVotes();
	}
	
	public void run()
	{
		while(main.getVotingEnabled())
		{
			if(main.getNumberVotes() - prevNumVotes > 10)
			{//magic
				votePoint = new HashMap<>(main.getVotes());
				votePlot.add(votePoint);
				String trendingCandidate = getBestTrend();
				String leadingCandidate = getLeading();
				String majCandidate = getMajority();
				System.out.println("Predictions:");
				System.out.println("Currently trending candidate: " + trendingCandidate);
				System.out.println("Currently leading candidate: " + leadingCandidate);
				System.out.println("Candidate with majority lead: " + majCandidate);
			}		
		}	
	}
	
	private String getBestTrend()
	{
		String incombentCandidate = "No one";
		Integer bestVoteDif = 0;
		HashMap<String, Integer> candidateList = votePlot.get(votePlot.indexOf(votePoint)-1);
		
		for(String cand: votePoint.keySet())
		{
			Integer voteDif = votePoint.get(cand) - candidateList.get(cand);
			if(voteDif > bestVoteDif)
			{
				bestVoteDif = voteDif;
				incombentCandidate = cand;
			}
		}
		return incombentCandidate;
	}
	
	private String getLeading()
	{
		String incombentCandidate = "No one";
		Integer mostVotes = 0;
		
		for(String cand: votePoint.keySet())
		{
			Integer candVotes = votePoint.get(cand);
			if(candVotes > mostVotes)
			{
				mostVotes = candVotes;
				incombentCandidate = cand;
			}
		}
		return incombentCandidate;
	}
	
	private String getMajority()
	{
		String incombentCandidate = "No one";
		Integer mostVotes = 0;
		Integer allVotes = _getSum(votePoint.values());
		for(String cand: votePoint.keySet())
		{
			Integer candVotes = votePoint.get(cand);
			if(candVotes > new Integer(allVotes) - candVotes)
			{
				mostVotes = candVotes;
				incombentCandidate = cand;
			}
		}
		return incombentCandidate;
	}
	
	private Integer _getSum(Collection<Integer> cands)
	{
		Integer sum = 0;
		for(Integer cand: cands)
		{
			sum += cand;
		}
		return sum;
	}
}
