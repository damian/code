public class Multilevel
{

	/*
	* Uses generics to determine the clearance/classification of
	* both agents and resources
	*/
	public static <E> int getLevel(E r)
	{
		String c = "";
		if (r instanceof Resource)
		{
			Resource x = (Resource)r;
			c = x.getClassification();
		}
		else
		{
			Agent a = (Agent)r;
			c = a.getClearance();
		}
		if (c.equals("TopSecret")) return 4;	
		else if (c.equals("Secret")) return 3;
		else if (c.equals("Confidential")) return 2;
		else if (c.equals("Restricted")) return 1;
		else return 0;
	}
	
	
	
	
	
}