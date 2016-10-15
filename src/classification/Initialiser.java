package classification;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Initialiser {

    public static void main(String[] args) throws FileNotFoundException 
    {
        String dataFileName = args[0];
        final int attributesQuantity = getValueFromUser("quantity of attributes");
        final int classesQuantity = getValueFromUser("quantity of classes");
        
        while ((dataFileName == "") || (dataFileName.split(".")[1] != "txt"))
        {
        	System.out.println("Insufficient data file.");
        	dataFileName = getStringFromUser("name of file with data");
        }
        
        DataAcquisitor acq = new DataAcquisitor(dataFileName, attributesQuantity, classesQuantity);
    }
    
    public static int getValueFromUser(String valueName)
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter " + valueName + ": ");
        
        int value = Integer.parseInt(sc.next());
        sc.close();
        
        return value;
    }
    
    public static String getStringFromUser(String valueName)
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter " + valueName + ": ");
        
        String value = sc.next();
        sc.close();
        
        return value;
    }
    
}
