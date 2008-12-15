import java.util.*;

public class Job implements Comparable<Job>
{
	String job;
	int importanceLevel;

	/*
	* Assigned to agents with to relate to a specific resourceID and a level
	* of importance to the employer
	*/
	public Job(String job, int importance)
	{
		this.job = job;
		this.importanceLevel = importance;
	}
	
	public String getJob()
	{
		return job;
	}
	
	public int getImportanceLevel()
	{
		return importanceLevel;
	}
	
	/*
	* To be used when agents prioritise their jobs
	*/
	public int compareTo(Job other)
	{
		//Job other = (Job)j;
		if (this.getImportanceLevel() < other.getImportanceLevel()) return -1;
		if (this.getImportanceLevel() == other.getImportanceLevel()) return 0;
		return 1;
	}




}