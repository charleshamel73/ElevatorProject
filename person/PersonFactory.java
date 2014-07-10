/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author charl_000
 */
package person;
/**
 * Factory class to properly delegate and create Person Objects
 * @author charl_000
 */
public class PersonFactory {
    private PersonFactory(){};
    /**
     * Builds a Person Object based on PersonType with the data location and destination
     * @param personType 0:Office Person
     * @param location starting floor of the person
     * @param destination where the person wants to go
     * @return a Person object of type personType with the right specifications
     */
    public static Person build(int personType,int algorithm,int location,int destination)
    {
        Person personPointer=null; 
        switch(personType){
            case 0:
                personPointer=new PersonOfficeImpl(algorithm,location,destination);
                break;
        }
        return personPointer;
    }
}
