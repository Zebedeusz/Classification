package classification;

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
	private Bayes bayes = new Bayes();
	private F_Measure fMeasure = F_Measure.getInstance();
	
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
		String[] attrNumbers = userInteractor.getStringArrayFromUser("numbers of attributes to discretize");
		int bins = userInteractor.getValueFromUser("quantity of bins for the attribute values");
		
		switch(typeOfDiscretization)
		{
		case(1):
			for(int i = 0; i < attrNumbers.length; i++)
				dataAcq.discretizeAttributeByWidth(Integer.parseInt(attrNumbers[i]), bins);
			break;
		
		case(2):
			for(int i = 0; i < attrNumbers.length; i++)
				dataAcq.discretizeAttributeByFrequency(Integer.parseInt(attrNumbers[i]), bins);
			break;
		}
		
		System.out.println("Discretization of attributes into " + bins + " bins performed successfuly");
	}
	
	private void classify()
	{		
		switch(ensureCorrectEnteredClassifierValue(userInteractor.displayAvailableClassifiers(), 1, 1))
		{
		case(1):
			int dividedDataSize = dataAcq.getDividedData().size();
			String filePath = dataAcq.getDataFileLocation() + dataAcq.getDataFileName() + "_measures";
			int[][][] scores = new int[dividedDataSize][dataAcq.getClassesQuantity()][dataAcq.getClassesQuantity()];
			double[][] recalls = new double[dividedDataSize][dataAcq.getClassesQuantity()];
			double[][] precisions = new double[dividedDataSize][dataAcq.getClassesQuantity()];
			double[] accuracies = new double[dividedDataSize];
			//System.out.println("Size of divededData: " + dividedDataSize);
			for(int i = 0; i < dividedDataSize; i++)
			{
				dataAcq.clearTrainingData();
				dataAcq.clearTestData();
				
				//System.out.println("Iteration: " + i);
				
				dataAcq.appendTestData(i, i+1);
				
				if(i+1 != dividedDataSize)
					dataAcq.appendTrainingData(i+1, dividedDataSize);
				if(i != 0)
					dataAcq.appendTrainingData(0, i);
				
				bayes.setTrainingData(dataAcq.getTrainingData());
				bayes.classifyExamples(dataAcq.getTestData());
				fMeasure.calculateScores(bayes.getClassifiedData(), dataAcq.getClassesQuantity(), dataAcq.getClassValues());
				
				scores[i] = fMeasure.getScores();
				recalls[i] = fMeasure.getRecall();
				precisions[i] = fMeasure.getPrecision();
				accuracies[i] = fMeasure.getAccuracy();
				
				//fMeasure.writeMeasuresToFile(filePath, dataAcq.getClassesQuantity(), dataAcq.getClassValues());
			}
			
			System.out.println("Classification performed successfuly.");
			
			fMeasure.calculateMeanAccuracy(accuracies);
			fMeasure.calculateMeanPrecisions(precisions);
			fMeasure.calculateMeanRecalls(recalls);
			fMeasure.calculateMeanScores(scores);
			
			fMeasure.writeMeansOfMeasuresToFile(filePath, dataAcq.getClassesQuantity(), dataAcq.getClassValues());
			System.out.println("Confusion matrix with f-measures saved to a file.");
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
			//bayes.writeDataToFile(userInteractor.getStringFromUser("full path where to save the data"), dataAcq.getAttributesQuantity());
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
