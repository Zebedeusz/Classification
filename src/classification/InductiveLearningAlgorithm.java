package classification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InductiveLearningAlgorithm extends Classifier
{
	private List<List<List<String>>> attributeValues; 
	private int[][][] attributeValuesQuantity;
	
	//for each class in trainingData finds values of attributes which are unique in dataset
	//and counts their quantity in the class
	private void inspectTrainingData()
	{
		attributeValues = new ArrayList<>();
		 
		int classesCnt = 0;
				
		for(Iterator<List<String[]>> trainingDataClassesIterator = trainingData.iterator(); trainingDataClassesIterator.hasNext();)
		{
			attributeValues.add(new ArrayList<>());
			
			List<String[]> tempExamples = trainingDataClassesIterator.next();
			
			
			for(int i = 0; i < tempExamples.get(0).length-1; i++)
				attributeValues.get(classesCnt).add(new ArrayList<String>());
			
			for(Iterator<String[]> trainingDataExamplesIterator = tempExamples.iterator(); trainingDataExamplesIterator.hasNext();)
			{
				String[] example = trainingDataExamplesIterator.next();
				
				for(int attrCnt = 0; attrCnt < attributeValues.get(classesCnt).size(); attrCnt++)
				{
					boolean found = false;
					
					for(int attrValuesCnt = 0; attrValuesCnt < attributeValues.get(classesCnt).get(attrCnt).size(); attrValuesCnt++)
					{
						if(attributeValues.get(classesCnt).get(attrCnt).get(attrValuesCnt).equals(example[attrCnt]))
							found = true;
					}
					
					if(!found)
					{
						attributeValues.get(classesCnt).get(attrCnt).add(example[attrCnt]);
						System.out.println(example[attrCnt] +  " to class " + classesCnt + " to attr " + attrCnt);
					}
						
				}	
			}
			
			attributeValuesQuantity = new int[attributeValues.size()][attributeValues.get(0).size()][];
			
			for(int i = 0; i < attributeValues.get(classesCnt).size(); i++)
				attributeValuesQuantity[classesCnt][i] = new int[attributeValues.get(classesCnt).get(i).size()];
			
			for(int i = 0; i < attributeValuesQuantity[classesCnt].length; i++)
				for(int j = 0; j < attributeValuesQuantity[classesCnt][i].length; j++)
					attributeValuesQuantity[classesCnt][i][j] = 0;
			
			for(Iterator<String[]> trainingDataExamplesIterator = tempExamples.iterator(); trainingDataExamplesIterator.hasNext();)
			{
				String[] example = trainingDataExamplesIterator.next();
				
				for(int i = 0; i < attributeValuesQuantity[classesCnt].length; i++)
					for(int j = 0; j < attributeValuesQuantity[classesCnt][i].length; j++)
						if(attributeValues.get(classesCnt).get(i).get(j).equals(example[i]))
							attributeValuesQuantity[classesCnt][i][j]++;
			}
		
			for(int i = 0; i < attributeValuesQuantity[classesCnt].length; i++)
				for(int j = 0; j < attributeValuesQuantity[classesCnt][i].length; j++)
					System.out.println("Quantity of attrValues in [" + classesCnt + "]["+ i + "][" + j +"]: " + attributeValuesQuantity[classesCnt][i][j]);
			
			
		classesCnt++;	
		}	
	}
	
	public void classifyExamples(List<String[]> testData)
	{
		inspectTrainingData();
	}
}

