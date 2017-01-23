package classification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bayes extends Classifier 
{
	private boolean normalize = false;
	private double[][] means;
	private double[][] variations;
	
	public boolean isNormalize() {
		return normalize;
	}

	public void setNormalize(boolean normalize) {
		this.normalize = normalize;
	}

	private double meanValue(int classNumber, int attributeNumber)
	{
		double mean = 0;
		
		for(Iterator <String[]> exampleIterator = trainingData.get(classNumber).iterator(); exampleIterator.hasNext();)
		{
			mean += Double.parseDouble(exampleIterator.next()[attributeNumber]);
		}
		
		mean /= trainingData.get(classNumber).size();
		
		means[classNumber][attributeNumber] = mean;
		return mean;
	}
	
	private double getMeanValue(int classNumber, int attributeNumber)
	{
		if(means==null)
			means = new double[trainingData.size()][trainingData.get(0).get(0).length];
		
		if(means[classNumber][attributeNumber]!=0)
			return means[classNumber][attributeNumber];
		else
			return meanValue(classNumber, attributeNumber);
		
	}
	
	private double variationValue(int classNumber, int attributeNumber)
	{
		double variation = 0;
		
		for(Iterator <String[]> exampleIterator = trainingData.get(classNumber).iterator(); exampleIterator.hasNext();)
		{
			variation += Math.pow((Double.parseDouble(exampleIterator.next()[attributeNumber])-means[classNumber][attributeNumber]),2);
		}
		
		variation /= trainingData.get(classNumber).size();
		
		return variation;
	}
	
	private double getVariationValue(int classNumber, int attributeNumber)
	{
		if(variations == null)
			variations = new double[trainingData.size()][trainingData.get(0).get(0).length];
		
		if(variations[classNumber][attributeNumber]!=0)
			return variations[classNumber][attributeNumber];
		else
			return variationValue(classNumber, attributeNumber);	
	}

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
	
	private double attributeProbabilityNormalized(int attributeNumber, double attributeValue, int classNumber)
	{
		double mean = getMeanValue(classNumber, attributeNumber);
		double variation = getVariationValue(classNumber, attributeNumber);
		
		double p = ( 1 / (Math.sqrt(variation) * (Math.sqrt(2 * Math.PI) ) ))
				* Math.exp(- ( ( Math.pow(attributeValue-mean, 2) ) / (2*variation) ));
		
		return p+1;
	}
	
	public String classifyExample(String[] example)
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
				if(!normalize)
					pAttr *= attributeProbability(j, example[j], i);
				if(normalize)
					pAttr *= attributeProbabilityNormalized(j, Double.parseDouble(example[j]), i);
				
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
		
		//System.out.println(trainingData.get(maxPClass).size());
		return trainingData.get(maxPClass).get(0)[trainingData.get(maxPClass).get(0).length-1];
	}
	
	public void classifyExamples(List<String[]> testData)
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

	}
}
