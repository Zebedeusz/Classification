package classification;

public class Initialiser {

    public static void main(String[] args)
    {
    	/*
    	Director director = Director.getInstance();
    	director.beginWork();
    	*/
    	Director director = Director.getInstance();
    	/*
    	director.dataAcq.initialise("glass.data.txt");
    	director.dataAcq.getDataFromFile();
    	director.dataAcq.standarizeData();
    	director.dataAcq.divideData(2);
*/
    	
    	//String[] dataSets = {"seeds.data.txt", "glass.data.txt", "wine.data.txt", "winequality-red.txt", "winequality-white.txt"};
    	String[] dataSets = {"wine.data.txt"};
    	int[] crossValidArray = {5};
    	
    	for(String dataSet : dataSets)
    	{
        	director.dataAcq.initialise(dataSet);
        	director.dataAcq.getDataFromFile();
        	for(int i = 0; i < director.dataAcq.getAttributesQuantity(); i++)
        		director.dataAcq.discretizeAttributeByFrequency(i, 5);
        	
        	for(int div : crossValidArray)
        	{
        		director.dataAcq.divideData(div);
        		
        		director.classifyBayes();
        		director.classifyBagging(5, 0.8);
		    	director.classifyBoosting(5, 0.8);
        	}
    	}
    	
    	/*
    	String[] dataSets = {"seeds.data.txt", "glass.data.txt", "wine.data.txt", "winequality-red.txt", "winequality-white.txt"};
    	int[] crossValidArray = {2};
    	int[] kArray = {3,5,7};
    	DistanceCalculationMethod[] dstCalcMeth = {DistanceCalculationMethod.Euclides, DistanceCalculationMethod.Manhattan, DistanceCalculationMethod.Czebyszew};
    	VotingApproach[] votAppr = {VotingApproach.democracy, VotingApproach.theCloserTheBetter, VotingApproach.doubleWeighted};
    	
    	for(String dataSet : dataSets)
    	{
        	director.dataAcq.initialise(dataSet);
        	director.dataAcq.getDataFromFile();
        	director.dataAcq.standarizeData();
        	
        	for(int div : crossValidArray)
        	{
        		director.dataAcq.divideData(div);
        		
        		for(int k : kArray)
        			for(DistanceCalculationMethod dcm : dstCalcMeth)
        				for(VotingApproach vA : votAppr)
        			    	director.classifyKNN(k, dcm, vA);
        	}
    	}
    	
    	*/
    	/*
    	director.dataAcq.initialise("data2_pred,prep,ppron,conj.txt");
    	director.dataAcq.getDataFromFile();
    	for(int i = 0; i < director.dataAcq.getAttributesQuantity(); i++)
    		director.dataAcq.discretizeAttributeByFrequency(i, 5);
    	director.dataAcq.divideData(3);
    	director.classifyBayes();
    	
    	UserInteractor userInteractor =  UserInteractor.getInstance();
    	int userPick;
    	userPick = userInteractor.displayMenu(1);
    	
    	while(userPick != 1 || userPick != 2)
    	{
    			System.out.println("Incorrect choice. Try again.\n");
    			userPick = userInteractor.displayMenu(1);
    	}
    	
    	if(userPick == 2)
    		System.exit(0);
    	else
    	{
    		DataAcquisitor dataAcq = DataAcquisitor.getInstance();
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
    		
    		userPick = userInteractor.displayMenu(2);
    		
    	}
    	
    	
    	
    	
    	
    	
    	classValues[0] = "Iris-setosa";
    	classValues[1] = "Iris-versicolor";
    	classValues[2] = "Iris-virginica";
    	
    	
    	
		while (!getDataFromFile(dataFileName))
		{
			this.dataFileName = getStringFromUser("path to data file");
		}
		
    	
        
    	/*
    	/*
        DataAcquisitor acq = new DataAcquisitor();
        acq.loadData();
        
        for (int i = 0; i < 4; i++)
        	acq.discretizeAttributeByFrequency(i, 6);
        //acq.writeDataToFile();
        
        acq.divideData(8);
        acq.appendTrainingData(0,4);
        acq.appendTrainingData(6,8);
        
        Bayes bayes = new Bayes();
        bayes.setTrainingData(acq.getTrainingData());            
        List<String[]> classifiedData;
        bayes.classifyExamples(acq.getTestData(4,6));
        classifiedData = bayes.getClassifiedData();
        
        System.out.println("Size of classifiedData: " + classifiedData.size());
        
        int cnt = 0;
        for(Iterator<String[]> iterator = classifiedData.iterator(); iterator.hasNext();)
        {
        	String[] attr = iterator.next();
        	if(attr[attr.length-2].equals(attr[attr.length-1]))
        	{
        		cnt++;
        	}
        	
        	//if(attr[attr.length-2].equals("unacc"))
        		//attrsWithUnacc++;
        	/*
        	for(int i = 0; i < attr.length; i++)
        	{
        		System.out.print(attr[i]+",");
        	}
        	System.out.println();
        	*/
        	
        
       	
        //System.out.println("Matching values: " + cnt);
               
        /*
        System.out.println("Number of classes in trainingData:" + trainingData.size());
        
        for(int i = 0; i < trainingData.size(); i++)
		{
			System.out.println("Number of examples in class" + i +": " + trainingData.get(i).size());
		}
        
        
        
        System.out.println("Number of classes in testData:" + testData.size());
        
        for(int i = 0; i < testData.size(); i++)
		{
			System.out.println("Number of examples in class" + i +": " + testData.get(i).size());
		}
        
        System.out.print("Example from training set: ");
        for(int i  = 0; i < trainingData.get(0).get(0).length; i++)
        {
        	System.out.print(trainingData.get(0).get(0)[i] + ", ");
        }
        System.out.println();
        
        System.out.print("Example from test set: ");
        for(int i  = 0; i < testData.get(0).get(0).length; i++)
        {
        	System.out.print(testData.get(0).get(0)[i] + ", ");
        }
        System.out.println();
        */
        
    
    

    }
}
