/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package elevator;

import util.InvalidDataException;
/**
 * An interface that allows for multiple implementations of Elevator.<br>
 * @author Charles Hamel
 * @since Version 1.0
 */
public interface Elevator{
    void addDest(int destination) throws InvalidDataException;
    void shutdown();
    boolean goingUp();
    int getLocation();
    int getDefault();
    boolean getChanging();
    boolean checkDest(int location);
    boolean getIdle();
    boolean getRunning();
    
}
