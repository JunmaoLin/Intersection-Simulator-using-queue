/**
 * This class represents one of the roads in our intersection.
*/

public class TwoWayRoad {
    public static final int FORWARD_WAY = 0;
    public static final int BACKWARD_WAY = 1;
    public static final int NUM_WAYS = 2;
    public static final int LEFT_LANE = 0;
    public static final int MIDDLE_LANE = 1;
    public static final int RIGHT_LANE = 2;
    public static final int NUM_LANES = 3;
    private String name;
    private int greenTime;
    private int leftSignalGreenTime;
    private VehicleQueue[][] lanes;
    private LightValue lightValue;

    /**
     * This is the constructor of the class.
     * @param initName
     *  name of the road
     * @param initGreenTime
     *  green time of the road
     * @throws IllegalArgumentException
     *  if the name is null or initGreenTime is negative or 0
     */
    public TwoWayRoad(String initName, int initGreenTime) throws IllegalArgumentException{
        if(initGreenTime <=0 || initName == null){
            throw new IllegalArgumentException("initGreenTime must be greater than 0 or initName cannot be null");
        }
        lanes = new VehicleQueue[NUM_WAYS][NUM_LANES];///////
        for(int i = 0; i < lanes.length; i++){
            for(int j = 0; j < lanes[i].length; j++){
                lanes[i][j] = new VehicleQueue();
            }
        }
        this.name = initName;
        this.greenTime = initGreenTime;
        leftSignalGreenTime = (int)Math.floor(1.0/NUM_LANES * initGreenTime);
    }

    /**
     * This method sets the lightValue of the road.
     * @param lightValue
     */
    public void setLightValue(LightValue lightValue){
        this.lightValue = lightValue;
    }

    /**
     * This is the getter method for the time of Green Time
     * @return
     */
    public int getGreenTime() {
        return greenTime;
    }

    /**
     * THis method returns the cars on a specific lane
     * @param wayIndex
     *  The direction the car is going in.
     * @param laneIndex
     *  The lane the car arrives in.
     * @return
     *  a String array of cars on the lane
     */
    public String getVehicles(int wayIndex, int laneIndex){
        String vehicles = "";
        VehicleQueue curQueue = lanes[wayIndex][laneIndex];
        VehicleQueue temp = new VehicleQueue();
        while(!curQueue.isEmpty()){
            if(wayIndex == 0){
                Vehicle curVehicle = curQueue.dequeue();
                vehicles = "[" + String.format("%03d", curVehicle.getSerialID()) + "]" + " " + vehicles;
                temp.enqueue(curVehicle);
            }
            else if(wayIndex == 1){
                Vehicle curVehicle = curQueue.dequeue();
                vehicles = vehicles + " " + "[" + String.format("%03d", curVehicle.getSerialID()) + "]" ;
                temp.enqueue(curVehicle);
            }
        }
        lanes[wayIndex][laneIndex] = temp;
        return vehicles;
    }

    /**
     * This method returns the lanes of the road.
     * @return
     */
    public VehicleQueue[][] getLanes(){
        return this.lanes;
    }

    /**
     * This is the getter method for the light value.
     * @return
     */
    public LightValue getLightValue(){
        return this.lightValue;
    }

    /**
     * This method returns the name of the road.
     * @return
     */
    public String getName(){
        return this.name;
    }

    public LightValue getCurrentLightValue(int timerVal){
        if(timerVal > leftSignalGreenTime){
            return LightValue.GREEN;
        }
        else{
            return LightValue.LEFT_SIGNAL;
        }
    }

    /**
     * Executes the passage of time in the simulation.
     * @param timerVal
     *  The current timer value, determines the state of the light.
     * @return
     *  An array of Vehicles that has been dequeued during this time step.
     * @throws IllegalArgumentException
     *  If timerval ≤ 0.
     */
    public Vehicle[] proceed(int timerVal) throws IllegalArgumentException{
        if(timerVal <= 0){
            throw new IllegalArgumentException("timerVal must be greater than 0");
        }
        VehicleQueue vehiclesQueue = new VehicleQueue();

        if(timerVal > leftSignalGreenTime){
            this.lightValue = LightValue.GREEN;
            if(!lanes[0][1].isEmpty()){
                vehiclesQueue.enqueue(lanes[0][1].dequeue());
            }
            if(!lanes[0][2].isEmpty()){
                vehiclesQueue.enqueue(lanes[0][2].dequeue());
            }
            if(!lanes[1][2].isEmpty()){
                vehiclesQueue.enqueue(lanes[1][2].dequeue());
            }
            if(!lanes[1][1].isEmpty()){
                vehiclesQueue.enqueue(lanes[1][1].dequeue());
            }
            this.lightValue = LightValue.GREEN;
        }
        else if(timerVal <= leftSignalGreenTime){
            this.lightValue = LightValue.LEFT_SIGNAL;
            if(!lanes[0][0].isEmpty()){
                vehiclesQueue.enqueue(lanes[0][0].dequeue());
            }
            if(!lanes[1][0].isEmpty()){
                vehiclesQueue.enqueue(lanes[1][0].dequeue());
            }
            this.lightValue = LightValue.LEFT_SIGNAL;
        }

        Vehicle[] vehicleArray = new Vehicle[vehiclesQueue.size()];
        for(int i = 0; i < vehicleArray.length; i++){
            vehicleArray[i] = vehiclesQueue.dequeue(); 
        }
        return vehicleArray;
    }

    /**
     * Enqueues a vehicle into a the specified lane.
     * @param wayIndex
     *  The direction the car is going in.
     * @param laneIndex
     *  The lane the car arrives in.
     * @param vehicle
     *  The vehicle to enqueue; must not be null.
     * @throws IllegalArgumentException
     *  If wayIndex > 1 || wayIndex < 0 || laneIndex < 0 || laneIndex > 2 or vehicle==null
     */
    public void enqueueVehicle(int wayIndex, int laneIndex, Vehicle vehicle) throws IllegalArgumentException{
        if(wayIndex > 1 || wayIndex < 0 || laneIndex < 0 || laneIndex > 2 || vehicle == null){
            throw new IllegalArgumentException("wayIndex, laneIndex or vehicle is invalid");
        }
        lanes[wayIndex][laneIndex].enqueue(vehicle);
    }

    /**
     * Checks if a specified lane is empty.
     * @param wayIndex
     *  The direction of the lane.
     * @param laneIndex
     *  The index of the lane to check.
     * @return
     *  true if the lane is empty, else false.
     * @throws IllegalArgumentException
     *  If wayIndex > 1 || wayIndex < 0 || laneIndex < 0 || laneIndex > 2.
     */
    public boolean isLaneEmpty(int wayIndex, int laneIndex) throws IllegalArgumentException{
        if(wayIndex > 1 || wayIndex < 0 || laneIndex < 0 || laneIndex > 2){
            throw new IllegalArgumentException("wayIndex or laneIndex is invalid.");
        }
        return lanes[wayIndex][laneIndex].isEmpty();
    }

    public boolean isAllLanesEmpty(){
        boolean flag = true;
        for(int wayIndex = 0; wayIndex < NUM_WAYS; wayIndex++){
            for(int laneIndex = 0; laneIndex < NUM_LANES; laneIndex++){
                if(!isLaneEmpty(wayIndex, laneIndex)){
                    flag = false;
                    return false;
                }
            }
        }
        return flag;
    }
}
