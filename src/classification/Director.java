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
	String dataFileName;
	int classesQuantity;
	int attributesQuantity;
	String[] classValues;
	
	boolean dataLoaded = false;
	boolean crossValidPerformed = false;
	
	private void dataLoading()
	{
		String dataFileName = userInteractor.getStringFromUser("path to a data file");
		int classesQuantity = userInteractor.getValueFromUser("quantity of classes in the dataset");
		int attributesQuantity = userInteractor.getValueFromUser("quantity of attributes in examples");
		
		String[] classValues = new String[classesQuantity];
		for(int i = 0; i < classesQuantity; i++)
			classValues[i] = userInteractor.getStringFromUser("name of class " + i);
		
		dataAcq.initialise(dataFileName, classesQuantity, attributesQuantity, classValues);
		
		while(!dataAcq.getDataFromFile(dataFileName))
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
		if(!this.dataLoaded)
		{
			System.out.println("Dataset has to be loaded before performing this operation.");
			return;
		}
		
		if(!this.crossValidPerformed)
		{
			System.out.println("Cross validation has to be done before performing this operation.");
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
			this.crossValidPerformed = true;
			break;
		case(6):
			System.exit(0);
		case(7):
			
		}
	}
	
	public void beginWork()
	{

		processUserChoice(ensureCorrectEnteredMenuValue(userInteractor.displayMenu(), 1, 7));
    	
	}
}
