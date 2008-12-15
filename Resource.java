class Resource
{

	String id, classification;
	int tokens, baseCost;
	
	/*
	* Resources attributes to be used in both the access control side of the model, in determining
	* how agent jobs should be best allocated, and also in the scoring mechanism
	*/
	public Resource(String id, String classification, int tokens, int baseCost)
	{
		this.id = id;
		this.classification = classification;
		this.tokens = tokens;
		this.baseCost = baseCost;
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getClassification()
	{
		return classification;
	}
	
	public int getBaseCost()
	{
		return baseCost;
	}
	
	public int getTokens()
	{
		return tokens;
	}
	
	public String toString()
	{
		return id+" - "+classification +" - "+tokens+" - "+baseCost;//+" - ";+importanceLevel; 
	}


}