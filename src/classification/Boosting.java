package classification;

import java.util.ArrayList;
import java.util.List;

public class Boosting extends Classifier
{
	private final int QNT_OF_BAYES_CLASSIFIERS;
	private final double PORTION_OF_TRAINING_DATA_IN_SUBSAMPLE;
	private Bayes[] setOfBayesClassifiers;
	
	Boosting(int qntOfBayesClassifiers, double portionOfTrainingDataInSubsample)
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
	
	
}
