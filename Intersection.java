/**
 * This class represents a crossing of two or more roads at a stop light in our simulation.
*/

public class Intersection {
    private TwoWayRoad[] roads;
    private int lightIndex;
    private int countdownTimer;
    private final int MAX_ROADS = 4;

    /**
     * Constructor which initializes the roads array.
     * @param initRoads
     *  Array of roads to be used by this intersection.
     * @throws IllegalArgumentException
     *  If initRoads is null.
     *  If any index of initRoads is null.
     *  initRoads.length > MAX_ROADS.
     */
    public Intersection(TwoWayRoad[] initRoads) throws IllegalArgumentException{  
        if(initRoads == null || initRoads.length > MAX_ROADS){
            throw new IllegalArgumentException("roads cannot be null or exceed MAX_ROADS");
        }
        for(int i = 0; i < initRoads.length; i++){
            if(initRoads[i] == null){
                throw new IllegalArgumentException("contains null road");
            }
        }
        this.roads = initRoads;
        this.lightIndex = 0;
        this.countdownTimer = initRoads[lightIndex].getGreenTime();
    }

    /**
     * This method returns the countdown timer
     * @return
     *  int
     */
    public int getCountdownTimer() {
        return countdownTimer;
    }

    /**
     * Sets the light value of current road
     * @param lightValue
     */
    public void setCurrentRoadLightValue(LightValue lightValue){
        roads[lightIndex].setLightValue(lightValue);
    }

    /**
     * Returns the light value of current road.
     * @return
     *  String
     */
    public String getLightValueWithRoadName(int timerVal) {
        String str = "";
        if(roads[lightIndex].getCurrentLightValue(timerVal) == LightValue.GREEN){
            str = "Green Light for " + roads[lightIndex].getName() + ".";
        }
        else if(roads[lightIndex].getCurrentLightValue(timerVal) == LightValue.LEFT_SIGNAL){
            str = "Left Signal for " + roads[lightIndex].getName() + ".";
        }
        else if(roads[lightIndex].getCurrentLightValue(timerVal) == LightValue.RED){
            str = "Red Light for " + roads[lightIndex].getName() + ".";
        }
        return str;
    }

    /**
     * Performs a single iteration through the intersection.
     * @return
     *  An array of Vehicles which have passed though the intersection during this time step.
     */
    public Vehicle[] timeStep(){
        Vehicle[] vehArr = roads[lightIndex].proceed(countdownTimer);
        return vehArr;
    }

    /**
     * Performs a single iteration through the intersection
     */
    public void timeStepPtTwo(){
        countdownTimer--;
        if(countdownTimer == 0){
            roads[lightIndex].setLightValue(LightValue.RED);
            lightIndex = (lightIndex + 1) % roads.length;
            this.countdownTimer = roads[lightIndex].getGreenTime();
        }
    }

    /**
     * Enqueues a vehicle onto a lane in the intersection.
     * @param roadIndex
     * Index of the road in roads which contains the lane to enqueue onto.
     * @param wayIndex
     * Index of the direction the vehicle is headed. Can either be TwoWayRoad.FORWARD or TwoWayRoad.BACKWARD
     * @param laneIndex
     * Index of the lane on which the vehicle is to be enqueue. Can either be TwoWayRoad.RIGHT_LANE, TwoWayRoad.MIDDLE_LANE, or TwoWayRoad.LEFT_LANE.
     * @param vehicle
     * The Vehicle to enqueue onto the lane.
     * @throws IllegalArgumentException
     * IllegalArgumentException
     * If vehicle is null.
     * If any of the index parameters above are not within the valid range.
     */
    public void enqueueVehicle(int roadIndex, int wayIndex, int laneIndex, Vehicle vehicle) throws IllegalArgumentException{
        if(vehicle == null || roadIndex < 0 || roadIndex >= roads.length || wayIndex < 0 || wayIndex >= TwoWayRoad.NUM_WAYS ||
            laneIndex < 0 || laneIndex >= TwoWayRoad.NUM_LANES){
            throw new IllegalArgumentException("Error: vehicle cannot be null or out of range");
        }
        roads[roadIndex].enqueueVehicle(wayIndex, laneIndex, vehicle);
    }

    /**
     * This method return true if no vehicles are left on the lanes
     * @return
     *  boolean
     */
    public boolean isEmpty(){
        for(int i = 0; i < roads.length; i++){
            if(!roads[i].isAllLanesEmpty()){
                return false;
            }
        }
        return true;
    }


    /**
     * Prints the intersection to the terminal in a neatly formatted manner.
     */
    public void display(){
        for(int i  = 0; i < roads.length; i++){
            TwoWayRoad currentRoad = roads[i];
            //VehicleQueue[][] vehicleQueue = currentRoad.getLanes();
            System.out.println("    " + currentRoad.getName() + ":");
            System.out.println("                           FORWARD               BACKWARD");
            System.out.println("    ==============================               ===============================");
            String forwardLeftVehicles = currentRoad.getVehicles(0, 0);
            System.out.printf("    %31s", forwardLeftVehicles);
            //System.out.print("[L] ");
            if(currentRoad.getLightValue() != LightValue.LEFT_SIGNAL){
                System.out.print("[L] x   ");
            }
            else{
                System.out.print("[L]     ");
            }
            if(currentRoad.getLightValue() != LightValue.GREEN){
                System.out.print("x [R]");
            }
            else{
                System.out.print("  [R]");
            }
            String backwardRightVehicles = currentRoad.getVehicles(1, 2);
            System.out.printf("%-32s", backwardRightVehicles);
            System.out.println("\n    ------------------------------               -------------------------------");

            String forwardMiddleVehicles = currentRoad.getVehicles(0, 1);
            System.out.printf("    %31s", forwardMiddleVehicles);
            if(currentRoad.getLightValue() != LightValue.GREEN){
                System.out.print("[M] x   ");
            }
            else{
                System.out.print("[M]     ");
            }
            if(currentRoad.getLightValue() != LightValue.GREEN){
                System.out.print("x [M]");
            }
            else{
                System.out.print("  [M]");
            }
            String backwardMiddleVehicles = currentRoad.getVehicles(1, 1);
            System.out.printf("%-32s", backwardMiddleVehicles);
            System.out.println("\n    ------------------------------               -------------------------------");

            String forwardRightVehicles = currentRoad.getVehicles(0, 2);
            System.out.printf("    %31s", forwardRightVehicles);
            if(currentRoad.getLightValue() != LightValue.GREEN){
                System.out.print("[R] x   ");
            }
            else{
                System.out.print("[R]     ");
            }
            if(currentRoad.getLightValue() != LightValue.LEFT_SIGNAL){
                System.out.print("x [L]");
            }
            else{
                System.out.print("  [L]");
            }
            String backwardLeftVehicles = currentRoad.getVehicles(1, 0);
            System.out.printf("%-32s", backwardLeftVehicles);
            System.out.println("\n    ==============================               ===============================");
            System.out.println("\n");

        }
    }

}


