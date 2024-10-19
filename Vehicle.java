/**
 * This class represents a car which passes through the intersection.
 */

public class Vehicle {
    private static int serialCounter = 0;
    private int serialId;
    private int timeArrived;
    
    /**
     * This is the constructor of the Vehicle class.
     * @param initTimeArrived
     *  initial arrival time of the vehicle
     * @throws IllegalArgumentException
     *  thrown if initTimeArrived ≤ 0.
     */
    public Vehicle(int initTimeArrived) throws IllegalArgumentException{
        if(initTimeArrived <= 0){
            throw new IllegalArgumentException("initTimeArrived must be greater than 0.");
        }
        serialCounter++;
        serialId = serialCounter;
        timeArrived = initTimeArrived;
    }

    /**
     * This is a getter method for the serialId.
     * @return
     *  an int representing the serialId.
     */
    public int getSerialID(){
        return serialId;
    }

    /**
     * This is a getter method for the timeArrived.
     * @return
     *  an int representing the timeArrived
     */
    public int getTimeArrived(){
        return timeArrived;
    }

}
