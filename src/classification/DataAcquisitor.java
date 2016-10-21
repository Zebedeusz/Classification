package classification;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class DataAcquisitor 
{
	private int attributesQuantity = 0;
	private int classesQuantity = 0;
	private String dataFileName;
	
    private String[] example = new String[attributesQuantity+1];
    private List <List<String[]>> classes = new ArrayList<>();
    private List <List<List<String[]>>> dividedData = new ArrayList<>();
    
    
    
    public void loadData()
    {
    	getInfoFromUser();
		
    	try 
    	{
    		while (!getDataFromFile(dataFileName))
    		{
    			this.dataFileName = getStringFromUser("path to data file");
    		}
			
    		/*
			int i = 0;
			for (List al : classes)
			{
				System.out.println("Size of class" + i + ": " + al.size());
				i++;
			}
			*/
			System.out.println("Data aquisition finished successfuly.");
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    private boolean getDataFromFile(String dataFileName) throws IOException 
    {
    	BufferedReader dataFileReader = null;
    	
    	try
    	{
    		dataFileReader = new BufferedReader(new FileReader("C:/Users/Micha³/workspace/Classification/src/classification/" + dataFileName));
    		
        	if (!classes.isEmpty())
        		classes.removeAll(classes);
        	
        	String[] classValues = new String[classesQuantity];
        	
        	for (int i = 0; i < classesQuantity; i++)
        	{
        		classes.add(new ArrayList<String[]>());
        		classValues[i] = getStringFromUser("value of class" + i);
        	}
        	
        	while (dataFileReader.ready())
        	{
        		example = dataFileReader.readLine().split(",");
        		
        		for (int i = 0; i < classValues.length; i++)
        		{
        			if (classValues[i].equals(example[attributesQuantity]))
        			{
        				classes.get(i).add(example);
        				break;
        			}
        		}
        	}
        	
        	dataFileReader.close();
        	return true;
    	}
    	catch (FileNotFoundException e)
    	{
    		System.out.println("The file does not exist in this path.");
    		return false;
    	}
    }
    
    public void divideData(int chunks)
    {
		List <List<String[]>> tempListClasses = new ArrayList<>();
		List <String[]> tempListExamples = new ArrayList<>();
		int dividedClassSize;
		
    	for (int i = 0; i < chunks; i++)
    	{
			int k = 0;		
    		dividedData.add(new ArrayList<>());
    		
    		for (Iterator<List<String[]>> iterator = classes.iterator(); iterator.hasNext();) 
    		{
				List <String[]> iterClass = iterator.next();
				dividedClassSize = iterClass.size()/chunks;
				dividedData.get(i).add(new ArrayList<>());
								
				for(int j = dividedClassSize*i; j < dividedClassSize*(i+1); j++)
				{
					dividedData.get(i).get(k).add(iterClass.get(j));
				}
				
				k++;
			}
    	}
    	
    	int j = 0;
    	String[] tempExample = new String[classes.get(0).get(0).length];
    	
    	for (Iterator<List<String[]>> iterator = classes.iterator(); iterator.hasNext();)
    	{
    		List<String[]> tempClasses = iterator.next();	
    		int tempClassesSize = tempClasses.size();
    		
    		if(tempClassesSize%chunks!=0)
    		{
    			for(int i = 0; i < tempClassesSize%chunks; i++)
    			{
    				tempExample = tempClasses.get(tempClassesSize-1-i);
    				dividedData.get(i).get(j).add(tempExample);
    			}
    		}
    		j++;
    	}
    	
    	/*
    	System.out.println("chunks in divedData: " + dividedData.size());
    	j = 0;
    	for (Iterator<List<List<String[]>>> iterator = dividedData.iterator(); iterator.hasNext();)
    	{
    		List <List<String[]>> tempClasses = iterator.next();
    		int nc = tempClasses.size();
    		System.out.println("classes in chunk" + j + ": " + nc);
    		for(int i = 0; i < nc; i++)
    		{
    			System.out.println("examples in class" + i + ": " + tempClasses.get(i).size());
    		}
    		j++;
    		
    	} 
    	*/   	
    }
    
    
    
    
    
    
    
    public List<List<String[]>> getData()
    {
    	return this.classes;
    }
    
    public List<List<String[]>> getTrainingData(int chunkFrom, int chunkTo)
    {
    	List<List<String[]>> trainingData = new ArrayList<>();
    	List<String[]> dataClass;
    	int j;
    	
    	for (int i = 0; i < classesQuantity; i++)
    	{
    		trainingData.add(new ArrayList<>());
    	}
    	
    	for (int i = chunkFrom; i < chunkTo; i++)
    	{
    		j = 0;
    		for(Iterator<List<String[]>> iterator = dividedData.get(i).iterator(); iterator.hasNext();)
    		{
    			dataClass = iterator.next();
    			
    			for(int k = 0; k < dataClass.size(); k++)
    			{
    				trainingData.get(j).add(dataClass.get(k));
    			}
    			j++;
    		}	
    	}	
    	
    	return trainingData;
    }
     
    public List<List<String[]>> getTestData(int chunkFrom, int chunkTo)
    {
    	List<List<String[]>> testData = new ArrayList<>();
    	List<String[]> dataClass;
    	String[] example;
    	
    	for (int i = 0; i < classesQuantity; i++)
    	{
    		testData.add(new ArrayList<>());
    	}
    	
    	int j = 0;
    	for (int i = chunkFrom; i < chunkTo; i++)
    	{
    		j = 0;
    		for(Iterator<List<String[]>> iterator = dividedData.get(i).iterator(); iterator.hasNext();)
    		{
    			dataClass = iterator.next();
    			
    			for(int k = 0; k < dataClass.size(); k++)
    			{
    				example = dataClass.get(k);
    				example[attributesQuantity] = "";
    				testData.get(j).add(example);
    			}
    			j++;
    		}	
    	}	
    	
    	
    	
    	return testData;
    }
    
    private void getInfoFromUser()
    {
    	/*dataFileName = getStringFromUser("path to data file");
    	
    	while(attributesQuantity == 0)
    		attributesQuantity = getValueFromUser("quantity of attributes");
    	
    	while(classesQuantity == 0)
    		classesQuantity = getValueFromUser("quantity of classes");*/
    	dataFileName = "car.data.txt";
    	attributesQuantity = 6;
    	classesQuantity = 4;
    	
    }
    
    public int getValueFromUser(String valueName)
    {
    	Scanner sc = new Scanner(System.in);
    	int value = 0;
        System.out.print("Enter " + valueName + ": ");
        
        try
        {
            value = sc.nextInt();
        }

        catch(InputMismatchException e)
        {
        	System.out.println("Unaccepted data type inserted");
        	value = 0;
        }
        
        return value;
    }
    
    public String getStringFromUser(String valueName)
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter " + valueName + ": ");
        
        String value = sc.next();
        
        return value;
    }

}
