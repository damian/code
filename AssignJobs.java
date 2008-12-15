import java.util.*;

public class AssignJobs
{

	/*
	public static final int[] TS_FREQUENCY = {20,20,20,20,20}; //Percentage breakdowns to be used in larger simulations
	public static final int[] S_FREQUENCY = {15,30,30,15,10};
	public static final int[] C_FREQUENCY = {30,30,30,8,2};
	public static final int[] R_FREQUENCY = {50,40,5,4,1}; 
	*/
	
	public static final int[] TS_FREQUENCY = {2,2,3,3}; //Demonstration settings going from Restricted - Top Secret
	public static final int[] S_FREQUENCY = {2,2,4,2};
	public static final int[] C_FREQUENCY = {3,4,2,1};
	public static final int[] R_FREQUENCY = {6,3,1,0};
	
	public static final int MIN_JOBS_PP = 3;
	public static final int MAX_JOBS_PP = 10; 
	
	public static final int IMPORTANCE_LEVEL = 5; //Five weightings of importance going from 0 to 5
	
	/*
	* Passed arraylists of agents and resources which were outputted as a result of MakeAgents and MakeResources
	* classes within this project. This class uses a combination of that information and that present here
	* to determine a how many jobs an agent should be allocated and the nature of those jobs suiting the role
	* of the individual they are being assigned to
	*/
	public AssignJobs(ArrayList<Agent> agents, ArrayList<Resource> resources)
	{
		Collections.shuffle(resources);
		ArrayList<Job> jobs = new ArrayList<Job>();
		int tasks;
		int[] boundaries = new int[TS_FREQUENCY.length];
		for (Agent a : agents)
		{
			tasks = (int)(Math.random()*MAX_JOBS_PP);
			//Makes sure the number of tasks assigned to an agent is within the range of MIN_JOBS_PP and MAX_JOBS_PP
			if (!(tasks > 3))
			{
				tasks+=MIN_JOBS_PP;
			}
			jobs = allocateJobs(a, resources, jobs, tasks, boundaries);
			a.setJobs(jobs);
			jobs.clear();
		}
	}
	
	
	
	public ArrayList<Job> allocateJobs(Agent a, ArrayList<Resource> resources, ArrayList<Job> jobs, int tasks, int[] boundaries)
	{	
		switch (Multilevel.getLevel(a))
		{
			case 4 : boundaries = TS_FREQUENCY.clone(); break;
			case 3 : boundaries = S_FREQUENCY.clone(); break;
			case 2 : boundaries = C_FREQUENCY.clone(); break;
			case 1 : boundaries = R_FREQUENCY.clone(); break;
			default : break;	
		}
	
		Resource r;
		Job j;
		while (tasks >= 0)
		{
			r = resources.get(new Random().nextInt(resources.size()));
			j = new Job(r.getId(), new Random().nextInt(IMPORTANCE_LEVEL+1));
			if (!(jobs.contains(r.getId())) && boundaries[Multilevel.getLevel(r)-1] > 0)
			{
				jobs.add(j);
				boundaries[Multilevel.getLevel(r)-1]--;
				tasks--;
			}
		}
		return jobs;
	}
	

	
	/*
	public static void main(String[] args)
	{
		VettingProcess vetting = new VettingProcess();
		MakeResources resources = new MakeResources();
		AssignJobs assign = new AssignJobs(vetting.getHired(), resources.getResources());
		System.out.println("Average no of jobs : "+MakeAgents.getAverageNoTasks());
		
		
		for (Agent a : vetting.getHired())
		{
			System.out.println(a.getId()+" : "+a.getClearance()+" - tok : "+a.getTokens()+" - hyb : "+a.getBaseTokens()+" - "+a.getJobs().size()+" jobs");
			for (Job j : a.getJobs())
			{
				System.out.print(""+j.getJob()+" - Imp Level : "+j.getImportanceLevel());
				for (Resource r : resources.getResources())
				{
					if (r.getId().equals(j.getJob()))
					{
						System.out.println(" - "+r.getClassification());
					}
				}
			}
		} 
	}*/
	





}