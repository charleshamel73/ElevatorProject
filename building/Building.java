/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package building;
import elevator.ElevatorController;
import floor.FloorFacade;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import person.Person;
import person.PersonFactory;
import util.InvalidDataException;
import util.TimerSingleton;
/**
 *
 * @author charl_000
 */
public class Building implements Runnable,Serializable{
    /**
     * This is the thread that runs the simulator
     */
    private transient Thread personThread;
    /**
     * This int controls how long the simulation will run
     */
    private int simulationTime;
    /**
     * This int is how many floors exist in the building
     */
    private int floorCount;
    /**
     * This int is how many elevators will be created
     */
    private int elevator;
    /**
     * This int represents the default floor of all the elevators
     */
    private int defaultFloor;
    /**
     * This int represents the amount of people created per minute
     */
    private int spawnRatePerMinute;
    /**
     * This int indicates if the code will be optimized with different algorithms
     */
    private int upgrade;
    /**
     * This is the max percentage of total amount of people that were able to spawn on each floor 
     */
    private int percentageStartDest[];
    /**
     * This is the Time of simulation versus real time
     */
    private int ratio;
    /**
     * This is the max amount of people that can exist in the elevator
     */
    private int maxPeople;
    /**
     * This is the door operation time in ms
     */
    private int doorOperationTime;
    /**
     * This is the travel time in ms
     */
    private int travelTime;
    /**
     * This Building runs the simulator and builds the class through an xml configuration file
     * @throws InvalidDataException When the data passed to Elevator doesn't follow proper protocol
     * @throws FileNotFoundException When the input file isn't found
     * @throws IOException When their is errors when getting the input
     */
    public Building(String fileLoc) throws FileNotFoundException, IOException, InvalidDataException{
        /** This initializes the xml settings for testing**/
//        Input File will corrupt when not remade after a while
        
//        try
//        {
//            simulationTime=(60*2);
//            elevator=4;
//            defaultFloor=1;
//            floorCount=16;
//            ratio=2;
//            maxPeople=8;
//            spawnRatePerMinute=5;
//            doorOperationTime=3000;
//            travelTime=1000;
//            if(fileLoc.equalsIgnoreCase("Configuration.ser")){
//                setUpgrade(0);
//            }
//            else{
//                setUpgrade(1);
//            }
//            int[] percentage=new int[floorCount];
//            percentage[0]=15;
//            percentage[1]=5;
//            percentage[2]=5;
//            percentage[3]=5;
//            percentage[4]=5;
//            percentage[5]=5;
//            percentage[6]=10;
//            percentage[7]=5;
//            percentage[8]=5;
//            percentage[9]=5;
//            percentage[10]=5;
//            percentage[11]=5;
//            percentage[12]=5;
//            percentage[13]=5;
//            percentage[14]=5;
//            percentage[15]=10;
//            percentageStartDest= percentage.clone();
//           FileOutputStream fileOut =
//           new FileOutputStream(fileLoc);
//           ObjectOutputStream out =
//                              new ObjectOutputStream(fileOut);
//           out.writeObject(this);
//           out.close();
//            fileOut.close();
//        }catch(IOException i)
//        {
//            i.printStackTrace();
//        }
        
        Building e = null;
        try
        {
           FileInputStream fileIn =
                            new FileInputStream(fileLoc);
           ObjectInputStream in = new ObjectInputStream(fileIn);
           e = (Building) in.readObject();
           in.close();
           fileIn.close();
        }catch(IOException i)
        {
           i.printStackTrace();
           return;
        }catch(ClassNotFoundException c)
        {
           System.out.println("Building class not found");
           c.printStackTrace();
           return;
        }
        setSimTime(e.getSimTime());
        setRatio(e.getRatio());
        setElevatorCount(e.getElevatorCount());
        setFloorCount(e.getFloorCount());
        setDefaultFloor(e.getDefaultFloor());
        setPercentageStartDest(e.getpercentageStartDest());
        setSpawnRatePerMinute(e.getSpawnRatePerMinute());
        TimerSingleton.getInstance().setRatio(getRatio());
        setMaxPeople(e.getMaxPeople());
        setDoorOperationTime(e.getDoorOperationTime());
        setTravelTime(e.getTravelTime());
        setUpgrade(e.getUpgrade());
        FloorFacade.getInstance().createFloors(getFloorCount());
        personThread= new Thread(this);
        
        ElevatorController.getInstance().createElevators(getElevatorCount(),getFloorCount(),getDefaultFloor(),getMaxPeople(),getDoorOperationTime(),getTravelTime());
        getPersonThread().start();
    }
    /**
     * Gets the thread that runs the building
     * @return the thread that runs Building Simulation
     */
    private Thread getPersonThread() {
        return personThread;
    }
    
