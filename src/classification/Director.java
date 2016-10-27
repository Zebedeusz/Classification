package classification;

import java.util.List;

public class Director 
{
	
	//singleton 
	private static final Director instance = new Director();
	
	private Director(){};
	
	public static Director getInstance()
	{
		return instance;
	}
	
	private DataAcquisitor dataAcq = DataAcquisitor.getInstance();
	private UserInteractor userInteractor = UserInteractor.getInstance();
	
	private boolean dataLoaded = false;
	private boolean crossValidPerformed = false;
	
	private void dataLoading()
	{
		String dataFileName = userInteractor.getStringFromUser("path to a data file");
		
		dataAcq.initialise(dataFileName);
		
		while(!dataAcq.getDataFromFile())
		{
			System.out.println("Incorrect path to a data file. Try again.");
			dataFileName = userInteractor.getStringFromUser("path to a data file");	
		}
	}
	
	private void discretize(int typeOfDiscretization)
	{
		int attrNumber = userInteractor.getValueFromUser("number of attribute to discretize");
		int bins = userInteractor.getValueFromUser("quantity of bins for the attribute values");
		
		switch(typeOfDiscretization)
		{
		case(1):
			dataAcq.discretizeAttributeByWidth(attrNumber, bins);
			break;
		
		case(2):
			dataAcq.discretizeAttributeByFrequency(attrNumber, bins);
			break;
		}
		
		System.out.println("Discretization of attribute number " + attrNumber + " into " + bins + "bins performed successfuly");
	}
	
	private void classify()
	{		
		switch(ensureCorrectEnteredClassifierValue(userInteractor.displayAvailableClassifiers(), 1, 1))
		{
		case(1):
			Bayes bayes = new Bayes();
			F_Measure fMeasure = F_Measure.getInstance();
			int dividedDataSize = dataAcq.getDividedData().size();
			int[][] scores;
			//System.out.println("Size of divededData: " + dividedDataSize);
			for(int i = 0; i < dividedDataSize; i++)
			{
				dataAcq.clearTrainingData();
				dataAcq.clearTestData();
				
				System.out.println("Iteration: " + i);
				
				dataAcq.appendTestData(i, i+1);
				
				if(i+1 != dividedDataSize)
					dataAcq.appendTrainingData(i+1, dividedDataSize);
				if(i != 0)
					dataAcq.appendTrainingData(0, i);
				
				bayes.setTrainingData(dataAcq.getTrainingData());
				//System.out.println("Size of trainingData: " + bayes.trainingData.size());
				//System.out.println("Size of testData: " + dataAcq.getTestData().size());
				bayes.classifyExamples(dataAcq.getTestData());
				fMeasure.calculateScores(bayes.getClassifiedData(), dataAcq.getClassesQuantity(), dataAcq.getClassValues());
				
				scores = fMeasure.getScores();
				
				System.out.print("		");
				for(int j = 0; j < dataAcq.getClassValues().length; j++)
				{
					System.out.print(dataAcq.getClassValues()[j] + " ");
				}
				
				for(int k = 0; k < scores.length; k++)
				{
					System.out.print("\n" + dataAcq.getClassValues()[k] + " ");
					
					for(int l = 0; l < scores.length; l++)
						System.out.print("\n" + scores[k][l]);
					
				}

				
				for(int j = 0; j < fMeasure.getPrecision().length; j++)
				{
					System.out.println("Precision: " + fMeasure.getPrecision()[j]);
					System.out.println("Recall: " + fMeasure.getRecall()[j]);
				}

				System.out.println("Accuracy: " + fMeasure.getAccuracy());
			}
			break;
		}
	}
	
	private int ensureCorrectEnteredClassifierValue(int userChoice, int minValue, int maxValue)
	{
    	while(userChoice < minValue || userChoice > maxValue)
    	{
    			System.out.println("Incorrect choice. Try again.\n");
    			userChoice = userInteractor.displayAvailableClassifiers();
    	}
    	
    	return userChoice;
	}
	
	private int ensureCorrectEnteredMenuValue(int userChoice, int minValue, int maxValue)
	{
    	while(userChoice < minValue || userChoice > maxValue)
    	{
    			System.out.println("Incorrect choice. Try again.\n");
    			userChoice = userInteractor.displayMenu();
    	}
    	
    	return userChoice;
	}
	
	private void processUserChoice(int userChoice)
	{
		if((!this.dataLoaded) && (userChoice > 1 && userChoice < 7))
		{
			System.out.println("Dataset has to be loaded before performing this operation.");
			processUserChoice(ensureCorrectEnteredMenuValue(userInteractor.displayMenu(), 1, 7));
			return;
		}
		
		else if((!this.crossValidPerformed) && (userChoice == 6))
		{
			System.out.println("Crossvalidation has to be done before performing this operation.");
			processUserChoice(ensureCorrectEnteredMenuValue(userInteractor.displayMenu(), 1, 7));
			return;
		}
			
		switch(userChoice)
		{
		case(1):
			dataLoading();
			this.dataLoaded = true;
			this.crossValidPerformed = false;
			break;
		case(2):
			discretize(1);
			break;
		case(3):
			discretize(2);
			break;
		case(4):
			dataAcq.writeDataToFile(userInteractor.getStringFromUser("full path where to save the data"));
			break;
		case(5):
			dataAcq.divideData(userInteractor.getValueFromUser("quantity of data chunks in the dataset"));
			System.out.println("Your data was prepared for crossvalidation.");
			this.crossValidPerformed = true;
			break;
		case(6):
			classify();
			break;
		case(7):
			System.out.println("Closing application.");
			System.exit(0);
			
		}
	}
	
	public void beginWork()
	{
		while(true)
			processUserChoice(ensureCorrectEnteredMenuValue(userInteractor.displayMenu(), 1, 7));
	}
}
