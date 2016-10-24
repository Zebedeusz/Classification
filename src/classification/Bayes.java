package classification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bayes extends Classifier 
{
	private double classProbability(int classNumber)
	{
		int sizeOfDataSet = 0;
		
		for(List<String[]> ar : trainingData)
		{
			sizeOfDataSet += ar.size(); 
		}
		
		return trainingData.get(classNumber).size()/((double)sizeOfDataSet);
	}
	
	private double attributeProbability(int attributeNumber, String attributeValue, int classNumber)
	{
		int attributeValuesInClass = 0;
		
		for(Iterator<String[]> iterator = trainingData.get(classNumber).iterator(); iterator.hasNext();)
		{
			if(iterator.next()[attributeNumber].equals(attributeValue))
				attributeValuesInClass++;
		}
		
		return (((double)attributeValuesInClass)/trainingData.get(classNumber).size())+1;
	}
	
	private String classifyExample(String[] example)
	{
		double[] P = new double[trainingData.size()]; 
		double pAttr;
		double maxP;
		int maxPClass;
		
		for(int i = 0; i < trainingData.size(); i++)
		{
			P[i] = 1; pAttr = 1;
			
			for(int j = 0; j < example.length-1; j++)
			{
				pAttr *= attributeProbability(j, example[j], i);
				//System.out.println("pAttr: " + pAttr);
			}
			
			P[i] = classProbability(i)*pAttr;
			//System.out.println("P["+i+"]: " + P[i]);
		}
		
		maxP = P[0];
		maxPClass = 0;
		for(int i = 0; i < P.length; i++)
		{
			if (P[i] > maxP)
			{
				maxP = P[i];
				maxPClass = i;
			}
		}
		
		return trainingData.get(maxPClass).get(0)[trainingData.get(maxPClass).get(0).length-1];
	}
	
	public List<String[]>  classifyExamples(List<String[]> testData)
	{
		this.classifiedData = new ArrayList<String[]>();
		String[] example = new String[testData.get(0).length+1];
		String[] tempExample;
		for(Iterator<String[]> iterator = testData.iterator(); iterator.hasNext();)
		{	
			example = new String[testData.get(0).length+1];
			tempExample = iterator.next();
			for(int i = 0; i < example.length-1; i++)
				example[i] =  tempExample[i];
			
			example[example.length-1] = classifyExample(tempExample);
			this.classifiedData.add(example);
		}
		
		return classifiedData;
	}
}
