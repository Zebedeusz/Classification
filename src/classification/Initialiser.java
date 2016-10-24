package classification;

import java.util.Iterator;
import java.util.List;

public class Initialiser {

    public static void main(String[] args)
    {
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
        classifiedData = bayes.classifyExamples(acq.getTestData(4,6));
        
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
        	
        }
        	
        System.out.println("Matching values: " + cnt);
               
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
