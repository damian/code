public class ScoringSystem
{

	public static int tally(Agent a, String result, int jobIndex)
	{
		int score = 0;
		int importance = a.getJobs().get(jobIndex).getImportanceLevel();
		switch (importance)
		{	
			case 5 : score+=20; break;
			case 4 : score+=15; break;
			case 3 : score+=10; break;
			case 2 : score+=7; break;
			case 1 : score+=5; break;
			case 0 : score+=3; break;
			default : break;
		}
		if (determineResult(result))
		{
			return score;
		}
		else
		{
			return -(score);
		}
	}
	
	
	public static int defectTally(Resource r, String result)
	{
		int score = 0;
		int category = Multilevel.getLevel(r);
		switch (category)
		{
			case 4 : score+=100; break;
			case 3 : score+=50; break;
			case 2 : score+=25; break;
			case 1 : score+=10; break;
			default : break;
		}
		if (determineResult(result))
		{
			return -(score);
		}
		else
		{
			return score;
		}
	}
	
	public static boolean determineResult(String result)
	{
		if (result.equals("<Permit>"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}	

		
	
	
	
	
	
	
}