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
    	String[] dataSets = {"seeds.data.txt", "winequality-red.txt"};
    	//String[] dataSets = {"glass.data.txt"};
    	int[] crossValidArray = {2,3,5,10};
    	int[] qntOfClassifiers = {5,10,20,50};
    	double[] portionOfTrData = {0.6,0.8,1.0};

    	for(String dataSet : dataSets)
    	{
        	director.dataAcq.initialise(dataSet);
        	director.dataAcq.getDataFromFile();
        	for(int i = 0; i < director.dataAcq.getAttributesQuantity(); i++)
        		director.dataAcq.discretizeAttributeByFrequency(i, 5);
        	
        	for(int div : crossValidArray)
        	{
        		director.dataAcq.divideData(div);
        		//director.classifyBayes();
        		
        		for(int qnt : qntOfClassifiers)
        		{
        			for(double portion : portionOfTrData)
        			{
                		//director.classifyBagging(qnt, portion);
        		    	director.classifyBoosting(qnt, portion);
        			}
        		}
        	}
    	}
    }
}
