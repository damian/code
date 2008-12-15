import java.util.*;

public class VettingProcess
{

	double[] cutoff = {0.005, 0.01, 0.02, 0.025}; //Thresholds for each clearance level
	ArrayList<Integer> boot = new ArrayList<Integer>();
	ArrayList<Agent> goodies = new ArrayList<Agent>();
	ArrayList<Agent> subset = new ArrayList<Agent>();
	int[] breakdown = {15, 25, 25, 35}; //Percentages - must add up to 100
	
	public VettingProcess()
	{
		MakeAgents a = new MakeAgents();
		System.out.println("Before vetting process : "+a.getAgents().size());
		int index;
		for (int i=0; i<a.getAgents().size()-1; i++)
		{
			//Determines what threshold category each agent falls into
			if (a.getAgents().get(i).getClearance().equals("TopSecret"))
			{
				index = 0;
			}
			else if (a.getAgents().get(i).getClearance().equals("Secret"))
			{
				index = 1;
			}
			else if (a.getAgents().get(i).getClearance().equals("Confidential"))
			{
				index = 2;
			}
			else //if (a.getAgents().get(i).getClearance().equals("Restricted"))
			{
				index = 3;
			}
			/* else
			{
				index = 4;
			} */
			
			//Determines each agent with a propensity to defect thats greater than
			//the category threshold they previously fell within
			switch (index)
			{
				case 0 : 
				if (a.getAgents().get(i).getPropensity() > cutoff[index])
				{
					boot.add(i);
				}; break;
				case 1 : 
				if (a.getAgents().get(i).getPropensity() > cutoff[index])
				{
					boot.add(i);
				}; break;
				case 2 : 
				if (a.getAgents().get(i).getPropensity() > cutoff[index])
				{
					boot.add(i);
				}; break;
				case 3 :
				if (a.getAgents().get(i).getPropensity() > cutoff[index])
				{
					boot.add(i);
				}; break;

				default : break;
			}
		}
		System.out.println("Unsatisfactory Applicants : "+boot.size());
		Agent x;
		//Removes untrustworthy agents
		for (int i=boot.size()-1; i>=0; i--)
		{
			int kick = boot.get(i);
			x = a.getAgents().get(kick);
			a.getAgents().remove(x);
		}
		System.out.println("After vetting process : "+a.getAgents().size());
		goodies.addAll(a.getAgents());
		getSubset();
	}
	
	public void getSubset()
	{
		//From those who are deemed trustworthy for the clearances they asked for
		//this method "employs" the correct number of people to fit in with the percentage
		//breakdown indicated previously so there is a pre-determined hierarchical structure
		int[] categories = new int[breakdown.length];
		
		int ts = 0;
		int sec = 0;
		int con = 0;
		int res = 0;
		//int unc = 0;
		
		//Determines the lower bound for the structure of the hierarchy (Top Secret) as 
		//attaining those who are very trustworthy for this clearance level is a difficult thing.
		//Using this as a basis, the breakdown for the people in the following clearance levels is determined.
		for (Agent a : goodies)
		{
			if (a.getClearance().equals("TopSecret"))
			{
				subset.add(a);
				ts++;
				//categories[0]-=1;
			}
		}
		categories[0]=ts;
		double x = (double)subset.size()/breakdown[0];
		int total = (int)Math.ceil(x*(double)100);
		for (int i=1; i<breakdown.length; i++)
		{
			double left = (double)total/100;
			double right = (double)breakdown[i];
			double product = left*right;
			categories[i] = (int)Math.floor(product);
		}
		for (Agent a : goodies)
		{
			if (a.getClearance().equals("Secret") && sec < categories[1])
			{
				subset.add(a);
				sec++;
				//categories[1]-=1;
			}
			else if (a.getClearance().equals("Confidential") && con < categories[2])
			{
				subset.add(a);
				con++;
				//categories[2]-=1;
			}
			else if (a.getClearance().equals("Restricted") && res < categories[3])
			{
				subset.add(a);
				res++;
				//categories[3]-=1;
			}
		}
		System.out.println("Employees Hired : "+subset.size());
		System.out.println("Top Secret - "+ts);
		System.out.println("Secret - "+sec);
		System.out.println("Confidential - "+con);
		System.out.println("Restricted - "+res);
		//System.out.println("Unclassified - "+unc); 
		
		
	}
	
	public ArrayList<Agent> getHired()
	{
		return subset;
	}
	/*
	public static void main(String[] args)
	{
		VettingProcess v = new VettingProcess();
		System.out.println("Employees Hired : "+v.getHired().size());
	} */

	
}