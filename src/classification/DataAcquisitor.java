package classification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataAcquisitor 
{
	int attributesQuantity = 0;
	int classesQuantity = 0;
	String dataFileName;
	
	public DataAcquisitor(String dataFileName, int attributesQuantity, int classesQuantity)
	{
		this.dataFileName = dataFileName;
		this.attributesQuantity = attributesQuantity;
		this.classesQuantity = classesQuantity;
	}
	
    String[] example = new String[attributesQuantity+1];
    List <List> classes = new ArrayList<>();
  
    public void runProcedure()
    {
    	try 
    	{
			getData(dataFileName);
		} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    	finally
    	{
    		System.out.println("Data aquisition finished successfuly");
    	}
    }
    
    private void getData(String dataFileName) throws IOException
    {
    	BufferedReader dataFileReader = new BufferedReader(new FileReader(dataFileName));
    	String[] classValues = new String[classesQuantity];
    	
    	for (int i = 0; i < classesQuantity; i++)
    	{
    		classes.add(new ArrayList<String[]>());
    		classValues[i] = getValueFromUser("value of class" + i);
    	}
    	
    	while (dataFileReader.ready())
    	{
    		example = dataFileReader.readLine().split(".");
    		
    		for (int i = 0; i < classValues.length; i++)
    		{
    			if (classValues[i].equals(example[attributesQuantity]))
    			{
    				classes.get(i).add(example);
    				System.out.println(classes.get(i));
    				break;
    			}
    		}
    	}
    	
    	dataFileReader.close();
    }
    
    private String getValueFromUser(String valueName)
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter " + valueName + ": ");        
        String value = sc.next();
        
        while (value.equals("/s+"))
        {
        	System.out.println("Insufficient value.");
        	getValueFromUser(valueName);
        }
        
        System.out.println("");
        
        sc.close();
        
        return value;
    }
    
    

}
