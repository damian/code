import java.util.*;

public class Agent
{

    String id, clearance;
	int tokens,baseTokens;
	double propensity;
	boolean defect;
    ArrayList<Job> jobs = new ArrayList<Job>();
    
	/*
	* Agent attributes to be used in both the access control side of the model, in determining
	* how jobs should be allocated, and also in the scoring mechanism
	*/
    public Agent(String id, double propensity, String clearance, int tokens, int baseTokens)
    {
		this.id = id;
		this.propensity = propensity;
		this.clearance = clearance;
		this.tokens = tokens;
		this.baseTokens = baseTokens;
		this.defect = false;
    }

	public String getId()
	{
		return id;
	}
	
	/*
	* Agents are passed an array of jobs from the AssignJobs.class using this method
	*/
	public void setJobs(ArrayList<Job> jobs)
	{
		this.jobs.addAll(jobs);
		sortJobs();
	}
	
	/*
	* Method to prioritise agent jobs, based on their level of importance(from 5 down to 0)
	*/
	public void sortJobs()
	{
		Collections.sort(jobs);
		Collections.reverse(jobs);
	}
    
    public ArrayList<Job> getJobs()
    {
        return jobs;
    }
	
	public double getPropensity()
	{
		return propensity;
	}
	
	public String getClearance()
	{
		return clearance;
	}
	
	public int getTokens()
	{
		return tokens;
	}
	
	public int getBaseTokens()
	{
		return baseTokens;
	}
	
	public void setDefect()
	{
		defect = true;
	}
	
	public boolean getDefect()
	{
		return defect;
	}
	
	public String toString()
	{
		return id + " - "+clearance + " - "+propensity+ " - "+tokens+ " - "+baseTokens;
	}






}