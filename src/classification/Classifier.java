package classification;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Classifier 
{
	public List<List<String[]>> trainingData;
	public List<String[]> classifiedData;
	
	public void setTrainingData(List<List<String[]>> trainingData)
	{
		this.trainingData = new ArrayList<>(trainingData);
	}

	public List<String[]> getClassifiedData()
	{
		return classifiedData;
	}
	
    public void writeDataToFile(String path, int attributesQuantity)
    {
		BufferedWriter dataFileWriter;
		
		try 
		{
			dataFileWriter = new BufferedWriter(new FileWriter(path));
		for (Iterator<String[]> iteratorExamples = classifiedData.iterator(); iteratorExamples.hasNext();)
    	{	
			int i = 0;
			for(String attr : iteratorExamples.next())
			{
				dataFileWriter.write(attr);
				if (i<attributesQuantity+1)
					dataFileWriter.write(",");
				
				i++;
			}
			dataFileWriter.write("\n");
    	}
		
		dataFileWriter.close();
		System.out.println("Writing data to file finished successfuly.");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
    }
	
	
}
