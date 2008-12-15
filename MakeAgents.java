import java.util.*;
import java.lang.Math;


public class MakeAgents
{
	ArrayList<Double> randoms = new ArrayList<Double>();
	ArrayList<Agent> agents = new ArrayList<Agent>();
	public static final String[] clearance = {"TopSecret", "Secret", "Confidential", "Restricted", "Unclassified"}; //Unclassified is present for completeness

	//public static final int ASSUMED_TASKS_EACH = 8;

	public static final int NO_OF_AGENTS = 300;
	public static final double MAX_PROPENSITY = 0.05;
	public static final double HYBRID_ELASTICITY = 0.4;
	
	
	/*
	* Creates an array of agents with randomised attributes using the static information provided above
	*/
	public MakeAgents()
	{
		this.makePropensity(); //Generates an array of doubles
		//String s;
		int random = 0;
		int tokens = 0;
		int baseTokens = 0;
		for (int i=0; i<NO_OF_AGENTS;i++)
		{
			random = new Random().nextInt(clearance.length-1); //No need for the last item as there are no people who are unclassified in our study
			tokens = MakeResources.cost[random]*getAverageNoTasks();
			baseTokens = MakeResources.baseCost[random]+(int)(MakeResources.baseCost[random]*HYBRID_ELASTICITY);
			agents.add(new Agent(new String("Agent"+i),this.getDefect(),clearance[random],tokens,baseTokens));
		}	
	}
	
	/*
	* Utility method to determine the average number of jobs per person based on boundary values
	* stipulated in AssignJobs.java
	*/
	public static int getAverageNoTasks()
	{
		return (int)((AssignJobs.MIN_JOBS_PP+AssignJobs.MAX_JOBS_PP)/2);
	}
	
	/*
	* Method that generates an array of double values to be used in
	* allocating a propensity to defect within agents
	*/
	private void makePropensity()
	{
		double r;
		do
		{
			r = Math.random();
			if (r<MAX_PROPENSITY)
			{
				randoms.add(r);
			}
		}
		while (randoms.size() < NO_OF_AGENTS*2);
		Collections.shuffle(randoms);
	}
	
	/*
	* Returns a random double from the randoms array in seeding the agent attributes
	*/
	public double getDefect()
	{
		int random = new Random().nextInt(randoms.size()-1);
		return randoms.get(random);
	}
	
	/*
	* Get method to return the arraylist of agents
	*/
	public ArrayList<Agent> getAgents()
	{
		return agents;
	}
	
	public static void main(String[] args)
	{
		MakeAgents a = new MakeAgents();
		System.out.println("Average no of tasks : "+getAverageNoTasks());
		
		for (Agent ag : a.getAgents())
		{
			System.out.println(ag);
			//System.out.print(" - "+ag.getJobs().size()+" jobs");
			
		}
	}
	
	






}