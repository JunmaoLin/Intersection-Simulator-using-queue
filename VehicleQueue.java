/**
 * Lanes in our simulator will be modelled as a Queue of Vehicles
*/

import java.util.LinkedList;
public class VehicleQueue extends LinkedList{
    LinkedList<Vehicle> vehicleQueue = new LinkedList<Vehicle>();

    /**
     * This is the constructor for the VehicleQueue class
     */
    public VehicleQueue(){}

    /**
     * This method adds a vehicle to the queue
     * @param v
     *  adds a vehicle to the queue
     */
    public void enqueue(Vehicle v){
        vehicleQueue.add(v);
    }

    /**
     * this method returns the first vehicle in the queue
     * @return
     *  the first vehicle in the queue
     */
    public Vehicle dequeue(){
        return vehicleQueue.remove();
    }

    /**
     * This method returns the number of vehicles in the queue
     */
    public int size(){
        return vehicleQueue.size();
    }

    /**
     * This method checks if the queue is empty
     */
    public boolean isEmpty(){
        return vehicleQueue.isEmpty();
    }
}