    /** Gets the amount of people created per minute
     * @return the spawnRatePerMinute
     */
    public int getSpawnRatePerMinute() {
        return spawnRatePerMinute;
    }

    /** Sets the amount of people created per minute
     * @param spawnRatePerMinute the spawnRatePerMinute to set
     * @throws InvalidDataException when spawn rate is 0 or less 
     */
    public void setSpawnRatePerMinute(int spawnRatePerMinute) throws InvalidDataException {
        if(spawnRatePerMinute<1){
            throw new InvalidDataException("Number of people spawned cannot be 0");
        }
        this.spawnRatePerMinute = spawnRatePerMinute;
    }
    /** Gets which algorithm to use 
     * @return 0: not upgrading 1: upgraded
     */
    public int getUpgrade() {
        return upgrade;
    }

    /** Sets up the elevators to use a more optimized algorithm
     * @param upgrade 0: not upgraded 1: upgraded
     */
    public void setUpgrade(int upgrade) {
        this.upgrade = upgrade;
    }
    
    /**
     * Gets the amount of elevators to be created
     * @return how many elevators to make
     */
    public int getElevatorCount() {
        return elevator;
    }
    
    /**
     * Sets how many elevators to make
     * @param elevator how many elevators to make 
     * @throws InvalidDataException when Elevator is less than 1
     */
    public void setElevatorCount(int elevator) throws InvalidDataException {
        if(elevator<1){
            throw new InvalidDataException("You must have at least one elevator");
        }
        this.elevator = elevator;
    }
    /**
     * Sets the default floor of all elevator
     * @param floor int array representation of which floor to go to when Idle
     * @throws InvalidDataException when floor is less than 1 or above the maxfloor
     */
    public void setDefaultFloor(int floor) throws InvalidDataException {
        if(floor<1){
            throw new InvalidDataException("default floor cannot be below 0");
        }
        if(floor>getFloorCount()){
            throw new InvalidDataException("default floor cannot be above "+ floorCount);
        }
        defaultFloor=floor;
    }
   /**
    * Gets the default floor of the elevators
    * @return int representation of which floor to go to when Idle
    */
    public int getDefaultFloor() {
        return defaultFloor;
    }
    /**
     * Gets the amount of floors to create
     * @return how many floors to make
     */
    public int getFloorCount() {
        return floorCount;
    }
    /**
     * Sets the amount of floors to make
     * @param floor how many floors to make
     * @throws InvalidDataException when floor is 0 or below 
     */
    public void setFloorCount(int floor) throws InvalidDataException {
        if(floor<1){
            throw new InvalidDataException("have to have more than 0 floors");
        }
        floorCount=floor;
    }
    /**
     * Sets how long the simulation will run
     * @param simTime amount of time in seconds of how long to run
     * @throws InvalidDataException when simTime is less than 0
     */
    public void setSimTime(int simTime) throws InvalidDataException {
        if(simTime<1){
            throw new InvalidDataException("Simulation Time can not be below or equal to 0");
        }
        simulationTime=simTime;
    }
    /**
     * Gets how long the simulation will run
     * @return amount of time in seconds of how long to run simulation
     */
    public int getSimTime() {
       return simulationTime;
    }
    /** Gets the amount of time it takes to open and close the elevator door
     * @return the doorOperationTime
     */
    public int getDoorOperationTime() {
        return doorOperationTime;
    }

    /**Sets the amount of time it takes to open and close the door
     * @param doorOperationTime the doorOperationTime to set
     */
    public void setDoorOperationTime(int doorOperationTime) throws InvalidDataException {
        if(doorOperationTime<1){
            throw new InvalidDataException("doorOperationTime can not be below or equal to 0");
        }
        this.doorOperationTime = doorOperationTime;
    }
    
    /** Get the time ratio for simulation to real time
     * @return the ratio
     */
    public int getRatio() {
        return ratio;
    }

    /** Set the time ratio for simulation to real time
     * @param ratio the ratio to set
     */
    public void setRatio(int ratio) throws InvalidDataException {
        if(ratio<=0){
            throw new InvalidDataException("ratio cannot be below or equal to 0");
        }
        this.ratio = ratio;
    }
    
