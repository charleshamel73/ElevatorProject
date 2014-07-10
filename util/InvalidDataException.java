/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 * Exception class for whenever improper forms of data are handed over to setClasses
 * @author charl_000
 */
public class InvalidDataException extends Exception {

    /**
     *
     * @param string
     */
    public InvalidDataException(String string) {
        super(string);
    }
    
}
