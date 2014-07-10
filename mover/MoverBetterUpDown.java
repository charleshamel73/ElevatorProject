  /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mover;

import elevator.ElevatorController;
import person.Person;
import util.InvalidDataException;

/**
 *First Algorithm: If an elevator is available, this algorithm is used
 * @author charl_000
 */
public class MoverBetterUpDown implements Mover{
    /**
     * floor at which Person resides
     */
    int floorNumber;
    /**
     * Constructor to build MoverUpDown Object
     * @param floor location of Person who is using it
     */
    public MoverBetterUpDown(int floor){
        floorNumber=floor;
    }
    
    /**
     * The first algorithm because the request could be made is now added to a elevator destination list
     * @param x person who request is to be fulfilled
     */
    @Override
    public void requestElevator(Person x) {
        try {
            ElevatorController.getInstance().addBetterDest(x);
        } catch (InvalidDataException ex) {}
    }
}
