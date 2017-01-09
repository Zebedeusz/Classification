package classification;

import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import javafx.util.Pair;

enum DistanceCalculationMethod
{
	Euclides, Manhattan, Czebyszew
}

enum VotingApproach
{
	democracy, theCloserTheBetter, doubleWeighted
}

public class K_NearestNeighbours extends Classifier
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
		
		switch(votingApproach)
		{
			case democracy:
				
				List<Pair<String, Integer>> classesQnt = new ArrayList<>();
				
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
				
				List<Pair<String, Double>> classesWeights = new ArrayList<>();
				
				//calculation of class representatives in examplesWithDistance list
				for(Pair<String[], Double> exampleWithDistance : examplesWithDistance)
				{
					String tempClass = exampleWithDistance.getKey()[exampleWithDistance.getKey().length - 1];
					
					boolean found = false;
					for(Pair<String, Double> classWeight : classesWeights)
					{
						if(classWeight.getKey().equals(tempClass))
						{
							found = true;
							classWeight = new Pair(tempClass, classWeight.getValue() + (1/exampleWithDistance.getValue()));
							break;
						}
					}
					
					if(!found)
						classesWeights.add(new Pair(tempClass, (1/exampleWithDistance.getValue())));
				}
				
				//finding class with highest weight
				mostFrequentClass = "";
				double maxWeight = -1;
				
				for(Pair<String, Double> classWeight : classesWeights)
				{
					if(classWeight.getValue() > maxWeight)
					{
						mostFrequentClass = classWeight.getKey();
						maxWeight = classWeight.getValue();
					}
				}
				
				classOfExample = mostFrequentClass;
				
				break;
				
			case doubleWeighted:
				
				//sorting examplesWithDistance with ascending order
				Collections.sort(examplesWithDistance, new Comparator<Pair<String[], Double>>() 
				{
				    @Override
				    public int compare(final Pair<String[], Double> o1, final Pair<String[], Double> o2) 
				    {
				        return (int) (o1.getValue() - o2.getValue());
				    }
				});
				
				//calculating weights
				List<Pair<String, Double>> doubleClassesWeights = new ArrayList<>();
				double maxDist = examplesWithDistance.get(examplesWithDistance.size() - 1).getValue();
				double minDist = examplesWithDistance.get(1).getValue();
				
				//TODO
				for(int i = 1; i <= examplesWithDistance.size(); i++)
				{
					double weight;
					
					if(i == examplesWithDistance.size())
						weight = 0;
					else
					{
						weight = ((maxDist - examplesWithDistance.get(i).getValue()) / (maxDist - minDist)) * (1/i);
					}
					
					doubleClassesWeights.add(new Pair(examplesWithDistance.get(i).getKey(), weight));
				}
				
				

				
				
				
				break;
		}
		
		
		
		return classOfExample;
	}
	
	/*private void standarize(List<String[]> listToBeStandarized)
	{
		double[] attributeMeans = new double[listToBeStandarized.get(0).length];

		for(int i = 0; i < attributeMeans.length; i++)
			attributeMeans[i] = 0;
		
		double[] attributeStds = new double[listToBeStandarized.get(0).length];

		for(int i = 0; i < attributeStds.length; i++)
			attributeStds[i] = 0;
		
		//calculate mean
		for (Iterator<String[]> iterator = listToBeStandarized.iterator(); iterator.hasNext();)
		{
			String[] example = iterator.next();
			
			for(int i = 0; i < example.length; i++)
			{
				double attrValue = Double.parseDouble(example[i]);
				
				attributeMeans[i] += attrValue; 
			}
		}
		
		for(int i = 0; i < attributeMeans.length; i++)
			attributeMeans[i] /= listToBeStandarized.size();

		//calculate std
		for (Iterator<String[]> iterator = listToBeStandarized.iterator(); iterator.hasNext();)
		{
			String[] example = iterator.next();
			
			for(int i = 0; i < example.length; i++)
			{
				double attrValue = Double.parseDouble(example[i]);
				
				attributeStds[i] += Math.pow(attrValue - attributeMeans[i], 2); 
			}
		}
		
		for(int i = 0; i < attributeStds.length; i++)
			attributeStds[i] = Math.sqrt(attributeStds[i] /(listToBeStandarized.size() - 1));
		
		//calculate standarized value
		for (Iterator<String[]> iterator = listToBeStandarized.iterator(); iterator.hasNext();)
		{
			String[] example = iterator.next();

			for(int i = 0; i < example.length; i++)
			{
				double attrValue = Double.parseDouble(example[i]);
				
				example[i] = String.valueOf((attrValue - attributeMeans[i]) / attributeStds[i]);
			}
		}	
	}*/

	public void classifyExamples(List<String[]> testData)
	{
		//copying elements from trainingData list to simplified list in order to standarize them easier later
		List<String[]> simplifiedTrainingData = new ArrayList<>();
		
		this.classifiedData = new ArrayList<String[]>();
		
		for (Iterator<List<String[]>> iteratorClasses = trainingData.iterator(); iteratorClasses.hasNext();)
			simplifiedTrainingData.addAll(iteratorClasses.next());
		/*
		List<String[]> tempSimplifiedTrainingData = new ArrayList<>();
		
		for (Iterator<String[]> iteratorSimplifiedTrainingData = simplifiedTrainingData.iterator(); iteratorSimplifiedTrainingData.hasNext();)
		{
			String[] example = iteratorSimplifiedTrainingData.next();
			String[] exampleToAdd = new String[example.length - 1];
			
			for(int i = 0; i < example.length - 1; i++)
				exampleToAdd[i] = example[i];
			
			tempSimplifiedTrainingData.add(exampleToAdd);
		}
		
		//preparation:
			//standarize all examples in test data and training data
			//standarize(tempSimplifiedTrainingData);
			//standarize(testData);
			
			for (int i = 0; i < simplifiedTrainingData.size(); i++)
			{
				String[] example = new String[simplifiedTrainingData.get(0).length];
				
				for(int j = 0; j < example.length - 1; j++)
					example[j] = tempSimplifiedTrainingData.get(i)[j];
				
				example[example.length - 1] = simplifiedTrainingData.get(i)[simplifiedTrainingData.get(i).length - 1];
				
				simplifiedTrainingData.set(i, example);
			}*/

			
		//for all elements in testData:
			//find nearestNeighboursQnt examples closest to particular test example by:
				//for all elements in trainingData calculate distance between testData example and trainingData example
					//add nearestNeighboursQnt examples to the list, then with every new example compare the new example with the furthest example in the list, replace if necessary 
					//decide which class is to be assigned to the tested example
					//add the tested example to classifiedData
			
		for (Iterator<String[]> testDataIterator = testData.iterator(); testDataIterator.hasNext();)
		{
			String[] testExample = testDataIterator.next();
			
			examplesWithDistance.clear();
			double maxDist = 0;
			Pair<String[], Double> furthestExampleWithDist = new Pair(testExample, 0);
			
			for (Iterator<String[]> trainingDataIterator = simplifiedTrainingData.iterator(); trainingDataIterator.hasNext();)
			{
				String[] trainedExample = trainingDataIterator.next();
				
				double distance = calculateDistance(testExample, trainedExample);

				if(examplesWithDistance.size() < nearestNeighboursQnt)
					examplesWithDistance.add(new Pair(trainedExample, distance));
				
				else
				{
					//find the furthest example in examplesWithDistance
					maxDist = 0;
					for (Iterator<Pair<String[], Double>> examplesWithDistanceIterator = examplesWithDistance.iterator(); examplesWithDistanceIterator.hasNext();)
					{
						Pair<String[], Double> tempPair = examplesWithDistanceIterator.next();
						double tempDist = tempPair.getValue();
						
						if(tempDist > maxDist)
						{
							maxDist = tempDist;
							furthestExampleWithDist = tempPair;
						}
					}
					
					//compare found example distance with new distance
					if(maxDist > distance)
					{
						examplesWithDistance.remove(furthestExampleWithDist);
						examplesWithDistance.add(new Pair(trainedExample, distance));
						//System.out.println(examplesWithDistance.size());
					}
				}
			}
			
			String[] example = new String[simplifiedTrainingData.get(0).length + 1];
			
			for(int i = 0; i < testExample.length; i++)
				example[i] = testExample[i];
			
			example[example.length - 1] = voteForClass();
			
			//System.out.println(voteForClass());
			
			this.classifiedData.add(example);
		}
	}
}
