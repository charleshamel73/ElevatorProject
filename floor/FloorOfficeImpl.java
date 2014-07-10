/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author charl_000
 */
package floor;

import java.util.ArrayList;
import person.Person;
import util.InvalidDataException;

/**
 *
 * @author charl_000
 */
public class FloorOfficeImpl implements Floor{
    /**
     * This is the list of people that are on this floor
     */
    private ArrayList<Person> personList;
    /**
     * This is the location within the floor
     */
    private int location;
    /**
     * This is the total amount of people added to the floor
     */
    private int totalCreated;
    
    /**
     *Constructor that builds a floor for floor location
     * @param location floor number
     */
    public FloorOfficeImpl(int location) throws InvalidDataException{
        setLocation(location);
        personList= new ArrayList<>();
        totalCreated=0;
    }
    
    /**Returns the arraylist of people on the floor
     * @return the personList
     */
    private ArrayList<Person> getPersonList() {
        return personList;
    }
    
    /**Gets the total number of people created
     * @return the totalCreated
     */
    public int getTotalCreated() {
        return totalCreated;
    }

    /** Modifies current number of people
     */
    private void addTotalCreated() {
        totalCreated++;
    }
    /**
     * Adds a person to the list of people currently on floor and increments total created and than presses button
     * @param x the person that will be added to the list
     */
    @Override
    public void addPerson(Person x) { 
        getPersonList().add(x);
        addTotalCreated();
        getPersonList().get( getPersonList().indexOf(x) ).pressButton();
    }
    /**
     * Gets the amount of people currently on floor
     * @return size of PersonList
     */
    public int getPopulation(){
        return getPersonList().size();
    }
    /**
     * Sets location of person to target floor
     * @param loc this is the floorNumber
     */
    private void setLocation(int loc) throws InvalidDataException{
        if(loc<0){
            throw new InvalidDataException("Location can not be below 0");
        }
        location=loc;
    }
    /**
     * removes a person from the list of people at the target index
     * @param i location within the list of people
     */
    public void removePerson(int i){
        getPersonList().remove(i); 
    }
    /**
     * Gets the person from the list of people at target index
     * @param i location within the list of people
     * @return Person at index i in the list of people
     */
    @Override
    public Person getPerson(int i) {
        return getPersonList().get(i);
    }
    /**
     * Creates a string representation of the list of people
     * @return a string representation of the people currently in the floor
     */
    public String toString(){
        return getPersonList().toString();
    }

    
}
