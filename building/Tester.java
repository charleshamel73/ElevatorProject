/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package building;

/**
 *
 * @author charl00
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.InvalidDataException;
/**
 *
 * @author charl_000
 */
public class Tester{
    
    /**
     *
     * @param args
     * @throws InterruptedException
     * @throws InvalidDataException
     */
    public static void main(String[] args) throws InterruptedException, InvalidDataException {
        try {
            /*
             *insert elevator initialization... 
             */
            
            /*
            // Elevators are created under type 0, with id 0-5, and in
            //a building that max floor is 15
            Elevator cw1=ElevatorFactory.build(0, 0, 15,1,1);
            Elevator cw2=ElevatorFactory.build(0, 1, 15,1,1);
            Elevator cw3=ElevatorFactory.build(0, 2, 15,1,1);
            Elevator cw4=ElevatorFactory.build(0, 3, 15,1,1);
            Elevator cw5=ElevatorFactory.build(0, 4, 15,1,1);
            Elevator cw6=ElevatorFactory.build(0, 5, 15,1,1);
            
            // Create 2 thread objects (t1-t6) using cw1-cw6.
            // Now we can run these in their own thread
            Thread t1 = new Thread((ElevatorPassengerImpl)cw1); 
            Thread t2 = new Thread((ElevatorPassengerImpl)cw2);
            Thread t3 = new Thread((ElevatorPassengerImpl)cw3); 
            Thread t4 = new Thread((ElevatorPassengerImpl)cw4);
            Thread t5 = new Thread((ElevatorPassengerImpl)cw5); 
            Thread t6 = new Thread((ElevatorPassengerImpl)cw6);
            
            
            // Start the 2 threads (execute their "run" method)
            // Here we wait for both threads t1 & t2 to finish.
            //First Test
            
            System.out.println("DESTINATION 11 ADDED TO ELEVATOR 1");
            cw1.addDest(11);//at floor 1
            Thread.sleep(1000);//cw1 at floor 2
            System.out.println("DESTINATION 14 ADDED TO ELEVATOR 2");
            cw2.addDest(14);//cw1 at 2 cw2 at 1
            Thread.sleep(2000);//cw1 at 4 cw2 at 3
            System.out.println("DESTINATION 13 ADDED TO ELEVATOR 2");
            cw2.addDest(13);
            System.out.println("DESTINATION 15 ADDED TO ELEVATOR 2");
            cw2.addDest(15);
            Thread.sleep(20000);//cw1 at 14 cw2 at 13 
            
            //second test
            System.out.println("DESTINATION 10 ADDED TO ELEVATOR 1");
            cw1.addDest(10);
            Thread.sleep(2000);
            System.out.println("DESTINATION 1 ADDED TO ELEVATOR 1");
            cw1.addDest(1);
            Thread.sleep(12000);
            System.out.println("DESTINATION 2 ADDED TO ELEVATOR 1");
            cw1.addDest(2);
            System.out.println("DESTINATION 5 ADDED TO ELEVATOR 1");
            cw1.addDest(5);
            System.out.println("DESTINATION 3 ADDED TO ELEVATOR 1");
            cw1.addDest(3);
            try {
                Thread.sleep(1000);
                cw1.shutdown();
                cw2.shutdown();
                cw3.shutdown();
                cw4.shutdown();
                cw5.shutdown();
                cw6.shutdown();
                t1.join();
                t2.join();
                t3.join();
                t4.join();
                t5.join();
                t6.join();
                
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            */
            Building simulation= new Building("Configuration.ser");
            Building simulation2= new Building("Configuration2.ser");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}