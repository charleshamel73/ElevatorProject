/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mover;

import elevator.ElevatorController;
import person.Person;

/**
 * Second Algorithm for moving a person if the request can not be made
 11* @author charl_000
 */
public class MoverPending implements Mover{
    /**
     * floor at which Person resides
     */
    int floorNumber;
    /**
     * Constructor to build MoverPending
     * @param floor the floor at which the person resides
     */
    public MoverPending(int floor){
        floorNumber=floor;
    }
    /**
     * Because the request could not be made this request elevator adds the Person to pendingDest
     * @param x The person in question
     */
    @Override
    public void requestElevator(Person x) {
        ElevatorController.getInstance().addPending(x);
    }
}
