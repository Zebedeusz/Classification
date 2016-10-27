package classification;

import java.util.List;

public abstract class Classifier 
{
	public List<List<String[]>> trainingData;
	public List<String[]> classifiedData;
	
	public void setTrainingData(List<List<String[]>> trainingData)
	{
		this.trainingData = trainingData;
	}

	public List<String[]> getClassifiedData()
	{
		return classifiedData;
	}
	
	
}
