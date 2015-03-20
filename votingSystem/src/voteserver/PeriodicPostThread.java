package voteserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import common.Service;

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
		prevNumVotes = 0;
	}
	
	public void run(){
		Service.logInfo("Post Thread started");
		while(main.getVotingEnabled())
		{
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				Service.logError("Post thread interrupted while waiting");
			}
			if(main.getNumberVotes() - prevNumVotes >= 5)
			{//magic
				prevNumVotes = main.getNumberVotes();
				votePoint = new HashMap<>(main.getVotes());
				votePlot.add(votePoint);
				String trendingCandidate = getBestTrend();
				String leadingCandidate = getLeading();
				String majCandidate = getMajority();
				Service.logInfo("Predictions:");
				Service.logInfo("Currently trending candidate: " + trendingCandidate);
				Service.logInfo("Currently leading candidate: " + leadingCandidate);
				Service.logInfo("Candidate with majority lead: " + majCandidate);
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
			if(!candidateList.containsKey(cand))
			{
				candidateList.put(cand, 0);
			}
			
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
