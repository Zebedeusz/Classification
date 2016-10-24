package classification;

import java.util.List;

public class Initialiser {

    public static void main(String[] args)
    {
        DataAcquisitor acq = new DataAcquisitor();
        acq.loadData();
        
        //acq.discretizeAttributeByFrequency(0, 10);
        //acq.writeDataToFile();
        
        acq.divideData(10);
        
        List<List<String[]>> trainingData;
        trainingData = acq.getTrainingData(0, 9);
        
        List<String[]> testData;
        testData = acq.getTestData(9, 10);
        
        
        Bayes bayes = new Bayes();
        
        bayes.setTrainingData(trainingData);
        bayes.classifyExamples(testData);
        
        List<String[]> classifiedData;
        classifiedData = bayes.getClassifiedData();
        
        System.out.println("Size of classifiedData: " + classifiedData.size());
        int cnt = 0;
        int attrsWithUnacc= 0;
        for(String[] attr : classifiedData)
        {
        	if(attr[attr.length-2].equals(attr[attr.length-1]))
        	{
        		cnt++;
        	}
        	
        	if(attr[attr.length-1].equals("unacc"))
        		attrsWithUnacc++;
        }
        	
        System.out.println("Matching values: " + cnt);
        System.out.println("Values with unacc: " + attrsWithUnacc); 
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
