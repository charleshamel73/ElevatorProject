package floor;

import person.Person;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *Interface that allows for multiple creations of Floor
 * @author charl_000
 */
public interface Floor {
    void addPerson(Person x);
    int getPopulation();
    Person getPerson(int i);
    void removePerson(int i);
    int getTotalCreated();
}
