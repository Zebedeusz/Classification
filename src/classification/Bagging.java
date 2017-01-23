package classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bagging extends Classifier
{
	private final int QNT_OF_BAYES_CLASSIFIERS;
	private final double PORTION_OF_TRAINING_DATA_IN_SUBSAMPLE;
	private Bayes[] setOfBayesClassifiers;
	
	Bagging(int qntOfBayesClassifiers, double portionOfTrainingDataInSubsample)
	{
		this.QNT_OF_BAYES_CLASSIFIERS = qntOfBayesClassifiers;
		this.PORTION_OF_TRAINING_DATA_IN_SUBSAMPLE = portionOfTrainingDataInSubsample;
		this.setOfBayesClassifiers = new Bayes[QNT_OF_BAYES_CLASSIFIERS];
		
		for(int i = 0; i < QNT_OF_BAYES_CLASSIFIERS; i++)
			setOfBayesClassifiers[i] = new Bayes();
	}
	
	private List<List<String[]>> getSubsampleOfTrainingData()
	{
		List<List<String[]>> subsample = new ArrayList<>();
		
		for(int i = 0; i < trainingData.size(); i++)
		{
			subsample.add(new ArrayList<>());
			
			int sizeOfSubSampleInTheClass = (int) (trainingData.get(i).size()*PORTION_OF_TRAINING_DATA_IN_SUBSAMPLE);
			for(int j = 0; j < sizeOfSubSampleInTheClass; j++)
				subsample.get(i).add(trainingData.get(i).get((int) (Math.random()*((double) trainingData.get(i).size()))));
		}
		return subsample;
	}
	
	public void classifyExamples(List<String[]> testData)
	{
		this.classifiedData = new ArrayList<String[]>();
		
		for(Bayes bayes : setOfBayesClassifiers)
			bayes.setTrainingData(getSubsampleOfTrainingData());
		
		for(String[] example : testData)
		{
			HashMap<String, Integer> qntOfClassifiedClasses = new HashMap<>();;

			for(Bayes bayes : setOfBayesClassifiers)
			{
				String classForExample = bayes.classifyExample(example);
				
				if(qntOfClassifiedClasses.containsKey(classForExample))
					qntOfClassifiedClasses.put(classForExample, qntOfClassifiedClasses.get(classForExample) + 1);
				else
					qntOfClassifiedClasses.put(classForExample, 1);
			}
			
			int max = 0;
			String maxClass = "";
			for (Map.Entry<String, Integer> entry : qntOfClassifiedClasses.entrySet())			
			{
				if(entry.getValue() > max)
				{
					max = entry.getValue();
					maxClass = entry.getKey();
				}
			}
			
			String[] tempExample = new String[example.length+1];
			for(int i = 0; i < example.length; i++)
				tempExample[i] = example[i];
			
			tempExample[tempExample.length-1] = maxClass;
			this.classifiedData.add(example);
		}
		
		
	}
}
