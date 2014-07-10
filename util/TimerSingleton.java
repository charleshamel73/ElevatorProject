/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import javax.swing.Timer;


/**
 *Singleton for the time keeping aspect of the program
 * @author charl_000
 */
public class TimerSingleton {
    /**
     * thread-safe instance
     */
    private static volatile TimerSingleton instance;
    /**
     * Allows for use of the timer to update each simulated second
     */
    private Timer timer;
    /**
     * The current simulation to real time ratio
     */
    private int rate;
    /**
     * The current simulation time
     */
    private static  Time time=new Time(0);
    /** Gets the variable time
     * @return the time
     */
    private Time getTime() {
        return time;
    }

    /** Sets the time to aTime
     * @param aTime the time to set
     */
    private void setTime(long aTime) throws InvalidDataException {
        if(aTime==0){
            throw new InvalidDataException("Time can not be below or equal to 0");
        }
        time.setTime(aTime);
    }
    
    /**
     * Constructor that can only be called within the class
     */
    private TimerSingleton(){}
    /**
     * Initiates a single instance and allows access to this Singleton
     * @return TimerSingleton
     */
    public static TimerSingleton getInstance(){
        if(instance==null){
            synchronized(TimerSingleton.class){
                if(instance==null){
                    instance= new TimerSingleton();
                }
            }
        }
        return instance;
    }
    /**
     * This is code that updates the clock every "simulation" second
     */
    ActionListener updateClockAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          // Assumes clock is a custom component
            synchronized(getTime()){
                Time newTime=Time.valueOf(getTime().toString());
                newTime.setSeconds(newTime.getSeconds()+1);
                try {
                    setTime(newTime.getTime());
                } catch (InvalidDataException ex) {}
            }
        }
    };
    /**
     * Sets the time rate to target ratio
     * @param ratio the time ratio that should be made
     */
    public void setRatio(int ratio) throws InvalidDataException{
        if(ratio<1){
            throw new InvalidDataException("Ratio cannot be less than 1");
        }
        rate=ratio;
        timer = new Timer((1000/rate),updateClockAction);
        timer.start();
    }
    /**
     * Gets the time rate ratio
     * @return time rate ratio
     */
    public int getRatio(){
        return rate;
    }
    /**
     * Gets a string representation of the current time
     * @return String representation of current time
     */
    public String getTimeString(){
        synchronized(getTime()){
            return getTime().toString();
            //Sometimes returns Null which makes little to no sense
        }
    }
}
