/**
 * This class represents the manager of the simulation 
*/

import java.util.*;
public class IntersectionSimulator {
    /**
     * Start for application
     * @param args
     */
    public static void main(String args[]){
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to IntersectionSimulator 2021\n");
        int simulationTime;
        double arrivalProbability;
        int numStreets;
        String streetNames[];
        int maxGreenTime[];

        if(args.length < 5){
            System.out.print("Input the simulation time: ");
            simulationTime = scan.nextInt();
            System.out.print("Input the arrival probability: ");
            arrivalProbability = scan.nextDouble();
            scan.nextLine();
            while(arrivalProbability <= 0.0 || arrivalProbability > 1.0){
                System.out.println("Probability must be between 0.0 and 1.0.");
                System.out.println("Try again.");
                System.out.print("Input the arrival probability: ");
                arrivalProbability = scan.nextDouble();
            }
            System.out.print("Input number of Streets: ");
            numStreets = scan.nextInt();
            scan.nextLine();
            while(numStreets < 1 || numStreets > 4){
                System.out.println("Number of streets must be between 1 and 4.");
                System.out.println("Try again.");
                System.out.print("Input number of Streets: ");
                numStreets = scan.nextInt();
            }
            scan.nextLine();
            streetNames = new String[numStreets];
            maxGreenTime = new int[numStreets];
            for(int i = 0; i < streetNames.length; i++){
                System.out.print("Input Street " + (i+1) + " name: ");
                String name = scan.nextLine();
                while(Arrays.asList(streetNames).contains(name)){
                    System.out.println("Duplicate Detected.");
                    System.out.print("Input Street " + (i+1) + " name: ");
                    name = scan.nextLine();
                }
                streetNames[i] = name;
            }
            for(int i = 0; i < streetNames.length; i++){
                System.out.print("Input max green time for " + streetNames[i] + ": ");
                int time = scan.nextInt();
                while(time <= 0){
                    System.out.println("Max green time must be greater than 0.");
                    System.out.print("Input max green time for " + streetNames[i] + ": ");
                    time = scan.nextInt();
                }
                maxGreenTime[i] = time;
            }
            scan.nextLine();
        }
        else{
            simulationTime = Integer.parseInt(args[0]);
            arrivalProbability = Double.parseDouble(args[1]);
            numStreets = Integer.parseInt(args[2]);
            streetNames = new String[numStreets];
            maxGreenTime = new int[numStreets];
            
            for (int i = 0; i < numStreets; ++i) {
                streetNames[i] = args[3 + i];
                maxGreenTime[i] = Integer.parseInt(args[3 + numStreets + i]);
            }
        }

        simulate(simulationTime, arrivalProbability, streetNames, maxGreenTime);
        System.out.println("End simulation.");
    }

