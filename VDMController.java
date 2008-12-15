import org.omg.CORBA.*;
import java.io.*;
import java.util.LinkedList;
import jp.co.csk.vdm.toolbox.api.*;
import jp.co.csk.vdm.toolbox.api.corba.ToolboxAPI.*;
import jp.co.csk.vdm.toolbox.api.corba.VDM.*;

/**
 * This class is designed to interface with the VDM++ Toolbox. This class will connect to the toolbox, 
 * and setup the toolbox to begin operating with a given set of vpp files. The class also has support for
 * executing operations and commands on a VDM++ model.
 *
 * @author Richard Payne
 * @version 25/04/2008
**/
public class VDMController
{
	private short client;
	private VDMApplication app;
	private VDMInterpreter interp;

	/*
	 * This method will initialise the VDMToolbox, it shall set up an ORB for communiation with the 
	 * running VDM++ Toolbox (either command-line or gde), the given files shall be added, parsed and
	 * typed checked. Also the interpreter shall be initialised.
	 *
	 * @param   files A LinkedList containing the files to be added to the new project
	 * @return The client ID
	 */
	public void init(LinkedList files)
	{
		try
		{
			ToolboxClient tool =  new ToolboxClient();
			org.omg.CORBA.Object obj = tool.getVDMApplication(new String [] {}, ToolType.PP_TOOLBOX);

			app = VDMApplicationHelper.narrow(obj);

			client = app.Register();
			app.PushTag(client);

			//create new VDM++ project
			VDMProject prj = app.GetProject();
			prj.New();
			
			for(int i = 0; i < files.size(); i++)
			{
				//add relevant file(s) to project
				
				System.out.println((String) files.get(i));
				prj.AddFile((String) files.get(i));
			}
			
			//parse relevant file(s)
			VDMParser parser = app.GetParser();
			
			for(int i = 0; i < files.size(); i++)
			{
				//add relevant file(s) to project
				parser.Parse((String) files.get(i));
			}
			
			typeCheck();
			
			//create interpreter object and initialize
			interp = app.GetInterpreter();
			interp.DynTypeCheck(true);
			interp.DynInvCheck(true);
			interp.DynPreCheck(true);
			interp.DynPostCheck(true);
 			interp.Initialize();
		}
		catch(ToolboxClient.CouldNotResolveObjectException ex)
		{
			System.out.println("Could not resovle toolbox object");
			ex.printStackTrace();
		}
		catch(APIError ex)
		{
			System.out.println("Error in setting up project");
			ex.printStackTrace();
		}
	}
	
	
	/**
	 * Method to execute an operation in VDM++
	 *
	 * @param expr       Expression to evaluate
	 * @param arguements Parameters for expression
	 * @return          Result of expression in a VDMGeneric object
	 * @throws APIError Thrown if there is something wrong with the Aply method of the interpreter
	 */
	public VDMGeneric executeOperation(String expr, VDMSequence arguments) 
	{
		VDMGeneric result = null;
		try
		{
			//uses the interpreter to execute an operation of the model - with the supplied VDMSequence
			//as the list of arguements and the operation to apply as the String expr.
			result = interp.Apply(client, expr, arguments);
		}
		catch(APIError ex)
		{
			System.out.println("Execute operation error");
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Method to evaluate a command in VDM++
	 *
	 * @param expr       Expression to evaluate
	 */
	public void evaluateCommand(String expr)
	{		
		try
		{	
			interp.EvalCmd(expr);
		}
		catch(APIError ex)
		{
			System.out.println("Evaluate Command error");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Method to evaluate an expression in VDM++
	 *
	 * @param expr       Expression to evaluate
	 */
	public VDMGeneric evaluateExpression(String expr)
	{		
		VDMGeneric result = null;
		try
		{	
			result = interp.EvalExpression(client, expr);
		}
		catch(APIError ex)
		{
			System.out.println("Evaluate expression error \n" +
							   "Problem statment: " + expr + "\n"+
							   "Check VDM++ interpreter log for more details \n");
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Method to type check all current classes in the model
	 *
	 * @throws APIError Thrown somewhere here...
	 */
	private void typeCheck()
	{
		try
		{
			//get list of modules (classes) in project
			ModuleListHolder moduleList = new ModuleListHolder();
			app.GetProject().GetModules(moduleList);
			
			//create type checker object and type check modules (classes)
			VDMTypeChecker typeCheck = app.GetTypeChecker();
			boolean checkOK = typeCheck.TypeCheckList(moduleList.value);
			if(!checkOK)
			{
				System.out.println("Type checking has encountered errors");
			}
		}
		catch(APIError ex)
		{
			System.out.println("Error occured when type checking");
			ex.printStackTrace();
		}
	}
	
	public VDMFactory getVDMFactory()
	{
		return app.GetVDMFactory();
	}
	
}
