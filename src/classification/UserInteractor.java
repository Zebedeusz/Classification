package classification;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInteractor 
{
	private static final UserInteractor instance = new UserInteractor();
	private Scanner sc = new Scanner(System.in);
	
	private UserInteractor(){};
	
	public static UserInteractor getInstance()
	{
		return instance;
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
    
    public String[] getStringArrayFromUser(String valueName)
    {
        System.out.print("Enter " + valueName + ": ");
        
        String[] value = sc.next().split(",");

        return value;
    }
    
    public int displayMenu()
    {
        	System.out.println("\nSelect an activity: \n"
					+ "1. Load a new dataset from a file\n"
					+ "2. Discretize attributes\n"
					+ "3. Save the data to a file\n"
					+ "4. Prepare the data for crossvalidation\n"
					+ "5. Start classification\n"
					+ "6. Close application\n");

    	return sc.nextInt();
    }
    
    public int displayAvailableClassifiers()
    {
    	System.out.println("Select a classifier: \n"
				+ "1. Naive Bayes Classifier\n"
    			+ "2. Naive Bayes Classifier with normalization of continuous attributes\n"
				+ "3. Inductive Learning Algorithm\n"
    			+ "4. K nearest neighbours");

	return sc.nextInt();
    }
    
    public int displayAvailableDiscretizers()
    {
    	System.out.println("Select a discretization method: \n"
				+ "1. by Width\n"
    			+ "2. by Frequency\n");

	return sc.nextInt();
    }
    
}
