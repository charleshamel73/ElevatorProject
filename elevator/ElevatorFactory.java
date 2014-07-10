/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author charl_000
 */

package elevator;

import util.InvalidDataException;

/*
 *This is a static Factory class that can be accessed
 * from multiple points. <br>
 * Usage example:
 * <pre>
 * Elevator y= ElevatorFactory(0,1,4);
 * </pre>
 *
 * @author Charles Hamel
 * @since Version 1.0
 */
/**
 *
 * @author charl_000
 */
public class ElevatorFactory {
    private ElevatorFactory(){}
     /**
     * A simple Factory class that builds and sends the Elevator
     *
     * @param elevatorType the elevator Types 
     * current: 0_OfficeImpl.
     * @param id the id of the elevator.
     * @param maxFloor the top floor of the building
     * @param defaultFloor default floor of the building
     * @param max the max amount of people that can ride in the elevator
     * @param doorOp how long it takes to open and close the doors
     * @param TravelT how long it takes to travel one floor
     * @return reference to Elevator class
     * @throws InvalidDataException if any of the five parameter don't follow proper format
     * @see InvalidDataException
     * @see ElevatorPassengerImpl
     **/
    
    public static Elevator build(int elevatorType,int id,int maxFloor,int defaultFloor,int max, int doorOp, int TravelT) throws InvalidDataException{
        Elevator elevatorPointer=null;
        switch(elevatorType){
            case 0:
                elevatorPointer= new ElevatorPassengerImpl(id,maxFloor,defaultFloor,max,doorOp,TravelT);
        }    
        return elevatorPointer;
    }
}
