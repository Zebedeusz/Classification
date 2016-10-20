package classification;

public class Initialiser {

    public static void main(String[] args)
    {
        DataAcquisitor acq = new DataAcquisitor();
        acq.loadData();
        acq.divideData(10);
        acq.getTrainingData(2, 7);
    }
    

    
}
