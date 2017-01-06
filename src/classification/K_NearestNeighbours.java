package classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.util.Pair;

enum DistanceCalculationMethod
{
	Euclides, Manhattan, Czebyszew
}

enum VotingApproach
{
	democracy, theCloserTheBetter
}

public class K_NearestNeighbours 
{
	int nearestNeighboursQnt;
	DistanceCalculationMethod distanceCalculationMethod;
	VotingApproach votingApproach;
	
	List<Pair<String[], Double>>  examplesWithDistance = new ArrayList<>();
	
	public K_NearestNeighbours(int nearestNeighboursQnt, DistanceCalculationMethod distanceCalculationMethod,
			VotingApproach votingApproach) 
	{
		this.nearestNeighboursQnt = nearestNeighboursQnt;
		this.distanceCalculationMethod = distanceCalculationMethod;
		this.votingApproach = votingApproach;
	}
	
	private double calculateDistance(String[] exampleTested, String[] exampleTrained)
	{
		double distance = 0;
		
		switch (distanceCalculationMethod)
		{
			case Euclides:
				
				for(int i = 0; i < exampleTested.length; i++)
					distance += Math.pow(Double.parseDouble(exampleTested[i]) - Double.parseDouble(exampleTrained[i]), 2);
				
				distance = Math.sqrt(distance);
				
				break;
				
			case Manhattan:
				
				for(int i = 0; i < exampleTested.length; i++)
					distance += Math.abs(Double.parseDouble(exampleTested[i]) - Double.parseDouble(exampleTrained[i]));
				
				break;
				
			case Czebyszew:
				
				for(int i = 0; i < exampleTested.length; i++)
					 distance = Math.max(Math.abs(Double.parseDouble(exampleTested[i]) - Double.parseDouble(exampleTrained[i])), distance);

				break;
		}
		
		return distance;
	}
	
	private String voteForClass()
	{
		String classOfExample = "";
		
		List<Pair<String, Integer>> classesQnt = new ArrayList<>();
		
		switch(votingApproach)
		{
			case democracy:
				
				//calculation of class representatives in examplesWithDistance list
				for(Pair<String[], Double> exampleWithDistance : examplesWithDistance)
				{
					
					String tempClass = exampleWithDistance.getKey()[exampleWithDistance.getKey().length - 1];
					
					boolean found = false;
					for(Pair<String, Integer> classWithQnt : classesQnt)
					{
						if(classWithQnt.getKey().equals(tempClass))
						{
							found = true;
							classWithQnt = new Pair(tempClass, classWithQnt.getValue() + 1);
							break;
						}
					}
					
					if(!found)
						classesQnt.add(new Pair(tempClass, 1));
				}
				
				//finding the most often occuring class in classesQnt
				String mostFrequentClass = "";
				int maxQnt = 0;
				
				for(Pair<String, Integer> classQnt : classesQnt)
				{
					if(classQnt.getValue() > maxQnt)
					{
						mostFrequentClass = classQnt.getKey();
						maxQnt = classQnt.getValue();
					}
				}
				
				classOfExample = mostFrequentClass;
				
				break;
				
			case theCloserTheBetter:
				
				
				
				
				break;
		}
		
		
		return classOfExample;
	}
	
	
	


	public void classifyExamples(List<String[]> testData)
	{
		//for all elements in testData:
			//find nearestNeighboursQnt examples closest to particular test example by:
				//for all elements in trainingData calculate distance between testData example and trainingData example
					//add nearestNeighboursQnt examples to the list, then with every new example compare the new example with the furthest example in the list, replace if necessary 
					//decide which class is to be assigned to the tested example
					//add the tested example to classifiedData
	}

	
	
}
