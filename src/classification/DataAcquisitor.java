package classification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class DataAcquisitor 
{
	private int attributesQuantity;
	private int classesQuantity;
	private String dataFileName;
	private String dataFileLocation = "/home/michal/workspace/Classification/src/classification/Datasets/Car/";
	String[] classValues;
	
    private String[] example;
    private List <List<String[]>> classes;
    private List <List<List<String[]>>> dividedData;
    
    private Scanner sc;
    
    
    private void initialise()
    {
    	this.classValues = new String[classesQuantity];
    	this.example = new String[attributesQuantity+1];
    	this.dividedData = new ArrayList<>();
    	this.classes = new ArrayList<>();
    	this.sc = new Scanner(System.in);
    }
    
    public void loadData()
    {
    	getInfoFromUser();
    	initialise();
    	
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
    		dataFileReader = new BufferedReader(new FileReader(dataFileLocation + dataFileName));
    		
        	if (!classes.isEmpty())
        		classes.clear();
        	
        	for (int i = 0; i < classesQuantity; i++)
        	{
        		classes.add(new ArrayList<String[]>());
        		classValues[i] = getStringFromUser("value of class" + i);
        	}
        	
        	while (dataFileReader.ready())
        	{
        		example = dataFileReader.readLine().split(",");
        		
        		for (int i = 0; i < classesQuantity; i++)
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
    
    
    public void discretizeAttributeByWidth(int attrNumber, int bins)
    {
		double max = Double.parseDouble(classes.get(0).get(0)[attrNumber]);
		double min = max;
		
    	for (Iterator<List<String[]>> iteratorClasses = classes.iterator(); iteratorClasses.hasNext();)
    	{	
    		for (Iterator<String[]> iteratorExamples = iteratorClasses.next().iterator(); iteratorExamples.hasNext();)
    		{
    			//finding maximum value of attribute
    			double tempValue = Double.parseDouble(iteratorExamples.next()[attrNumber]);
    			if(max < tempValue)
    				max = tempValue;
    			
    			//finding minimum value of attribute
    			else if(min > tempValue)
    				min = tempValue;
    		}
    	}
    	
    	int i = 0;
    	for (Iterator<List<String[]>> iteratorClasses = classes.iterator(); iteratorClasses.hasNext();)
    	{	
    		int j = 0;
    		for (Iterator<String[]> iteratorExamples = iteratorClasses.next().iterator(); iteratorExamples.hasNext();)
    		{
    			double attrValue = Double.parseDouble(iteratorExamples.next()[attrNumber]);
    			
    			for(int k = 0; k < bins; k++)
    			{
    				if(attrValue >= (min+((max-min)/bins)*k) && attrValue <= (min+(((max-min)/bins)*(k+1))))
        				classes.get(i).get(j)[attrNumber] = String.valueOf((min+((max-min)/bins)*k)) + "-" +  String.valueOf(min+(((max-min)/bins)*(k+1)));
    			}	
    			j++;
    		}
    		i++;
    	}
    }
    
    public void discretizeAttributeByFrequency (int attrNumber, int bins)
    {
    	List<String[]> examples = new ArrayList<String[]>();
    	
    	BufferedReader dataFileReader = null;
    	BufferedWriter dataFileWriter = null;
		try 
		{
			dataFileReader = new BufferedReader(new FileReader(dataFileLocation + dataFileName));

			dataFileWriter = new BufferedWriter(new FileWriter(dataFileLocation + dataFileName + "_sorted"));
			
			//reading data from initial file
	    	while (dataFileReader.ready())
	    	{
	    		examples.add(dataFileReader.readLine().split(","));
	    	}
	    	
	    	dataFileReader.close();
	    	
	    	//sorting examples list
	    	String[] tempExample;
	    	for(int i = 1; i < examples.size()-1; i++)
	    	{
	    		int j = i;
	    		while((j > 0) && (Double.parseDouble(examples.get(j-1)[attrNumber]) > Double.parseDouble(examples.get(j)[attrNumber])))
				{
	    			tempExample = examples.get(j-1);
	    			examples.set(j-1,examples.get(j));
	    			examples.set(j,tempExample);
	    			j--;
				}
	
	    	}
	    	
	    	//discretisation 
	    	int binSize = examples.size()/bins;
	    	int leftExamples = examples.size()%bins;
	    	int j = 0;
	    	int compValue = 0;
	    	String newAttrValue;
	    	
			for(int i = 0; i < bins; i++)
			{
				if(leftExamples > 0)
					newAttrValue = examples.get(j)[attrNumber] + "-" + examples.get(binSize*(i+1)+compValue)[attrNumber];
				else
					newAttrValue = examples.get(j)[attrNumber] + "-" + examples.get(binSize*(i+1)+compValue-1)[attrNumber];
				
				for(; j < binSize*(i+1) + compValue; j++)
				{
					//examples.get(j)[attrNumber] = "BIN" + String.valueOf(i+1);
					examples.get(j)[attrNumber] = newAttrValue;
				}
				
				if(leftExamples > 0)
				{
					//examples.get(binSize*(i+1))[attrNumber] = "BIN" + String.valueOf(i+1);
					examples.get(binSize*(i+1)+compValue)[attrNumber] = newAttrValue;
					compValue++;
					j++;
					leftExamples--;
				}
			}
    	
	    	
	    	for (Iterator<String[]> iterator = examples.iterator(); iterator.hasNext();) 
	    	{
	    		int i = 0;
	    		for(String attrValue : iterator.next())
	    		{
	    			dataFileWriter.write(attrValue);
	    			
	    			if (i<attributesQuantity)
	    				dataFileWriter.write(",");
	    			
	    			i++;
	    		}
	    		
	    		dataFileWriter.write("\n");
			}
	    	
	    	dataFileWriter.close();
	    	
			//writing discretised data to classes variable
      		classes.clear();
      		
      		dataFileReader = new BufferedReader(new FileReader(dataFileLocation + dataFileName + "_sorted"));
      		
        	for (int i = 0; i < classesQuantity; i++)
        	{
        		classes.add(new ArrayList<String[]>());
        	}
        	
        	while (dataFileReader.ready())
        	{
        		example = dataFileReader.readLine().split(",");
        		
        		for (int i = 0; i < classesQuantity; i++)
        		{
        			//System.out.println("class Value:" + example[attributesQuantity]);
        			//System.out.println(classValues[i]);
        			if (classValues[i].equals(example[attributesQuantity]))
        			{
        				classes.get(i).add(example);
        				break;
        			}
        		}
        	}
        	dataFileReader.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
    	
    }
    
    public void writeDataToFile()
    {
    	String writeFile = "/home/michal/workspace/Classification/src/classification/Datasets/Wine/wineNew.data.txt";

		BufferedWriter dataFileWriter;
		try 
		{
			dataFileWriter = new BufferedWriter(new FileWriter(writeFile));
		for (Iterator<List<String[]>> iteratorClasses = classes.iterator(); iteratorClasses.hasNext();)
    	{	
    		for (Iterator<String[]> iteratorExamples = iteratorClasses.next().iterator(); iteratorExamples.hasNext();)
    		{
    			int i = 0;
    			for(String attr : iteratorExamples.next())
    			{
    				dataFileWriter.write(attr);
	    			if (i<attributesQuantity)
	    				dataFileWriter.write(",");
	    			
	    			i++;
    			}
    			dataFileWriter.write("\n");
    		}
    	}
		
		dataFileWriter.close();
		System.out.println("Writing data to file finished successfuly.");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
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
     
    public List<String[]> getTestData(int chunkFrom, int chunkTo)
    {
    	List<String[]> testData = new ArrayList<>();
    	List<String[]> dataClass;
    	String[] example;
    	
    	int j;
    	for (int i = chunkFrom; i < chunkTo; i++)
    	{
    		j = 0;
    		for(Iterator<List<String[]>> iterator = dividedData.get(i).iterator(); iterator.hasNext();)
    		{
    			dataClass = iterator.next();
    			
    			for(int k = 0; k < dataClass.size(); k++)
    			{
    				example = dataClass.get(k);
    				//example[attributesQuantity] = "";
    				testData.add(example);
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
    	this.dataFileName = "car.data.txt";
    	this.attributesQuantity = 6;
    	this.classesQuantity = 4;
    	
    }
    
    public int getValueFromUser(String valueName)
    {

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
        System.out.print("Enter " + valueName + ": ");
        
        String value = sc.next();

        return value;
    }

}
