
/**
 *Implementation of a Basic Office Person who starts at one floor and travels to another
 * @author charl_000
 */
package person;

import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import mover.Mover;
import mover.MoverFactory;
import util.InvalidDataException;
import util.TimerSingleton;

/**
 *
 * @author charl_000
 */
public class PersonOfficeImpl implements Person{
    /**
     * Boolean Value representing whether or not the person is traveling up
     */
    private boolean travelUp=false; 
    /** 
     * Mover class that delegates how he request the elevator
     */
    private Mover myMover;
    /**
     * start floor
     */
    private int location=0;
    /**
     * End floor
     */
    private int destination=0;
    /**
     * Wait time for the elevator
     */
    private long waitTime;
    /**
     * time taken to get from start to dest once in elevator
     */
    private long rideTime;
    /**
     * time storage variable
     */
    private long timeStorage;
    /**
     * Constructor that sets up PersonOfficeImpl with target start and end floor
     * @param loc location of start floor
     * @param dest location of end floor
     */
    public PersonOfficeImpl(int algorithm,int loc,int dest){
        try {
            setLocation(loc);
            setDestination(dest);
        } catch (InvalidDataException ex) {}
        if(getLocation()<getDestination()) {
            setTravelUp(true);
        }
        else {
            setTravelUp(false);
        }
        createMover(algorithm);
    }
    /**
     * Creates the proper mover for this Person
     */
    private void createMover(int algorithm){
        myMover=MoverFactory.build(this,algorithm);
    }
    /**Gets the amount of time it took from the request to pickup 
     * @return the amount of time it took from the request to pickup 
     */
    public long getWaitTime() {
        return waitTime;
    }

    /**Sets the amount of time it took from the request to pickup
     * @param waitTime the amount of time it took
     */
    private void setWaitTime(long waitTime) throws InvalidDataException {
        if(waitTime<0){
            throw new InvalidDataException("waitTime can not be below 0");
        }
        this.waitTime = waitTime;
    }

    /** Gets the time it took from getting in the elevator to getting off of it
     * @return the time it took from getting in the elevator to getting off of it
     */
    public long getRideTime() {
        return rideTime;
    }

    /** Sets the time it took from getting in the elevator to getting off
     * @param rideTime the time it took from getting in the elevator to getting off
     */
    private void setRideTime(long rideTime) throws InvalidDataException {
        if(waitTime<0){
            throw new InvalidDataException("rideTime can not be below 0");
        }
        this.rideTime = rideTime;
    }

    /** Gets the time value stored in for calculations
     * @return the time value that was last stored
     */
    public long getTimeStorage() {
        return timeStorage;
    }

    /** Sets the time in long value that will be used for calculations
     * @param time the time in long value that is to be set
     */
    public void setTimeStorage(long time) throws InvalidDataException {
        if(waitTime<0){
            throw new InvalidDataException("time storage can not be below 0");
        }
        timeStorage = time;
    }
    /**
     * Sets up the start location of this person
     * @param loc location of start floor
     */
    private void setLocation(int loc) throws InvalidDataException{
        if(loc <=0){
            throw new InvalidDataException("location can not be below 0");
        }
        location=loc;
    }
    /**
     * Sets up boolean value of traveling up
     * @param state true if the elevator is going up
     */
    private void setTravelUp(boolean state){
        travelUp=state;
    }
    /**
     * Sets up the end destination with target destination
     * @param dest location of end floor
     */
    private void setDestination(int dest) throws InvalidDataException{
        if(dest<=0){
            throw new InvalidDataException("Dest can not be below or equal to 0");
        }
        destination=dest;
    }
    /**
     * Gets the start location of this Person
     * @return location of start floor
     */
    @Override
    public int getLocation(){
        return location;
    }
    /**
     * Gets whether or not this Person is going up or down
     * @return true if person is going up
     */
    @Override
    public boolean getTravelUp(){
        return travelUp;
    }
    /**
     * Gets the end destination floor
     * @return location of end floor
     */
    @Override
    public int getDestination(){
        
        return destination;
    }
    /**
     * Presses the "CallBox" button and sends the request to Elevator Controller based on which Mover this Person was given.
     */
    @Override
    public void pressButton() {
       if(getLocation()<getDestination()){
            System.out.println(TimerSingleton.getInstance().getTimeString()+": Floor "+getLocation() +": Up call Button is pressed");                    
        }
        else{
            System.out.println(TimerSingleton.getInstance().getTimeString()+": Floor "+getLocation() +": Down call Button is pressed");  
       }
       Time current=Time.valueOf(TimerSingleton.getInstance().getTimeString());
       long currentLong=current.getTime();
        try {
            setTimeStorage(currentLong);
        } catch (InvalidDataException ex) {}
       myMover.requestElevator(this);
    }
    /**
     * Creates a string representation of this person
     * @return String of start location and end destination
     */
    public String toString(){
        return ("From: "+ getLocation()+ "/Dest: "+ getDestination() + "..");
    }
    /**
     * checks the current time and sets the ridetime
     */
    @Override
    public void arrived() {
        Time current=Time.valueOf(TimerSingleton.getInstance().getTimeString());
        long currentLong=current.getTime();
        try {
            setRideTime(currentLong-getTimeStorage());
        } catch (InvalidDataException ex) {}
        System.out.println("Person from floor "+ getLocation() + " had a ride time of "+ getRideTime()/1000+ " seconds.");
    }
    /**
     * Checks the current time, sets the wait time and resets timeStorage with the current time
     */
    @Override
    public void pickedUp() {
        Time current=Time.valueOf(TimerSingleton.getInstance().getTimeString());
        long currentLong=current.getTime();
        long difference=currentLong-getTimeStorage();
        try {
            setWaitTime(difference);
            setTimeStorage(currentLong);
        } catch (InvalidDataException ex) {}
        
        System.out.println("Person from floor "+ getLocation() + " had a wait time of "+ getWaitTime()/1000+ " seconds.");
    }

    
}