    /**Gets the time it takes for the elevator to go one floor
     * @return the travelTime
     */
    public int getTravelTime() {
        return travelTime;
    }

    /**Sets the time it takes for the elevator to go one floor
     * @param travelTime the travelTime to set
     */
    public void setTravelTime(int travelTime) throws InvalidDataException {
        if(travelTime<1){
            throw new InvalidDataException("travelTime can not be below or equal to 0");
        }
        this.travelTime = travelTime;
    }
    /** Gets the max amount of people that can exist in the elevator
     * @return the maxPeople
     */
    public int getMaxPeople() {
        return maxPeople;
    }

    /**Sets the max amount of people that can exist in the elevator
     * @param maxPeople the maxPeople to set
     */
    public void setMaxPeople(int maxPeople) throws InvalidDataException {
        if(maxPeople<1){
            throw new InvalidDataException("max people can not be below or equal to 0");
        }
        this.maxPeople = maxPeople;
    }
    /**
     * This returns the array reference to the percentage tables
     * @return a table of percentages refering to how much population density should on each floor
     */
    private int[] getpercentageStartDest() {
        return percentageStartDest;
    }
    /**Gets the percentage of people starting at each floor
     * @param index 
     * @return the percentageStart
     */
    public int getPercentageStartDest(int index) {
        return percentageStartDest[index];
    }

