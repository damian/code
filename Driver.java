/**
 * The Driver class acts as the central driver of the workbench, the class creates the VPPController
 * object.
 *
 * @author Richard Payne
 * @version 25/04/2008
 */

import java.io.*;
import java.util.*;
import jp.co.csk.vdm.toolbox.api.*;
import jp.co.csk.vdm.toolbox.api.corba.ToolboxAPI.*;
import jp.co.csk.vdm.toolbox.api.corba.VDM.*;




public class Driver
{
	public VDMController control = new VDMController();
	
	/**
	 * Main Method for the workbench.
	 *
	 * @param args the command line arguments
	 */
	public static void main(String args[]) 
	{
		long start = System.currentTimeMillis();			
		Driver test = new Driver();
		test.run();
		long time = System.currentTimeMillis() - start;
		System.out.println("Elapsed time : "+time);
	}
	
	/**
	 * The run method for Driver class
	 */
	public void run()
	{
		try
		{
			//list for the files to be used in 
			LinkedList<String> files = new LinkedList<String>();
			
			files.add(new String("/Users/mymac/Desktop/Multilevel_and_Economics/Cond.vpp"));
			files.add(new String("/Users/mymac/Desktop/Multilevel_and_Economics/Env.vpp"));
			files.add(new String("/Users/mymac/Desktop/Multilevel_and_Economics/Evaluator.vpp"));
			files.add(new String("/Users/mymac/Desktop/Multilevel_and_Economics/PDP.vpp"));
			files.add(new String("/Users/mymac/Desktop/Multilevel_and_Economics/PEP.vpp"));
			files.add(new String("/Users/mymac/Desktop/Multilevel_and_Economics/Request.vpp"));
			
			//Makes the test file and adds it to the above list
			MakeVDMFiles testFile = new MakeVDMFiles();
			
			files.add(new String("/Users/mymac/Desktop/Multilevel_Tokens/Test.vpp"));
			
			// The syntax and type checking of the files. 
			control.init(files);			
			control.evaluateCommand("create t := new Test()");
			
			
			//Arrays to collect the results of the same agents making requests through the
			//various policies
			ArrayList<VDMGeneric> multilevel = new ArrayList<VDMGeneric>();
			ArrayList<VDMGeneric> permit = new ArrayList<VDMGeneric>();
			ArrayList<VDMGeneric> deny = new ArrayList<VDMGeneric>();
			ArrayList<VDMGeneric> token = new ArrayList<VDMGeneric>();
			ArrayList<VDMGeneric> hybrid = new ArrayList<VDMGeneric>();
			VDMGeneric nRequest;
			int totalRequests = 0;

			for (Agent a : testFile.getAgents())
			{
				totalRequests += a.getJobs().size();
				//System.out.println(a.getId() + " - "+a.getTokens());
			}
			double averageReq = (double)totalRequests/(double)testFile.getAgents().size();
			System.out.println("Average Requests Per Agent : "+averageReq);
			System.out.println("Requests Per Policy : "+totalRequests);
			System.out.println("Total No of Requests : "+totalRequests*5);
			
			for (Agent a : testFile.getAgents())
			{
				for (Job j : a.getJobs())
				{
					//double r = Math.random();
					if (Math.random()<a.getPropensity())
					{
						a.setDefect();
					}
					
					nRequest = control.evaluateExpression("t.pep.makeRequest(new Request(t."+a.getId()+", t."+j.getJob()+", mk_token(\"Write\")))");
					multilevel.add(nRequest);
					nRequest = control.evaluateExpression("t.permitPEP.makeRequest(new Request(t."+a.getId()+", t."+j.getJob()+", mk_token(\"Write\")))");
					permit.add(nRequest);
					nRequest = control.evaluateExpression("t.denyPEP.makeRequest(new Request(t."+a.getId()+", t."+j.getJob()+", mk_token(\"Write\")))");
					deny.add(nRequest);
					nRequest = control.evaluateExpression("t.tokenPEP.makeRequest(new Request(t."+a.getId()+", t."+j.getJob()+", mk_token(\"Write\")))");
					token.add(nRequest);
					nRequest = control.evaluateExpression("t.hybridPEP.makeRequest(new Request(t."+a.getId()+", t."+j.getJob()+", mk_token(\"Write\")))");
					hybrid.add(nRequest); 
				}
			}
			
			
			int overallMultilevel = 0;
			int overallDeny = 0;
			int overallPermit = 0;
			int overallTokens = 0;
			int overallHybrid = 0;
			int scoreCounter = 0;
			
			int multidefect = 0;
			int tokendefect = 0;
			int permitdefect = 0;
			int denydefect = 0;
			int hybriddefect = 0;
			for (Agent a : testFile.getAgents())
			{
				if (a.getDefect())
				{
					for (Job j : a.getJobs())
					{
						for (Resource r : testFile.getResources())
						{
							if (j.getJob().equals(r.getId()))
							{
								//System.out.println("Defected tally");
								multidefect+=ScoringSystem.defectTally(r,multilevel.get(scoreCounter).ToAscii());
								//System.out.println("Multilevel defect = "+defectTally(r,multilevel.get(scoreCounter).ToAscii()));
								permitdefect+=ScoringSystem.defectTally(r,permit.get(scoreCounter).ToAscii());
								//System.out.println("Permit defect = "+defectTally(r,permit.get(scoreCounter).ToAscii()));
								denydefect+=ScoringSystem.defectTally(r,deny.get(scoreCounter).ToAscii());
								//System.out.println("Deny defect = "+defectTally(r,deny.get(scoreCounter).ToAscii()));
								tokendefect+=ScoringSystem.defectTally(r,token.get(scoreCounter).ToAscii());
								//System.out.println("Economic defect = "+defectTally(r,token.get(scoreCounter).ToAscii()));
								hybriddefect+=ScoringSystem.defectTally(r,hybrid.get(scoreCounter).ToAscii());
								//System.out.println("Hybrid defect = "+defectTally(r,hybrid.get(scoreCounter).ToAscii()));
								scoreCounter++;
							}
						}
					}
				}
				else
				{
					for (int i=0; i<a.getJobs().size()-1; i++) //How many jobs each agent has
					{
						overallMultilevel+=ScoringSystem.tally(a,multilevel.get(scoreCounter).ToAscii(),i);
						overallPermit+=ScoringSystem.tally(a,permit.get(scoreCounter).ToAscii(),i);
						overallDeny+=ScoringSystem.tally(a,deny.get(scoreCounter).ToAscii(),i);
						overallTokens+=ScoringSystem.tally(a,token.get(scoreCounter).ToAscii(),i);
						overallHybrid+=ScoringSystem.tally(a,hybrid.get(scoreCounter).ToAscii(),i);
						scoreCounter++;
					}
				}
			}
			
			//int multilevelPermit = 0;
			//int multilevelDeny = 0;
			
			String[] multiresults = getResults(multilevel).split(" - ");
			int multilevelPermit = Integer.parseInt(multiresults[0]);
			int multilevelDeny = Integer.parseInt(multiresults[1]);
			
			String[] tokenresults = getResults(token).split(" - ");
			int tokenPermit = Integer.parseInt(tokenresults[0]);
			int tokenDeny = Integer.parseInt(tokenresults[1]);
			
			String[] permitresults = getResults(permit).split(" - ");
			int permitPolicy = Integer.parseInt(permitresults[0]);
			int permitPolicy1 = Integer.parseInt(permitresults[1]);
			
			String[] denyresults = getResults(deny).split(" - ");
			int denyPolicy = Integer.parseInt(denyresults[0]);
			int denyPolicy1 = Integer.parseInt(denyresults[1]);
			
			String[] hybridresults = getResults(hybrid).split(" - ");
			int hybridPermit = Integer.parseInt(hybridresults[0]);
			int hybridDeny = Integer.parseInt(hybridresults[1]);
			
			
			System.out.println("******************************************");
			System.out.println("All_Permit #Permit : "+permitPolicy);
			System.out.println("All_Permit #Deny : "+permitPolicy1);
			System.out.println("All_Permit Job Importance Score : "+overallPermit);
			System.out.println("All_Permit Defect Score : "+permitdefect);
			System.out.println("Overall Score : "+(overallPermit+permitdefect));
			System.out.println("******************************************");
			System.out.println("All_Deny #Permit : "+denyPolicy);
			System.out.println("All_Deny #Deny : "+denyPolicy1);
			System.out.println("All_Deny Job Importance Score : "+overallDeny);
			System.out.println("All_Deny Defect Score : "+denydefect);
			System.out.println("Overall Score : "+(overallDeny+denydefect));
			System.out.println("******************************************");
			System.out.println("Multilevel #Permit : "+multilevelPermit);
			System.out.println("Multilevel #Deny : "+multilevelDeny);
			System.out.println("Multilevel Job Importance Score : "+overallMultilevel);
			System.out.println("Multilevel Defect Score : "+multidefect);
			System.out.println("Overall Score : "+(overallMultilevel+multidefect));
			System.out.println("******************************************");
			System.out.println("Hybrid #Permit : "+hybridPermit);
			System.out.println("Hybrid #Deny : "+hybridDeny);
			System.out.println("Hybrid Job Importance Score : "+overallHybrid);
			System.out.println("Hybrid Defect Score : "+hybriddefect);
			System.out.println("Overall Score : "+(overallHybrid+hybriddefect));
			System.out.println("******************************************");
			System.out.println("Token #Permit : "+tokenPermit);
			System.out.println("Token #Deny : "+tokenDeny);
			System.out.println("Token Job Importance Score : "+overallTokens);
			System.out.println("Token Defect Score : "+tokendefect);
			System.out.println("Overall Score : "+(overallTokens+tokendefect));
			System.out.println("******************************************");
			
			
			for (Agent a : testFile.getAgents())
			{
				if (a.getDefect())
				{
					System.out.println(a.getId()+" - "+a.getClearance()+" - "+a.getPropensity());
					System.out.println("No of jobs : "+a.getJobs().size());
					for (Job j : a.getJobs())
					{
						for (Resource r : testFile.getResources())
						{
							if (r.getId().equals(j.getJob()))
							{
								System.out.println(r.getId() + " - "+r.getClassification());
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String getResults(ArrayList<VDMGeneric> results)
	{
		int permit = 0;
		int deny = 0;
		for (VDMGeneric v : results)
		{
			if (v.ToAscii().equals("<Permit>"))
			{
				permit++;
			}
			else
			{
				deny++;
			}
		}
		return permit + " - " + deny;
	}
	
	
}