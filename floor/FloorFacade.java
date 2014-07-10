/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package floor;

import java.util.ArrayList;
import person.Person;
import person.PersonFactory;

/**
 *This class is a facade class that makes floor data globally available but still keeps the data hidden and secure.
 * @author charl_000
 */


public class FloorFacade {
    /**
     * This is a thread safe instance variable
     */
    private static volatile FloorFacade instance;
    /**
     * Each position in the arraylist represent a floor
     */
    private ArrayList<Floor> floorList;
    /**
     * This ensures that this singleton is threadsafe and will only create one instance of this class
     * @return a reference to the instance variable
     */
    public static FloorFacade getInstance(){
        if(instance==null){
            synchronized(FloorFacade.class){
                if(instance==null){
                    instance= new FloorFacade();
                }
            }
        }
        return instance;
    }
    /**
     * This creates a number of floors based on numFloor
     * @param numFloor Amount of floors to create
     */
    public void createFloors(int numFloor){
        floorList = new ArrayList<>();
        for(int i=0;i<numFloor;i++){
            getFloorList().add(FloorFactory.build(0, i));
        }
    }
    
    /**Returns the arraylist of floors for the building
     * @return the floorList
     */
    private ArrayList<Floor> getFloorList() {
        return floorList;
    }
    
    /**
     * Gets number of floors within array List
     * @return the size of FloorList
     */
    public int getNumFloors(){
        return getFloorList().size();
    }
    
    /**
     *  This function adds a person to target floor
     * @param floor The floor that the person will be added to
     * @param x This is the person who will be added
     */
    public void addPersonToFloor(int floor,Person x){
        getFloorList().get(floor).addPerson(x);
    }
    /**
     * Gets how many people are currently on a target floor
     * @param i The floor to look into
     * @return the size of the Person Array
     */
    public int getPopulation(int i){
        return getFloorList().get(i).getPopulation();
    }
    /**
     * Gets how many people have been created and placed on target floor
     * @param floor the floor to look into
     * @return how many people have been created and placed
     */
    public int getTotalCreated(int floor){
        return getFloorList().get(floor).getTotalCreated();
    }
    /**
     *This checks if a target Person is able to enter the elevator if it is going in the same direction
     * @param index the index within the person arraylist of which person your targeting
     * @param elevatorLocation the current location of the elevator which will act as the floor to look up
     * @param goingUp If the elevator is currently going up or down
     * @param isEmpty boolean representing if destinations list is empty 
     * @return true if the target person can enter the elevator
     */
    public boolean canCome(int index,int elevatorLocation, boolean goingUp, boolean isEmpty){
        boolean travelingUp=getFloorList().get(elevatorLocation).getPerson(index).getTravelUp();
        if(travelingUp==goingUp || isEmpty){
            return true; 
        }
        return false;
    }
    /**
     * Gets target person from target floor and returns it while removing it from the array in the floor
     * @param floor the floor your looking into
     * @param index the location within the Person arraylist in the floor
     * @return A Person object that is a copy of the Person at target floor target index
     */
    public Person getPersonFromFloor(int floor,int index){
        Person data=getFloorList().get(floor).getPerson(index);
        getFloorList().get(floor).removePerson(index);
        return data;
    }
    /**
     * Checks to see if someone on the floor is able to enter the elevator if he is traveling in same direction
     * @param floor the floor that you are looking into
     * @param travelUp the direction of the elevator
     * @return true if a person is going in the same direction of travel
     */
    public boolean isSomeoneOnFloor(int floor,boolean travelUp){
        Floor level=getFloorList().get(floor);
        if(level.getPopulation()>0){
            for(int i=0;i<level.getPopulation();i++){
                Person target=level.getPerson(i);
                if(target.getTravelUp()==travelUp){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * This prints out all the floor population data
     */
    public void BlowOut(){
        System.out.println();
        for(int i=0;i<getFloorList().size();i++){
            System.out.println(getFloorList().get(i).toString());
        }
        System.out.println();
        
    }
    /**
     * This checks to see if all floors are empty. Use before termination to ensure completion
     * @return true if even a floor one person
     */
    public boolean floorsEmpty(){
        int sum=0;
        for(int i=0;i<getFloorList().size();i++){
            sum+=getFloorList().get(i).getPopulation();
        }
        if(sum==0) {
            return true;
        }
        return false;
    }
}

