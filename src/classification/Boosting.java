package classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Boosting extends Classifier
{
	private final int QNT_OF_BAYES_CLASSIFIERS;
	private final double PORTION_OF_TRAINING_DATA_IN_SUBSAMPLE;
	private Bayes[] setOfBayesClassifiers;
	private double[] wages;
	private List<String[]> flattenedTrainingData;
	
	Boosting(int qntOfBayesClassifiers, double portionOfTrainingDataInSubsample)
	{
		this.QNT_OF_BAYES_CLASSIFIERS = qntOfBayesClassifiers;
		this.PORTION_OF_TRAINING_DATA_IN_SUBSAMPLE = portionOfTrainingDataInSubsample;
		this.setOfBayesClassifiers = new Bayes[QNT_OF_BAYES_CLASSIFIERS];
		
		for(int i = 0; i < QNT_OF_BAYES_CLASSIFIERS; i++)
			setOfBayesClassifiers[i] = new Bayes();
		
		this.flattenedTrainingData = new ArrayList<>();
	}
	
	private List<List<String[]>> getSubsampleOfTrainingData(double[] probabilitiesRanges)
	{
		List<String[]> subsample = new ArrayList<>();
		List<List<String[]>> subsampleForBayes = new ArrayList<>();
		
		int sizeOfSubSample = (int) (flattenedTrainingData.size()*PORTION_OF_TRAINING_DATA_IN_SUBSAMPLE);
		
		for(int j = 0; j < sizeOfSubSample; j++)
		{
			double randomRange = Math.random();
			
			for(int k = 0; k < probabilitiesRanges.length; k++)
					if(probabilitiesRanges[k] > randomRange)
						subsample.add(flattenedTrainingData.get(k));
		}
		
		List<String> classValues = new ArrayList<>();
		for(String[] example : subsample)
		{
			boolean found = false;
			
			for(int i = 0; i < classValues.size(); i++)
				if(classValues.get(i).equals(example[example.length - 1]))
				{subsampleForBayes.get(i).add(example); found = true; break;}
			
			if(!found)
			{
				classValues.add(example[example.length - 1]);
				subsampleForBayes.add(new ArrayList<>());
				subsampleForBayes.get(subsampleForBayes.size() - 1).add(example);
			}
		}
		return subsampleForBayes;
	}
	
	public void classifyExamples(List<String[]> testData)
	{
		this.classifiedData = new ArrayList<String[]>();
		this.flattenedTrainingData = new ArrayList<>();
		
		//trainingData flattening
		this.trainingData.forEach(e -> this.flattenedTrainingData.addAll(e));
		
		wages = new double[flattenedTrainingData.size()];
		
		for(int i = 0; i < flattenedTrainingData.size(); i++)
			wages[i] = (double) (1/((double)flattenedTrainingData.size()));
		
		double[] errors = new double[QNT_OF_BAYES_CLASSIFIERS];
		double[] gammas = new double[QNT_OF_BAYES_CLASSIFIERS];
		
		//training
		int classifierIndex = 0;
		for(Bayes bayes : setOfBayesClassifiers)
		{
			double[] probabilities = new double[wages.length];
			double[] probabilitiesRanges = new double[wages.length];
			String[] classesForTrainingExamples = new String[flattenedTrainingData.size()];
			
			double sumOfWages = 0;
			
			for(int i = 0; i < wages.length; i++)
					sumOfWages += wages[i];
			
			for(int i = 0; i < wages.length; i++)
					probabilities[i] = wages[i] / sumOfWages;
			
			for(int i = 0; i < wages.length; i++)
					probabilitiesRanges[i] = sum(i, probabilities);
			
			bayes.setTrainingData(getSubsampleOfTrainingData(probabilitiesRanges));
			
			errors[classifierIndex] = 0;
			for(int i = 0; i < flattenedTrainingData.size(); i++)
			{
				classesForTrainingExamples[i] = bayes.classifyExample(flattenedTrainingData.get(i));
				
				if(!flattenedTrainingData.get(i)[flattenedTrainingData.get(i).length - 1].equals(classesForTrainingExamples[i]))
					errors[classifierIndex] += probabilities[i];
			}
				
			gammas[classifierIndex] = Math.log10((1-errors[classifierIndex])/errors[classifierIndex]);
			
			//wages update
			for(int i = 0; i < flattenedTrainingData.size(); i++)
				if(!flattenedTrainingData.get(i)[flattenedTrainingData.get(i).length - 1].equals(classesForTrainingExamples[i]))
					wages[i] = wages[i]*Math.exp(gammas[classifierIndex]);
			
			classifierIndex++;
		}
		
		//classification
		for(int i = 0; i < flattenedTrainingData.size(); i++)
		{
			HashMap<String, Double> wagesOfClassifiedClasses = new HashMap<>();;

			classifierIndex = 0;
			for(Bayes bayes : setOfBayesClassifiers)
			{
				String classForExample = bayes.classifyExample(flattenedTrainingData.get(i));
				
				if(wagesOfClassifiedClasses.containsKey(classForExample))
					wagesOfClassifiedClasses.put(classForExample, wagesOfClassifiedClasses.get(classForExample) + gammas[classifierIndex]);
				else
					wagesOfClassifiedClasses.put(classForExample, gammas[classifierIndex]);
				
				classifierIndex++;
			}
			
			double max = 0;
			String maxClass = "";
			for (Map.Entry<String, Double> entry : wagesOfClassifiedClasses.entrySet())			
			{
				if(entry.getValue() > max)
				{
					max = entry.getValue();
					maxClass = entry.getKey();
				}
			}
			
			String[] tempExample = new String[flattenedTrainingData.get(i).length+1];
			for(int j = 0; j < flattenedTrainingData.get(i).length; j++)
				tempExample[j] = flattenedTrainingData.get(i)[j];
			
			tempExample[tempExample.length-1] = maxClass;
			this.classifiedData.add(tempExample);
		}
		


		
		
	}
	
	private double sum(int elementIndex, double[] probabilities )
	{
		double probabilityRange = 0;
		
		for(int i = 0; i <= elementIndex; i++)
				probabilityRange += probabilities[i];
		
		return probabilityRange;
	}
	
	
}