    public static void simulate(int simulationTime, double arrivalProbability, String[] roadNames, int[] maxGreenTimes){
        TwoWayRoad[] twoWayRoadArr = new TwoWayRoad[roadNames.length];
        for(int i = 0; i < twoWayRoadArr.length; i++){
            twoWayRoadArr[i] = new TwoWayRoad(roadNames[i], maxGreenTimes[i]);
        }
        Intersection intersection = new Intersection(twoWayRoadArr);
        BooleanSourceHW4 booleanSourceHW4 = new BooleanSourceHW4(arrivalProbability);

        int totalCarsEntered = 0;
        int totalCarsPassed = 0;
        int totalWaitTime = 0;
        int longestWaitTime = 0;
        double averageWaitTime = 0;

        System.out.println("\nStarting Simulation...");

        int timeStep = 1;
        while(timeStep <= simulationTime){
            System.out.println("\n################################################################################\n");
            System.out.println("Time Step: " + timeStep + "\n");
            System.out.println("    " + intersection.getLightValueWithRoadName(intersection.getCountdownTimer()));
            System.out.println("    Timer = " + intersection.getCountdownTimer());
            System.out.println();
            System.out.println("    ARRIVING CARS:");
            outerLoop:
            for(int i = 0, maxVehicles = 0 ; i < twoWayRoadArr.length; i++){
                for(int wayIndex = 0; wayIndex <= 1; wayIndex++){
                    for(int laneIndex = 0; laneIndex <= 2; laneIndex++){
                        if(booleanSourceHW4.occursHW4()){
                            Vehicle newVehicle = new Vehicle(timeStep);
                            intersection.enqueueVehicle(i, wayIndex, laneIndex, newVehicle);
                            System.out.print("        Car[" + String.format("%03d", newVehicle.getSerialID()) + "] entered " + twoWayRoadArr[i].getName() + ", going ");
                            if(wayIndex == 0){
                                System.out.print("FORWARD");
                            }
                            else{
                                System.out.print("BACKWARD");
                            }
                            if(laneIndex == 0){
                                System.out.println(" in LEFT lane.");
                            }
                            else if(laneIndex == 1){
                                System.out.println(" in MIDDLE lane.");
                            }
                            else{
                                System.out.println(" in RIGHT lane.");
                            }
                            totalCarsEntered++;
                            maxVehicles++;
                            if(maxVehicles == 6){
                                break outerLoop;
                            }
                        }
                    }
                }
            }
            System.out.println("\n    PASSING CARS:");
            Vehicle[] passedCars = intersection.timeStep();
            for(int i = 0; i < passedCars.length; i++){
                System.out.println("        Car[" + String.format("%03d", passedCars[i].getSerialID()) + "] passes through. Wait time of " + (timeStep - passedCars[i].getTimeArrived()) + ".");
                totalWaitTime += timeStep - passedCars[i].getTimeArrived();
                if(timeStep - passedCars[i].getTimeArrived() > longestWaitTime){
                    longestWaitTime = timeStep - passedCars[i].getTimeArrived();
                }
            }
            totalCarsPassed += passedCars.length;
            System.out.println("\n");
            intersection.display();
            intersection.timeStepPtTwo();
            System.out.println("\n    STATISTICS:");
            System.out.println("        Cars currently waiting:  " + (totalCarsEntered - totalCarsPassed) + " cars");
            System.out.println("        Total cars passed:      " + totalCarsPassed + " cars");
            System.out.println("        Total wait time:        " + totalWaitTime + " turns");
            if(totalWaitTime != 0){
                averageWaitTime = (double)totalWaitTime/totalCarsPassed;
            }
            System.out.println("        Average wait time:      " + String.format("%.2f", averageWaitTime) + " turns");
            System.out.println();
            timeStep++;
        }

        while(!intersection.isEmpty()){
            System.out.println("\n################################################################################\n");
            System.out.println("Time Step: " + timeStep + "\n");
            System.out.println("    " + intersection.getLightValueWithRoadName(intersection.getCountdownTimer()));
            System.out.println("    Timer = " + intersection.getCountdownTimer());
            System.out.println("\nCars no longer arriving.");
            System.out.println();
            System.out.println("    ARRIVING CARS:\n");
            System.out.println("\n    PASSING CARS:");
            Vehicle[] passedCars = intersection.timeStep();
            for(int i = 0; i < passedCars.length; i++){
                System.out.println("        Car[" + String.format("%03d", passedCars[i].getSerialID()) + "] passes through. Wait time of " + (timeStep - passedCars[i].getTimeArrived()) + ".");
                totalWaitTime += timeStep - passedCars[i].getTimeArrived();
            }
            totalCarsPassed += passedCars.length;
            System.out.println("\n");
            intersection.display();
            intersection.timeStepPtTwo();
            System.out.println("\n    STATISTICS:");
            System.out.println("        Cars currently waiting:  " + (totalCarsEntered - totalCarsPassed) + " cars");
            System.out.println("        Total cars passed:      " + totalCarsPassed + " cars");
            System.out.println("        Total wait time:        " + totalWaitTime + " turns");
            if(totalWaitTime != 0){
                averageWaitTime = (double)totalWaitTime/totalCarsPassed;
            }
            System.out.println("        Average wait time:      " + String.format("%.2f", averageWaitTime) + " turns");
            System.out.println();
            timeStep++;
        }
        System.out.println("\n################################################################################");
        System.out.println("################################################################################");
        System.out.println("################################################################################");
        System.out.println("\nSIMULATION SUMMARY:\n");
        System.out.println("    Total Time:           " + timeStep + " steps");
        System.out.println("    Total vehicles:       " + totalCarsPassed + " vehicles");
        System.out.println("    Longest wait time:    " + longestWaitTime + " turns");
        System.out.println("    Total wait time:      " + totalWaitTime + " turns");
        System.out.println("    Average wait time:    " + String.format("%.2f", averageWaitTime) + " turns\n");
    }
}