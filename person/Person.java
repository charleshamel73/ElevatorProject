/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author charl_000
 */
package person;

import mover.Mover;
/**
 * Interface to create multiple Person Object for any type of specialization
 * @author charl_000
 */
public interface Person {
    void pressButton();
    boolean getTravelUp();
    void arrived();
    void pickedUp();
    long getWaitTime();
    long getRideTime();
    long getTimeStorage();
    int getDestination();
    int getLocation();
    String toString();
}
