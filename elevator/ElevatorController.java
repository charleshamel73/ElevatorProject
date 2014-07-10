/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Singleton Facade that takes request to the elevators and allows for
 * information hiding and can be accessed anywhere and only has one instance.
 * Usage example:
 * <pre>
 * ElevatorController.getInstance()
 * </pre>
 *
 * @author Charles Hamel
 * @since Version 1.0
 * @see #getInstance() 
 */

package elevator;
import util.InvalidDataException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import person.Person;
import util.TimerSingleton;
/**
 *
 * @author charl_000
 */
public class ElevatorController {
    /**
     * The list of elevators controlled by this facade.
     * @see Elevator
     */
    private ArrayList<Elevator> elevatorList;
    /**
     * The list of destinations that are needed when no elevator is available
     */
    private ArrayList<Person> pendingDest = new ArrayList<>();
    /**
     * helps keep the instance thread-safe .
     */
    private static volatile ElevatorController instance;
    /**
     * Constructor for ElevatorController and can only be accessed from within the class.
     */
    private ElevatorController(){};
    /**
     * Gets a list of elevators
     * @return arraylist of type Elevators
     */
    private ArrayList<Elevator> getElevatorList() {
        return elevatorList;
    }
    /**
     * Gets a list of pending Destinations that the elevators were unable to goto
     * @return an arraylist of Persons who are waiting to be picked up
     */
    private ArrayList<Person> getPendingDest() {
        return pendingDest;
    }
    /**
     * Creates a list of elevators and adds their information within elevatorList
     * @param numElevators number of elevators to create
     * @param maxFloor maxFloor of the building
     * @param defaultFloor floor to travel to once elevator is idle
     * @param maxPeople max amount of people that can ride each floor
     * @param doorOp how long it takes to open and close the elevator doors
     * @param travelT how long it takes to tranverse one floor
     * @throws InvalidDataException When start or defaultFloor are below 0 or above maxFloor
     */
    public void createElevators(int numElevators,int maxFloor,int defaultFloor,int maxPeople, int doorOp, int travelT) throws InvalidDataException{
        elevatorList = new ArrayList<>();
        for(int i=0;i<numElevators;i++){
            getElevatorList().add(ElevatorFactory.build(0,i,maxFloor,defaultFloor,maxPeople,doorOp,travelT));
        }
    }
    /**
     * creates the first instance of ElevatorController and is thread-safe.
     * @return instance of itself
     */
    public static ElevatorController getInstance(){
        if(instance==null){
            synchronized(ElevatorController.class){
                if(instance==null){
                    instance= new ElevatorController();
                }
            }
        }
        return instance;
    }
    /**
     *  Checks all elevators to see if any are available so that person x may be picked up
     * @param x This is person who is waiting for the elevator
     * @return true if an elevator is available for Person x to get to destination with
     */
    public boolean isAvailable(Person x){
        Elevator elevPtr=null;
        
        for(int i=0;i<getElevatorList().size();i++){
            int elevLoc =getElevatorList().get(i).getLocation();
            int playerLoc=x.getLocation();
            boolean elevDir=getElevatorList().get(i).goingUp();
            boolean playerDir=x.getTravelUp();
            if(elevLoc==1){
                elevPtr=getElevatorList().get(i);
            }
            else if((getElevatorList().get(i).getIdle())){
                elevPtr=getElevatorList().get(i);
            }
            else if(playerDir && elevDir && elevLoc<playerLoc){
                elevPtr=getElevatorList().get(i);
            }
            else if(!playerDir && !elevDir && elevLoc>playerLoc){
               elevPtr=getElevatorList().get(i); 
            }
        }
        if(elevPtr==null) {
            return false;
        }
        return true;
    }