    /**
     * Sets the percentage of people starting at each floor
     * @param percentageStart the percentageStart to set
     * @throws InvalidDataException when percentageStart has less or more rows than what is necessary
     */
    public void setPercentageStartDest(int[] percentageStart) throws InvalidDataException {
        if(percentageStart.length!=floorCount){
            throw new InvalidDataException("Not enough percentages for each floor");
        }
        this.percentageStartDest = percentageStart;
    }
    /**
     * This method runs the Simulation by creating the people and adding them to floors. The simulation ends when the Time is over and all elevators are at floor 1 and are shutdown
     */
    @Override
    public void run(){
        ArrayList<Person> population= new ArrayList<>();
        Random generator = new Random();
        int numFloors=FloorFacade.getInstance().getNumFloors();
        int popNum=0;
        Time done =Time.valueOf(TimerSingleton.getInstance().getTimeString());
        done.setSeconds(done.getSeconds()+(getSimTime()));
        boolean redo=false;
        int totalCount=getSpawnRatePerMinute()*(getSimTime()/60);
        if(totalCount==0){
            totalCount=1;
        }
        while(Time.valueOf(TimerSingleton.getInstance().getTimeString()).compareTo(done)<0){
            int floor;
            int dest;
            do{
                floor= generator.nextInt(numFloors);//0-14
                dest=generator.nextInt(numFloors);//0-14
                int currentPercentageStart=FloorFacade.getInstance().getTotalCreated(floor)/totalCount;
                if(currentPercentageStart>=getPercentageStartDest(floor)){
                    redo=true;
                }
                //Check with teacher if we are required to do Destinations percentage...
            }
            //Errot checking to ensure Person will have correct start and end floor
            while(redo || floor==dest ||  floor==0  || dest==0 || floor>=getFloorCount() || dest>=getFloorCount());
            
            try {
                population.add(PersonFactory.build(0,getUpgrade(),floor, dest));
                System.out.println(TimerSingleton.getInstance().getTimeString()+": Floor "+floor+ " :a person is created with destination "+ dest);
                FloorFacade.getInstance().addPersonToFloor(population.get(popNum).getLocation(),population.get(popNum));
                popNum++;
                int waitTime=(60000/getSpawnRatePerMinute());
                Thread.sleep(waitTime/TimerSingleton.getInstance().getRatio());
            } catch (InterruptedException ex) {}
            
        }
        ElevatorController.getInstance().totalShutdown();
        while(!ElevatorController.getInstance().allShutdown()){
            try {
                Thread.sleep(2000/TimerSingleton.getInstance().getRatio());
            } catch (InterruptedException ex) {}
        }
        System.out.println("Function is done at " +TimerSingleton.getInstance().getTimeString());
        synchronized(Building.class){
           if(getUpgrade()==0){
               System.out.println("Old Algorithm");
           }
           else{
               System.out.println("New Algorithm");
           }
            personChart(population);
            waitTimeChart(population);
            rideTimeChart(population);
        }
    }
    /**
     * Outputs a data chart analyzing wait times at each level
     * @param data arraylist of people that will be used to fill each spot in the chart
     */
    private void waitTimeChart(ArrayList<Person> data){
        String output;
        System.out.println("________________________________________________________");
        System.out.println("| Floor | Average Time | Min Wait Time | Max Wait Time |");
        // 7,14,15,15
        System.out.println("________________________________________________________");
        for(int floor=1; floor<getFloorCount();floor++){
            long min=0;
            long max=0;
            long average=0;
            long sum=0;
            long count=0;
            boolean start=false;
            for(int index=0;index<data.size();index++){
                if(data.get(index).getLocation()==floor){
                    long currentWait=data.get(index).getWaitTime();
                    if( start==false){
                        start=true;
                        min=currentWait;
                        max=currentWait;
                    }
                    if(min> currentWait){
                        min=currentWait;
                    }
                    if(max<currentWait){
                        max=currentWait;
                    }
                    sum+=currentWait;
                    count++;
                }
            }
            if(count!=0){
                average=sum/count;
            }
            // 7,14,15,15
            output= String.format("|  %d   |   %d seconds  |  %d  seconds   |    %d seconds   |",floor, average/1000,min/1000,max/1000);
            System.out.println(output);
        }
        System.out.println("________________________________________________________");
    }
    /**
     * Outputs a chart of the wait time, start floor, destination and ride time by person
     * @param data arraylist of people that will be used to fill each spot in the chart
     */
    private void personChart(ArrayList<Person> data){
        System.out.println("______________________________________________________________");
        System.out.println("| Person | Wait Time | Start Floor | Destination | Ride Time |");
        //8,11,13,13,11
        System.out.println("______________________________________________________________");
        for(int index=0;index<data.size();index++){
            Person x=data.get(index);
            String output= String.format("|   %d    | %d seconds |     %d      |      %d     | %d seconds |",index,x.getWaitTime()/1000,x.getLocation(),x.getDestination(),x.getRideTime()/1000);
            System.out.println(output);
        }
        System.out.println("______________________________________________________________");
    }
    /**
     * Outputs a chart that calculates average ride time from floor n to destination floor n
     * @param data arraylist of people that will be used to fill each spot in the chart
     */
    private void rideTimeChart(ArrayList<Person> data){
    String output;
    
    //prints tops set of lines
    System.out.print("_______");
    for(int i=1;i<getFloorCount();i++){
        if(i==getFloorCount()/2){
            System.out.print("  To Floor ");
        }
        else {
            System.out.print("___________");
        }
    }
    System.out.print("\n");
    
    //prints top info
    System.out.print("|     |  Floor  ");
    for(int i=1;i<getFloorCount();i++){
        if(i<=9){
            output=String.format("|   %d     ", i);
            System.out.print(output);
        }
        else if(i>9){
           output=String.format("|    %d   ",i);
           System.out.print(output);
        }
            
    }
    System.out.print("|\n");
    
    //prints top/bottom set of lines
    System.out.print("_______");
    for(int i=1;i<getFloorCount();i++){
        System.out.print("___________");
    }
    System.out.print("\n");
    String side= "From Floor";
    //prints data
    for(int floor=1;floor<getFloorCount();floor++){
        if((floor)>side.length() && floor<=9){
            System.out.print("|     | Floor "+(floor)+" ");
        }
        else if(floor>side.length() && floor>9){
            System.out.print("|     | Floor "+(floor));
            
        }
        else if(floor<=9){
            output= String.format("|  %s  | Floor "+floor+ " ", side.charAt(floor-1));
            System.out.print(output);
        }
        else{
            output= String.format("|  %s  | Floor "+floor, side.charAt(floor-1));
            System.out.print(output);
        }
        
        for(int target=1;target<getFloorCount();target++){
            long sum=0;
            long count=0;
            long average;
            for(Person x: data){
                if(x.getLocation()==floor && x.getDestination()==target){
                    sum+=x.getRideTime();
                    count++;
                }
            }
            if(count!=0){
                average=(sum/count)/1000;
            }
            else{
                average=0;
            }
            if(floor!=target){
               output=String.format("|    %d    ",average);
               System.out.print(output);
            }
            else{
                System.out.print("|   X     ");
            }
        }
        System.out.print("|\n");
    }
        
    //prints bottom set of lines
    System.out.print("_______");
    for(int i=1;i<getFloorCount();i++){
        System.out.print("___________");
    }
    System.out.print("\n");
    }
    
}
