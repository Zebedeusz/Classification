package classification;

import java.util.Iterator;
import java.util.List;

public class Initialiser {

    public static void main(String[] args)
    {
        DataAcquisitor acq = new DataAcquisitor();
        acq.loadData();
        acq.divideData(10);
        
        List<List<String[]>> trainingData;
        trainingData = acq.getTrainingData(0, 5);
        
        System.out.println("Number of classes in trainingData:" + trainingData.size());
        
        for(int i = 0; i < trainingData.size(); i++)
		{
			System.out.println("Number of examples in class" + i +": " + trainingData.get(i).size());
		}
        
        List<List<String[]>> testData;
        testData = acq.getTestData(5, 7);
        
        System.out.println("Number of classes in testData:" + testData.size());
        
        for(int i = 0; i < testData.size(); i++)
		{
			System.out.println("Number of examples in class" + i +": " + testData.get(i).size());
		}
        
        System.out.print("Example from training set: ");
        for(int i  = 0; i < trainingData.get(0).get(0).length; i++)
        {
        	System.out.print(trainingData.get(0).get(0)[i]);
        }
        System.out.println();
        
        System.out.print("Example from test set: ");
        for(int i  = 0; i < testData.get(0).get(0).length; i++)
        {
        	System.out.print(testData.get(0).get(0)[i]);
        }
        System.out.println();
        
    }
    

    
}
