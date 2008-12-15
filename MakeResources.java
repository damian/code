import java.util.*;

public class MakeResources
{
	ArrayList<Resource> resources = new ArrayList<Resource>();
	
	/*
	* Costing breakdown for resources, going from index 0(Top Secret) to element 3(Restricted)
	*/
	public static final int[] cost = {200,170,150,100};//,80};
	public static final int[] baseCost = {100, 75, 50, 25};//, 10};
	static final int NO_OF_RESOURCES = 2000;

	
	/*
	* Makes an arraylist of resources with randomised characteristics based on the
	* information provided here and in the MakeAgents class
	*/
	public MakeResources()
	{
		int random = 0;
		int price = 0;
		int base = 0;
		String classification;
		int importanceLevel = 0;
		for (int i=0; i<NO_OF_RESOURCES; i++)
		{
			random = new Random().nextInt(MakeAgents.clearance.length-1);
			classification = MakeAgents.clearance[random];
			
			//Costing for pure economic scheme
			price = cost[random];
			
			//Costing for the hybrid multi-level + economic scheme
			base = baseCost[random];
			resources.add(new Resource(new String("Resource"+i),classification,price,base));
		}
	}
	
	public ArrayList<Resource> getResources()
	{
		return resources;
	}
	
	/*
	public static void main(String[] args)
	{
		MakeResources a = new MakeResources();
		for (Resource r : a.getResources())
		{
			System.out.println(r);
		}
	}*/





}