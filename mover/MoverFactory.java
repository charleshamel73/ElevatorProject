/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mover;

import elevator.ElevatorController;
import person.Person;

/**
 * Factory class for the creation of Mover
 * @author charl_000
 */
public class MoverFactory {
    /**
     *
     */
    public MoverFactory(){}
    /**
     * builds a Mover using information from the person
     * @param x Person who is doing the moving
     * @return properly delegated Mover Class
     */
    public static Mover build(Person x,int algorithm){
        Mover CBPointer;
        if(ElevatorController.getInstance().isAvailable(x)) {
            if(algorithm==0){
                CBPointer= new MoverUpDown(x.getLocation());
            }
            else{
                CBPointer=new MoverBetterUpDown(x.getLocation());
            }
        }
        else{
            CBPointer= new MoverPending(x.getLocation());
        }
        return CBPointer;
    }
}
