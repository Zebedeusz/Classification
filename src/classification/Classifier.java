package classification;

import java.util.List;

public class Classifier 
{
	protected List<List<String[]>> trainingData;
	protected List<String[]> classifiedData;
	
	public void setTrainingData(List<List<String[]>> trainingData)
	{
		this.trainingData = trainingData;
	}

	public List<String[]> getClassifiedData()
	{
		return classifiedData;
	}
	
	
}
