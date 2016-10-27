package classification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class F_Measure 
{
	private static final F_Measure instance = new F_Measure();
	private int[][] scores;
	private double accuracy;
	private double[] recall;
	private double[] precision;
	
	private F_Measure(){};
	
	public static F_Measure getInstance()
	{
		return instance;
	}
	
	public void writeMeasuresToFile()
	{
		
	}
	
	public void calculateScores(List<String[]> classifiedData, int classesQuantity, String[] classValues)
	{
		List<List<String[]>> classes = new ArrayList<>();
		String[] example;
		int attributesQuantity = classifiedData.get(0).length-2;
		
		this.scores = new int [classesQuantity][classesQuantity];
		setZeroesInScoresArray();
		this.recall = new double[classesQuantity];
		this.precision = new double[classesQuantity]; 
		
    	for (int i = 0; i < classesQuantity; i++)
    		classes.add(new ArrayList<String[]>());
    	
		//moving classifiedData into classes
		for(Iterator<String[]> iterator = classifiedData.iterator(); iterator.hasNext();)
		{	
        	example = iterator.next();
        	
    		for (int i = 0; i < classesQuantity; i++)
    		{
    			if (classValues[i].equals(example[attributesQuantity]))
    			{
    				classes.get(i).add(example);
    				break;
    			}   			
    		}
		}
		
		List<String[]> classifiedClass;
		int classNumber = 0;
		int testOutcomePositive;
		
		//System.out.println("Classes size: " + classes.size());
		for(Iterator<List<String[]>> iteratorClasses = classes.iterator(); iteratorClasses.hasNext();)
		{
			classifiedClass = iteratorClasses.next();
			testOutcomePositive = 0;
			for(Iterator<String[]> iteratorExamples = classifiedClass.iterator(); iteratorExamples.hasNext();)
			{
				example = iteratorExamples.next();
				
				for(int i = 0; i < classesQuantity; i++)
				{
					if(classValues[i].equals(example[example.length-1]))
					{
						scores[classNumber][i]++;
						break;
					}
				}
				
				if(classValues[classNumber].equals(example[example.length-1]))
					testOutcomePositive++;
			}
			if(classifiedClass.size() != 0)
				recall[classNumber] = ((double)scores[classNumber][classNumber]) / ((double)classifiedClass.size());
			else
				recall[classNumber] = -1;
			if(testOutcomePositive != 0)
				precision[classNumber] = ((double)scores[classNumber][classNumber]) / ((double)testOutcomePositive);
			else 
				precision[classNumber] = -1;
			
			classNumber++;
		}
		accuracy = calculateAccuracy(classifiedData.size());
	}
	
	private double calculateAccuracy(int totalSizeOfData)
	{
		int truths = 0;
		
		for(int i = 0; i < scores.length; i++)
			truths += scores[i][i]; 
		
		return ((double)truths)/((double)totalSizeOfData);
	}
	private void setZeroesInScoresArray()
	{
		for(int i = 0; i < this.scores.length; i++)
		{
			for (int j = 0; j < this.scores.length; j++)
			{
				scores[i][j] = 0;
			}
		}
	}
	
	public int[][] getScores()
	{
		return scores;
	}
	
	public double getAccuracy() {
		return accuracy;
	}

	public double[] getRecall() {
		return recall;
	}

	public double[] getPrecision() {
		return precision;
	}
	
}
