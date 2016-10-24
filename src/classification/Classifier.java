package classification;

import java.util.List;

public class Classifier 
{
	protected List<List<String[]>> trainingData;
	//protected List<List<String[]>> testData;
	protected List<String[]> classifiedData;
	
	public void setTrainingData(List<List<String[]>> trainingData)
	{
		this.trainingData = trainingData;
	}
/*
	public void setTestData(List<List<String[]>> testData)
	{
		this.testData = testData;
	}
	*/
	public List<String[]> getClassifiedData()
	{
		return classifiedData;
	}
	
	
}
