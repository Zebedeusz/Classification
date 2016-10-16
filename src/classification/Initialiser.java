package classification;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Initialiser {

    public static void main(String[] args) throws FileNotFoundException 
    {
        DataAcquisitor acq = new DataAcquisitor();
        acq.loadData();
        acq.divideData(10);
    }
    

    
}