    /**
     * If an elevator runs out of destinations, it will seek pending destinations and pick them up 
     * @param elevatorIndex the location of the elevator within elevatorList
     * @throws InvalidDataException when the destination is below 0 or above maxFloor
     * @see #elevatorList

     */
    public void addPendingDest(int elevatorIndex) throws InvalidDataException{
        boolean grabAbove=false;
        Elevator elevator=getElevatorList().get(elevatorIndex);
        ArrayList<Integer> locStorage = new ArrayList<>();
        Person x;
        synchronized(getPendingDest()){
            for(int i=0;i<getPendingDest().size();i++){
                x = getPendingDest().get(i);                    //going down
                if(x.getLocation()>elevator.getLocation()){
                    locStorage.add(i);
                }
            }
            if(locStorage.isEmpty()){
                for(int i=0;i<getPendingDest().size();i++){
                    x = getPendingDest().get(i);
                    if(x.getLocation()<elevator.getLocation()){
                        locStorage.add(i);
                    }
                }
            }
            Collections.sort(locStorage);
            Collections.reverse(locStorage);
            for (int j=0;j<locStorage.size();j++) {
                int target=locStorage.get(j);
                elevator.addDest(getPendingDest().get(target).getLocation());
                if(grabAbove){
                    elevator.addDest(getPendingDest().get(target).getDestination());
                }
                getPendingDest().remove(target);
            }
            System.out.println(TimerSingleton.getInstance().getTimeString()+"Pending: "+getPendingDest().toString());
            
        }
    }
     /**
     * First Algorithm: If a elevator is available it uses this to directly add to a elevators destination List
     * @param x The person whose request is being fulfilled
     * @throws InvalidDataException When the destination that is added is below 0 or above maxFloor
     */
    public void addDest(Person x) throws InvalidDataException{
        Elevator elevPtr=null;
        for(int i=0;i<getElevatorList().size();i++){
            Elevator target=getElevatorList().get(i);
            boolean elevDir=target.goingUp();
            boolean playerDir=x.getTravelUp();
            int elevLoc= target.getLocation();
            int playerLoc=x.getLocation();
            if(playerDir && elevDir&& elevLoc<playerLoc){
                elevPtr=target;
            }
            if(!playerDir && !elevDir && elevLoc>playerLoc){
                elevPtr=target;
            }
            if(target.getIdle() || elevLoc==getElevatorList().get(i).getDefault()){
                elevPtr=target;
            }
        }
        if(elevPtr!=null){
            elevPtr.addDest(x.getLocation());
            if(x.getTravelUp()){
                elevPtr.addDest(x.getDestination());
            }
        }
    }
     /**
     * First Algorithm Upgraded: If a elevator is available it uses this to directly add to a elevators destination List
     * @param x The person whose request is being fulfilled
     * @throws InvalidDataException When the destination that is added is below 0 or above maxFloor
     */
    public void addBetterDest(Person x) throws InvalidDataException{
        Elevator elevPtr=null;
        int i=0;
        boolean found=false;
        for(int index=0;index<getElevatorList().size();index++){
            if(getElevatorList().get(index).checkDest(x.getLocation())&&(getElevatorList().get(index).goingUp()==x.getTravelUp() || getElevatorList().get(index).getChanging())){
                found=true;
            }
        }
        while(found==false && i<getElevatorList().size()){
            Elevator target=getElevatorList().get(i);
            boolean elevDir=target.goingUp();
            boolean playerDir=x.getTravelUp();
            int elevLoc= target.getLocation();
            int playerLoc=x.getLocation();
            if(playerDir==elevDir && elevLoc<playerLoc){
                elevPtr=target;
                found=true;
            }
            if(!playerDir==!elevDir && elevLoc>playerLoc){
                elevPtr=target;
                found=true;
            }
            if(target.getIdle() || elevLoc==target.getDefault() || target.getChanging()){
                elevPtr=target;
                found=true;
            }
            i++;
        }
        if(elevPtr!=null){
            elevPtr.addDest(x.getLocation());
            if(x.getTravelUp()){
                elevPtr.addDest(x.getDestination());
            }
        }
    }
    /**
     * Second Algorithm: if a request can't currently be fulfilled it is added to pending Destinations
     * @param x the Person who should be added to the list
     */
    public void addPending(Person x){
        synchronized(getPendingDest()){
            getPendingDest().add(x);
            System.out.println(TimerSingleton.getInstance().getTimeString()+"Pending: "+getPendingDest().toString());
        }
    }
    /**
     * Starts the shutdown protocol to all elevators
     */
    public void totalShutdown() {
        for(Elevator x: getElevatorList()){
            x.shutdown();
        }
    }
    /**
     * Checks to see if all elevators have finalized shutdown sequence
     * @return true if all floors are empty, passengers are empty and all elevators are at default floors
     */
    public boolean allShutdown() {
        int sum=0;
        for(Elevator x: getElevatorList()){
            if(!x.getRunning()){
                sum++;
            }
        }
        if(getElevatorList().size()==sum){
            return true;
        }
        return false;
    }
    
    /**
     * This prints out passenger data of every elevator
     */
    public void BlowOut(){
        System.out.println();
        for(int i=0;i<getElevatorList().size();i++){
            System.out.println(getElevatorList().get(i).toString());
        }
        System.out.println();
        
    }
}
