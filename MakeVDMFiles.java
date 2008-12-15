import java.io.*;
import java.util.*;


public class MakeVDMFiles
{
	ArrayList<Agent> agents;
	ArrayList<Resource> resources;
	
	/*
	*  Creates the rules/policies and overall environment to be used in the access control system
	*  
	*  This outputs a .vpp file which is fed into the VDM++ toolkit within the Driver, so agent token values within
	*  the java side of the model are not acted upon directly, but are within the toolkit. We are only concerned
	*  with their initial values and the results of each request, both of which can be determined from the Java side
	*  of this framework.
	*/ 
	public MakeVDMFiles()
	{
		try
		{
			/*
			* Calls the other classes that we have created, so this information can be used to fill out the material
			* within the environment variable in the access control model
			*/
			VettingProcess v = new VettingProcess();
			MakeResources makeResources = new MakeResources();
			resources = makeResources.getResources();
			Collections.shuffle(resources);
			agents = v.getHired();
			Collections.shuffle(agents);
			AssignJobs assign = new AssignJobs(agents, resources);
			
			
			//Makes a new test file each time the simulation is run with fresh values and the following
			//access control rules/policies
			FileWriter f = new FileWriter(new File("Test.vpp"));
			f.write("class Test"+ "\n\n");
	    	f.write("values"+"\n\n");
	    	f.write("public requester : Cond`UnVar = mk_Cond`UnVar(<requester>);"+"\n");
	    	f.write("public resource  : Cond`UnVar = mk_Cond`UnVar(<resource>);"+"\n\n");
	    	f.write("public docArray : Cond`Var = mk_Cond`Var(mk_token(\"documents\"));"+"\n");
			f.write("public clearanceArray : Cond`Var = mk_Cond`Var(mk_token(\"clearClass\"));"+"\n");
	    	f.write("public agentArray : Cond`Var = mk_Cond`Var(mk_token(\"agents\"));"+"\n\n");
	
			f.write("public TopSecret : Cond`Var = mk_Cond`Var(mk_token(\"TopSecret\"));"+"\n");
			f.write("public Secret : Cond`Var = mk_Cond`Var(mk_token(\"Secret\"));"+"\n");
			f.write("public Confidential : Cond`Var = mk_Cond`Var(mk_token(\"Confidential\"));"+"\n");
			f.write("public Restricted : Cond`Var = mk_Cond`Var(mk_token(\"Restricted\"));"+"\n");
			f.write("public Unclassified : Cond`Var = mk_Cond`Var(mk_token(\"Unclassified\"));"+"\n\n");
	
			
	
			for (Agent a : agents)
			{
				f.write("public "+a.getId()+" : Cond`Var = mk_Cond`Var(mk_token(\""+a.getId()+"\"));"+"\n");
			}
			f.write("\n");
			
			for (Resource r : resources)
			{
				f.write("public "+r.getId()+" : Cond`Var = mk_Cond`Var(mk_token(\""+r.getId()+"\"));"+"\n");
			}
			f.write("\n");
			
			//All Permit
			f.write("public all_permit : PDP`Rule = mk_PDP`Rule(nil,<Permit>,nil);"+"\n");
			f.write("public permit : set of PDP`Rule = {all_permit};"+"\n");
			f.write("public permitPolicy : PDP`Policy = mk_PDP`Policy(nil,permit);"+"\n\n");
			
			//All Deny
			f.write("public all_deny : PDP`Rule = mk_PDP`Rule(nil,<Deny>,nil);"+"\n");
			f.write("public deny : set of PDP`Rule = {all_deny};"+"\n");
			f.write("public denyPolicy : PDP`Policy = mk_PDP`Policy(nil,deny);"+"\n\n");
			
			//Multi-level
			f.write("public multilevel : PDP`Rule = mk_PDP`Rule(nil,<Permit>,new Cond(mk_Cond`Compare((mk_Cond`FArrayLookup(clearanceArray,(mk_Cond`ArrayLookup(docArray,resource)))),<LE>,(mk_Cond`FArrayLookup(clearanceArray,mk_Cond`ArrayLookup(agentArray,requester))))));"+"\n");
			f.write("public multi : set of PDP`Rule = {multilevel};"+"\n");
			f.write("public pol : PDP`Policy = mk_PDP`Policy(nil,multi);"+"\n\n");
			
			//Pure Token Scheme
			f.write("public token_scheme : PDP`Rule = mk_PDP`Rule(nil,<Permit>,new Cond(mk_Cond`Compare(mk_Cond`ArrayLookup(docArray,resource),<LE>,mk_Cond`ArrayLookup(agentArray,requester))));"+"\n");
			f.write("public tokens : set of PDP`Rule = {token_scheme};"+"\n");
			f.write("public tokenPolicy : PDP`Policy = mk_PDP`Policy(nil,tokens);"+"\n\n");
			
			f.write("public hybrid : set of PDP`Rule = {multilevel,token_scheme};"+"\n");
			f.write("public hybridPolicy : PDP`Policy = mk_PDP`Policy(nil,hybrid);"+"\n\n");
			
			f.write("public env : Env = new Env({clearanceArray |-> {TopSecret |-> 4, Secret |-> 3, Confidential |-> 2, Restricted |-> 1, Unclassified |-> 0}, agentArray |-> {");
			
			int counter = 0;
			for (Agent a : agents)
			{
				if (!(counter == agents.size()-1))
				{
					f.write(""+a.getId()+" |-> "+a.getClearance()+", ");
				}
				else
				{
					f.write(""+a.getId()+" |-> "+a.getClearance()+"}, docArray |-> {");
				}
				counter++;
			}
			
			int counter1 = 0;
			for (Resource r : resources)
			{
				if (!(counter1 == resources.size()-1))
				{
					f.write(""+r.getId()+" |-> "+r.getClassification()+", ");
				}
				else
				{
					f.write(""+r.getId()+" |-> "+r.getClassification()+"}});"+"\n\n");
				}
				counter1++;
			}
			
			f.write("public tokenEnv : Env = new Env({agentArray |-> {");
			int counter2 = 0;
			for (Agent a : agents)
			{
				if (!(counter2 == agents.size()-1))
				{
					f.write(""+a.getId()+" |-> "+a.getTokens()+", ");
				}
				else
				{
					f.write(""+a.getId()+" |-> "+a.getTokens()+"}, docArray |-> {");
				}
				counter2++;
			}
			
			int counter3 = 0;
			for (Resource r : resources)
			{
				if (!(counter3 == resources.size()-1))
				{
					f.write(""+r.getId()+" |-> "+r.getTokens()+", ");
				}
				else
				{
					f.write(""+r.getId()+" |-> "+r.getTokens()+"}});"+"\n\n");
				}
				counter3++;
			}
			
			f.write("public hybridEnv : Env = new Env({clearanceArray |-> {TopSecret |-> 4, Secret |-> 3, Confidential |-> 2, Restricted |-> 1, Unclassified |-> 0}, agentArray |-> {");
			int counter4 = 0;
			for (Agent a : agents)
			{
				if (!(counter4 == agents.size()-1))
				{
					f.write(""+a.getId()+" |-> mk_Env`Mix("+a.getClearance()+","+a.getBaseTokens()+"), ");
				}
				else
				{
					f.write(""+a.getId()+" |-> mk_Env`Mix("+a.getClearance()+","+a.getBaseTokens()+")}, docArray |-> {");
				}
				counter4++;
			}
			
			int counter5 = 0;
			for (Resource r : resources)
			{
				if (!(counter5 == resources.size()-1))
				{
					f.write(""+r.getId()+" |-> mk_Env`Mix("+r.getClassification()+","+r.getBaseCost()+"), ");
				}
				else
				{
					f.write(""+r.getId()+" |-> mk_Env`Mix("+r.getClassification()+","+r.getBaseCost()+")}});"+"\n\n");
				}
				counter5++;
			}
			//NEED TO FINISH OFF HERE!!!!!!!!
			
			//All Permit Policy Being added to the Evaluator
			f.write("public permitEval : Evaluator = new Evaluator(permitPolicy,env);"+"\n");
			f.write("public permitPEP : PEP = new PEP(env,permitEval);"+"\n\n");
			
			//All Deny Policy Being added to the Evaluator
			f.write("public denyEval : Evaluator = new Evaluator(denyPolicy,env);"+"\n");
			f.write("public denyPEP : PEP = new PEP(env,denyEval);"+"\n\n");
			
			//MultiLevel Policy Being added to the Evaluator
			f.write("public eval : Evaluator = new Evaluator(pol,env);"+"\n");
			f.write("public pep : PEP = new PEP(env,eval);"+"\n\n");
			
			//Pure Token Scheme being added to the Evaluator
			//Also is supplied with a different environment object
			f.write("public tokenEval : Evaluator = new Evaluator(tokenPolicy,tokenEnv);"+"\n");
			f.write("public tokenPEP : PEP = new PEP(tokenEnv,tokenEval);"+"\n\n");
		
			//Hybrid scheme being added to the Evaluator
			//Also supplied with a different Environment object
			f.write("public hybridEval : Evaluator = new Evaluator(hybridPolicy,hybridEnv);"+"\n");
			f.write("public hybridPEP : PEP = new PEP(hybridEnv,hybridEval);"+"\n\n");
		
			f.write("\n"+"end Test");
			f.close();
		
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<Agent> getAgents()
	{
		return agents;
	}
	
	public ArrayList<Resource> getResources()
	{
		return resources;
	}
	
	/* public static void main(String[] args)
	{
		MakeVDMFiles m = new MakeVDMFiles();
	} */





}