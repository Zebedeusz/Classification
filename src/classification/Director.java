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
	
	public DataAcquisitor dataAcq = DataAcquisitor.getInstance();
	private UserInteractor userInteractor = UserInteractor.getInstance();
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
	
	public void classify()
	{	
		int dividedDataSize = dataAcq.getDividedData().size();
		String filePath = dataAcq.getDataFileLocation() + dataAcq.getDataFileName() + "_measures.txt";
		int[][][] scores = new int[dividedDataSize][dataAcq.getClassesQuantity()][dataAcq.getClassesQuantity()];
		double[][] recalls = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		double[][] precisions = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		double[] accuracies = new double[dividedDataSize];
		double[][] fMeasures = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		
		int chosenClassifier = ensureCorrectEnteredClassifierValue(userInteractor.displayAvailableClassifiers(), 1, 4);
		switch(chosenClassifier)
		{	
		case(1):
		case(2):
			//System.out.println("Size of divededData: " + dividedDataSize);
			
			Bayes bayes = new Bayes();
		
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
				
				if(chosenClassifier == 1)
					bayes.setNormalize(false);
				
				if(chosenClassifier == 2)
					bayes.setNormalize(true);
				
				bayes.setTrainingData(dataAcq.getTrainingData());
				
				bayes.classifyExamples(dataAcq.getTestData());
				
				fMeasure.calculateScores(bayes.getClassifiedData(), dataAcq.getClassesQuantity(), dataAcq.getClassValues());
				
				scores[i] = fMeasure.getScores();
				recalls[i] = fMeasure.getRecall();
				precisions[i] = fMeasure.getPrecision();
				accuracies[i] = fMeasure.getAccuracy();
				fMeasures[i] = fMeasure.getF_Measure();
				
				//fMeasure.writeMeasuresToFile(filePath, dataAcq.getClassesQuantity(), dataAcq.getClassValues());
			}
			
			//bayes.writeDataToFile("/home/michal/workspace/Classification/src/classification/Datasets/Glass/xx.txt", 9);
			break;
		case(3):
			InductiveLearningAlgorithm ILA = new InductiveLearningAlgorithm();
			
			dataAcq.clearTrainingData();
			dataAcq.clearTestData();
			dataAcq.appendTrainingData(2,10);
			dataAcq.appendTestData(0,2);
			
			ILA.setTrainingData(dataAcq.getTrainingData());
			ILA.classifyExamples(dataAcq.getTestData());
			
			break;
			
		case(4):
			
			K_NearestNeighbours kNN = new K_NearestNeighbours(5, DistanceCalculationMethod.Euclides, VotingApproach.democracy);
			
			for(int i = 0; i < dividedDataSize; i++)
			{
				dataAcq.clearTrainingData();
				dataAcq.clearTestData();
				
				dataAcq.appendTestData(i, i+1);
				
				if(i+1 != dividedDataSize)
					dataAcq.appendTrainingData(i+1, dividedDataSize);
				if(i != 0)
					dataAcq.appendTrainingData(0, i);
		
				kNN.setTrainingData(dataAcq.getTrainingData());
				kNN.classifyExamples(dataAcq.getTestData());
				
				fMeasure.calculateScores(kNN.getClassifiedData(), dataAcq.getClassesQuantity(), dataAcq.getClassValues());
				
				scores[i] = fMeasure.getScores();
				recalls[i] = fMeasure.getRecall();
				precisions[i] = fMeasure.getPrecision();
				accuracies[i] = fMeasure.getAccuracy();
				fMeasures[i] = fMeasure.getF_Measure();
			}
			
			break;
		}
		
		System.out.println("Classification performed successfuly.");
		fMeasure.calculateMeanAccuracy(accuracies);
		fMeasure.calculateMeanPrecisions(precisions);
		fMeasure.calculateMeanRecalls(recalls);
		fMeasure.calculateMeanScores(scores);
		fMeasure.calculateMeanF_Measures(fMeasures);
		
		fMeasure.writeMeansOfMeasuresToFile(filePath, dataAcq.getClassesQuantity(), dataAcq.getClassValues());
		System.out.println("Confusion matrix with f-measures saved to a file.");
	}
	
	public void classifyKNN(int k, DistanceCalculationMethod distanceCalculationMethod, VotingApproach votingApproach)
	{
		int dividedDataSize = dataAcq.getDividedData().size();
		String filePath = dataAcq.getDataFileLocation() + "wyniki.csv";
		//String filePath = dataAcq.getDataFileLocation() + dataAcq.getDataFileName().split("\\.")[0] + "/" + dividedDataSize + "/" + String.valueOf(k) + "_" + distanceCalculationMethod.toString() + "_" + votingApproach.toString() + ".csv";
		int[][][] scores = new int[dividedDataSize][dataAcq.getClassesQuantity()][dataAcq.getClassesQuantity()];
		double[][] recalls = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		double[][] precisions = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		double[] accuracies = new double[dividedDataSize];
		double[][] fMeasures = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		
		K_NearestNeighbours kNN = new K_NearestNeighbours(k, distanceCalculationMethod, votingApproach);
		
		for(int i = 0; i < dividedDataSize; i++)
		{
			dataAcq.clearTrainingData();
			dataAcq.clearTestData();
			
			dataAcq.appendTestData(i, i+1);
			
			if(i+1 != dividedDataSize)
				dataAcq.appendTrainingData(i+1, dividedDataSize);
			if(i != 0)
				dataAcq.appendTrainingData(0, i);
	
			kNN.setTrainingData(dataAcq.getTrainingData());
			kNN.classifyExamples(dataAcq.getTestData());
			
			fMeasure.calculateScores(kNN.getClassifiedData(), dataAcq.getClassesQuantity(), dataAcq.getClassValues());
			
			scores[i] = fMeasure.getScores();
			recalls[i] = fMeasure.getRecall();
			precisions[i] = fMeasure.getPrecision();
			accuracies[i] = fMeasure.getAccuracy();
			fMeasures[i] = fMeasure.getF_Measure();
		}
			//System.out.println("Classification performed successfuly.");
			fMeasure.calculateMeanAccuracy(accuracies);
			fMeasure.calculateMeanPrecisions(precisions);
			fMeasure.calculateMeanRecalls(recalls);
			fMeasure.calculateMeanScores(scores);
			fMeasure.calculateMeanF_Measures(fMeasures);
			fMeasure.caseName =  dataAcq.getDataFileName().split("\\.")[0] + "_" + dividedDataSize + "_" + k + "_" + distanceCalculationMethod.toString() + "_" + votingApproach.toString();
			fMeasure.writeMeansOfMeasuresToFile(filePath, dataAcq.getClassesQuantity(), dataAcq.getClassValues());
			//System.out.println("Confusion matrix with f-measures saved to a file.");
			System.out.println(fMeasure.caseName + " saved");
	}
	
	public void classifyBayes()
	{
		int dividedDataSize = dataAcq.getDividedData().size();
		String filePath = dataAcq.getDataFileLocation() +  dataAcq.getDataFileName().split("\\.")[0] +"_wyniki.csv";
		int[][][] scores = new int[dividedDataSize][dataAcq.getClassesQuantity()][dataAcq.getClassesQuantity()];
		double[][] recalls = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		double[][] precisions = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		double[] accuracies = new double[dividedDataSize];
		double[][] fMeasures = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		
		Bayes bayes = new Bayes();
		
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
			
			bayes.buildClassifier();
			
			bayes.classifyExamples(dataAcq.getTestData());
			
			fMeasure.calculateScores(bayes.getClassifiedData(), dataAcq.getClassesQuantity(), dataAcq.getClassValues());
			
			scores[i] = fMeasure.getScores();
			recalls[i] = fMeasure.getRecall();
			precisions[i] = fMeasure.getPrecision();
			accuracies[i] = fMeasure.getAccuracy();
			fMeasures[i] = fMeasure.getF_Measure();
		}
		
		fMeasure.caseName =  dataAcq.getDataFileName().split("\\.")[0] + "_" + "bayes_" + dividedDataSize;

		System.out.println("Bayes finished:" + fMeasure.caseName );
		fMeasure.calculateMeanAccuracy(accuracies);
		fMeasure.calculateMeanPrecisions(precisions);
		fMeasure.calculateMeanRecalls(recalls);
		fMeasure.calculateMeanScores(scores);
		fMeasure.calculateMeanF_Measures(fMeasures);
		
		fMeasure.writeMeansOfMeasuresToFile(filePath, dataAcq.getClassesQuantity(), dataAcq.getClassValues());
		System.out.println("Confusion matrix with f-measures saved to a file.");
	}
	
	public void classifyBagging(int qntOfBayesClassifiers, double portionOfTrainingDataInSubsample)
	{
		int dividedDataSize = dataAcq.getDividedData().size();
		String filePath = dataAcq.getDataFileLocation() +  dataAcq.getDataFileName().split("\\.")[0] +"_wyniki.csv";
		int[][][] scores = new int[dividedDataSize][dataAcq.getClassesQuantity()][dataAcq.getClassesQuantity()];
		double[][] recalls = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		double[][] precisions = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		double[] accuracies = new double[dividedDataSize];
		double[][] fMeasures = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		
		Bagging bagging = new Bagging(qntOfBayesClassifiers, portionOfTrainingDataInSubsample);
		
		for(int i = 0; i < dividedDataSize; i++)
		{
			dataAcq.clearTrainingData();
			dataAcq.clearTestData();
			
			dataAcq.appendTestData(i, i+1);
			
			if(i+1 != dividedDataSize)
				dataAcq.appendTrainingData(i+1, dividedDataSize);
			if(i != 0)
				dataAcq.appendTrainingData(0, i);
			
			bagging.setTrainingData(dataAcq.getTrainingData());
			
			bagging.classifyExamples(dataAcq.getTestData());
			
			fMeasure.calculateScores(bagging.getClassifiedData(), dataAcq.getClassesQuantity(), dataAcq.getClassValues());
			
			scores[i] = fMeasure.getScores();
			recalls[i] = fMeasure.getRecall();
			precisions[i] = fMeasure.getPrecision();
			accuracies[i] = fMeasure.getAccuracy();
			fMeasures[i] = fMeasure.getF_Measure();
		}
		
		fMeasure.caseName =  dataAcq.getDataFileName().split("\\.")[0] + "_" + "bagging_" + dividedDataSize + "_" + qntOfBayesClassifiers + "_" + portionOfTrainingDataInSubsample;

		System.out.println("Bagging finished:" + fMeasure.caseName );
		fMeasure.calculateMeanAccuracy(accuracies);
		fMeasure.calculateMeanPrecisions(precisions);
		fMeasure.calculateMeanRecalls(recalls);
		fMeasure.calculateMeanScores(scores);
		fMeasure.calculateMeanF_Measures(fMeasures);

		fMeasure.writeMeansOfMeasuresToFile(filePath, dataAcq.getClassesQuantity(), dataAcq.getClassValues());
		System.out.println("Confusion matrix with f-measures saved to a file.");
	}
	
	public void classifyBoosting(int qntOfBayesClassifiers, double portionOfTrainingDataInSubsample)
	{
		int dividedDataSize = dataAcq.getDividedData().size();
		String filePath = dataAcq.getDataFileLocation() +  dataAcq.getDataFileName().split("\\.")[0] +"_wyniki.csv";
		int[][][] scores = new int[dividedDataSize][dataAcq.getClassesQuantity()][dataAcq.getClassesQuantity()];
		double[][] recalls = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		double[][] precisions = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		double[] accuracies = new double[dividedDataSize];
		double[][] fMeasures = new double[dividedDataSize][dataAcq.getClassesQuantity()];
		
		Boosting boosting = new Boosting(qntOfBayesClassifiers, portionOfTrainingDataInSubsample);
		
		for(int i = 0; i < dividedDataSize; i++)
		{
			dataAcq.clearTrainingData();
			dataAcq.clearTestData();
			
			dataAcq.appendTestData(i, i+1);
			
			if(i+1 != dividedDataSize)
				dataAcq.appendTrainingData(i+1, dividedDataSize);
			if(i != 0)
				dataAcq.appendTrainingData(0, i);
			
			boosting.setTrainingData(dataAcq.getTrainingData());
			
			boosting.classifyExamples(dataAcq.getTestData());
			
			fMeasure.calculateScores(boosting.getClassifiedData(), dataAcq.getClassesQuantity(), dataAcq.getClassValues());
			
			scores[i] = fMeasure.getScores();
			recalls[i] = fMeasure.getRecall();
			precisions[i] = fMeasure.getPrecision();
			accuracies[i] = fMeasure.getAccuracy();
			fMeasures[i] = fMeasure.getF_Measure();
		}
		
		fMeasure.caseName =  dataAcq.getDataFileName().split("\\.")[0] + "_" + "boosting_" + dividedDataSize + "_" + qntOfBayesClassifiers + "_" + portionOfTrainingDataInSubsample;

		System.out.println("Boosting finished:" + fMeasure.caseName );
		fMeasure.calculateMeanAccuracy(accuracies);
		fMeasure.calculateMeanPrecisions(precisions);
		fMeasure.calculateMeanRecalls(recalls);
		fMeasure.calculateMeanScores(scores);
		fMeasure.calculateMeanF_Measures(fMeasures);
		
		fMeasure.writeMeansOfMeasuresToFile(filePath, dataAcq.getClassesQuantity(), dataAcq.getClassValues());
		System.out.println("Confusion matrix with f-measures saved to a file.");
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
	
	private int ensureCorrectEnteredDiscretizerValue(int userChoice, int minValue, int maxValue)
	{
    	while(userChoice < minValue || userChoice > maxValue)
    	{
    			System.out.println("Incorrect choice. Try again.\n");
    			userChoice = userInteractor.displayAvailableDiscretizers();
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
			dataAcq.standarizeData();
			System.out.println("Your data was standarized.");
			break;
		case(3):
			discretize(ensureCorrectEnteredDiscretizerValue(userInteractor.displayAvailableDiscretizers(), 1, 2));
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
