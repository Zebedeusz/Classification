package classification;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class F_Measure 
{
	private static final F_Measure instance = new F_Measure();
	private int[][] scores;
	private double accuracy;
	private double[] recall;
	private double[] precision;
	
	private double[][] meanScores;
	private double meanAccuracy;
	private double[] meanRecall;
	private double[] meanPrecision;
	
	private F_Measure(){};
	
	public static F_Measure getInstance()
	{
		return instance;
	}
	
	public void writeMeansOfMeasuresToFile(String path, int classesQuantity, String[] classValues)
	{
		try {
			BufferedWriter dataFileWriter = new BufferedWriter(new FileWriter(path, true));
			
			dataFileWriter.write(" ,");
			
			for(int j = 0; j < classesQuantity; j++)
				dataFileWriter.write(classValues[j] + ",");
			
			for(int k = 0; k < scores.length; k++)
			{
				dataFileWriter.write("\n" + classValues[k] + ",");
				
				for(int l = 0; l < meanScores.length; l++)
					dataFileWriter.write(meanScores[k][l] + ",");
			}
			
			for(int j = 0; j < meanPrecision.length; j++)
			{
				dataFileWriter.write("\nMean Precision: " + meanPrecision[j] + "\n");
				dataFileWriter.write("Mean Recall: " + meanRecall[j] + "\n");
			}

			dataFileWriter.write("Mean Accuracy: " + meanAccuracy + "\n\n");
			
			dataFileWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeMeasuresToFile(String path, int classesQuantity, String[] classValues)
	{
		try {
			BufferedWriter dataFileWriter = new BufferedWriter(new FileWriter(path, true));
			
			dataFileWriter.write(" ,");
			
			for(int j = 0; j < classesQuantity; j++)
				dataFileWriter.write(classValues[j] + ",");
			
			for(int k = 0; k < scores.length; k++)
			{
				dataFileWriter.write("\n" + classValues[k] + ",");
				
				for(int l = 0; l < scores.length; l++)
					dataFileWriter.write(scores[k][l] + ",");
			}
			
			for(int j = 0; j < precision.length; j++)
			{
				dataFileWriter.write("\nPrecision: " + precision[j] + "\n");
				dataFileWriter.write("Recall: " + recall[j] + "\n");
			}

			dataFileWriter.write("Accuracy: " + accuracy + "\n\n");
			
			dataFileWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void calculateScores(List<String[]> classifiedData, int classesQuantity, String[] classValues)
	{
		List<List<String[]>> classes = new ArrayList<>();
		String[] example;
		int attributesQuantity = classifiedData.get(0).length-2;
		
		this.scores = new int [classesQuantity][classesQuantity];
		setZeroesInScoresArray();
		this.recall = new double[classesQuantity];
		this.precision = new double[classesQuantity]; 
		
    	for (int i = 0; i < classesQuantity; i++)
    		classes.add(new ArrayList<String[]>());
    	
		//moving classifiedData into classes
		for(Iterator<String[]> iterator = classifiedData.iterator(); iterator.hasNext();)
		{	
        	example = iterator.next();
        	
    		for (int i = 0; i < classesQuantity; i++)
    		{
    			if (classValues[i].equals(example[attributesQuantity]))
    			{
    				classes.get(i).add(example);
    				break;
    			}   			
    		}
		}
		
		List<String[]> classifiedClass;
		int classNumber = 0;
		int testOutcomePositive;
		
		//System.out.println("Classes size: " + classes.size());
		for(Iterator<List<String[]>> iteratorClasses = classes.iterator(); iteratorClasses.hasNext();)
		{
			classifiedClass = iteratorClasses.next();
			testOutcomePositive = 0;
			for(Iterator<String[]> iteratorExamples = classifiedClass.iterator(); iteratorExamples.hasNext();)
			{
				example = iteratorExamples.next();
				
				for(int i = 0; i < classesQuantity; i++)
				{
					if(classValues[i].equals(example[example.length-1]))
					{
						scores[classNumber][i]++;
						break;
					}
				}
				
				if(classValues[classNumber].equals(example[example.length-1]))
					testOutcomePositive++;
			}
			if(classifiedClass.size() != 0)
				recall[classNumber] = ((double)scores[classNumber][classNumber]) / ((double)classifiedClass.size());
			else
				recall[classNumber] = 0;
			if(testOutcomePositive != 0)
				precision[classNumber] = ((double)scores[classNumber][classNumber]) / ((double)testOutcomePositive);
			else 
				precision[classNumber] = 0;
			
			classNumber++;
		}
		accuracy = calculateAccuracy(classifiedData.size());
	}
	
	private double calculateAccuracy(int totalSizeOfData)
	{
		int truths = 0;
		
		for(int i = 0; i < scores.length; i++)
			truths += scores[i][i]; 
		
		return ((double)truths)/((double)totalSizeOfData);
	}
	
	private void setZeroesInScoresArray()
	{
		for(int i = 0; i < this.scores.length; i++)
		{
			for (int j = 0; j < this.scores.length; j++)
			{
				scores[i][j] = 0;
			}
		}
	}
	
	public void calculateMeanScores(int[][][] allScores)
	{
		double[][] tempScores = new double[scores.length][scores.length];
		
		for (int k = 0; k < tempScores.length; k++)
			for (int l = 0; l < tempScores.length; l++)
				tempScores[k][l] = 0;
		
		for (int j = 0; j < allScores.length; j++)
		{
			for (int k = 0; k < scores.length; k++)
			{
				for (int l = 0; l < scores.length; l++)
				{
					tempScores[k][l] += (double) allScores[j][k][l];
				}
			}
		}	
		
		for (int k = 0; k < scores.length; k++)
		{
			for (int l = 0; l < scores.length; l++)
			{
				tempScores[k][l] /= ((double) allScores.length);
			}
		}
		
		this.meanScores = tempScores;	
	}
	
	public void calculateMeanRecalls(double[][] allRecalls)
	{
		double[] tempRec = new double[allRecalls[0].length];
		
		for (int k = 0; k < tempRec.length; k++)
			tempRec[k] = 0;
		
		for(int i = 0; i < allRecalls.length; i++)
		{
			for(int j = 0; j < tempRec.length; j++)
			{
				tempRec[j] += allRecalls[i][j];
			}
		}
		
		for (int j = 0; j < tempRec.length; j++)
			tempRec[j] /= ((double) allRecalls.length);
		
		this.meanRecall = tempRec;
		
	}
	
	public void calculateMeanPrecisions(double[][] allPrecisions)
	{
		double[] tempPrec = new double[allPrecisions[0].length];
		
		for (int k = 0; k < tempPrec.length; k++)
			tempPrec[k] = 0;
	
		for(int i = 0; i < allPrecisions.length; i++)
		{
			for(int j = 0; j < tempPrec.length; j++)
			{
				tempPrec[j] += allPrecisions[i][j];
			}
		}
		
		for (int j = 0; j < tempPrec.length; j++)
			tempPrec[j] /= ((double) allPrecisions.length);
		
		this.meanPrecision = tempPrec;
	}
	
	public void calculateMeanAccuracy(double[] allAccuracies)
	{
		double tempAcc = 0;
		for(double acc : allAccuracies)
			tempAcc += acc;
		
		this.meanAccuracy = tempAcc/allAccuracies.length; 
	}
	
	public int[][] getScores()
	{
		return scores;
	}
	
	public double getAccuracy() {
		return accuracy;
	}

	public double[] getRecall() {
		return recall;
	}

	public double[] getPrecision() {
		return precision;
	}
	
}
