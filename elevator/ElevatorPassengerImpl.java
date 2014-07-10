/**
 * A class representing a Passenger Elevator.<br>
 * Usage example:
 * <pre>
 * Elevator myElevator = ElevatorPassengerImpl(1, 2,5,1,1);
 * </pre>
 *
 * @author Charles Hamel
 * @since Version 1.0
 */
package elevator;
import floor.FloorFacade;
import java.util.ArrayList;
import java.util.Collections;
import person.Person;
import util.InvalidDataException;
import util.TimerSingleton;
/**
 *
 * @author charl_000
 */
public class ElevatorPassengerImpl implements Elevator,Runnable{
    //private int passengerCount=0;
    /**
     * The Elevator's thread.
     * @see java.lang.Thread
     * @see #run() 
     */
    private Thread t;
    /**
     * The arraylist of passengers in the elevator
     */
    private ArrayList<Person> passengers=new ArrayList<>();
    
    /**
     * The current floor the elevator was located on.
     * @see #setLocation(int)  
     * @see #getLocation()
     **/
    private int location;
    
    /**
     * The floor that the elevator would travel to once it was idle.
     * @see #setDefault(int)
     * @see #getDefault() 
     */
    private int defaultFloor;
    
    /**
     * The elevator id.
     * @see #setId(int) 
     * @see #getDefault() 
     */
    private int identification;
    
    /**
     * The max floor of the building.
     * @see #setMaxFloor(int) 
     * @see #getMaxFloor() 
     */
    private int maxFloor;
    
    /**
     * Boolean that represented whether or not the elevator was still running.
     * @see #setRunning(boolean)
     * @see #getRunning()
     **/
    private boolean running;
    
    /**
     * Boolean that represented whether or not the elevator is idle.
     * @see #setIdle(boolean)
     * @see #getIdle() 
     */
    private boolean isIdle;
    /**
     * Boolean that represented whether or not the elevator is in the process of changing directions.
     * @see #setIdle(boolean)
     * @see #getIdle() 
     */
    private boolean changingDirection;
    /**
     * Boolean that represented whether or not shutdown protocol is active.
     * @see #setShutdown(boolean)
     * @see #getShutdown() 
     */
    private boolean shutdown;
    
    /**
     * An ArrayList<> of the destinations.
     * @see java.util.ArrayList
     * @see #addDest(int) 
     * @see #getDefault() 
     */
    private ArrayList<Integer> destinationList;
    /**
     * This is the maximum people that can ride one elevator
     */
    private int maxPassengerCount;
    
    /**
     * This is the time to traverse each floor.
     */
    private int travelPerFloor;/*miliseconds*/
    
    /**
     * This is the time it takes for the door to remain open.
     */
    private int doorOperationTime;/*miliseconds 2200*/
    
    /**
     * This is the time it waits to take request till it goes idle.
     */
    private int idleTime=1500;/*miliseconds 1500*/
    
    /**
     * A constructor for the passenger implementation of Elevator and starts the thread
     *
     * @param id the id of the elevator.
     * @param topFloor the top floor of the building
     * @param defaultF default floor of the building
     * @param max max amount of people that can fit in the elevator
     * @param doorOp the time it takes to open and close the elevator doors
     * @param TravelT the time it takes to travel one floor
     * @throws InvalidDataException if any of the four parameter don't follow proper format
     * @see InvalidDataException
     * @see ElevatorPassengerImpl
     **/
    public ElevatorPassengerImpl(int id,int topFloor,int defaultF, int max, int doorOp, int TravelT) throws InvalidDataException{
        setMaxFloor(topFloor);
        setRunning(true);
        setId(id);
        setDefault(defaultF);
        setLocation(defaultF);
        setMaxPassengerCount(max);
        setDoorOperationTime(doorOp);
        setTravelPerFloor(TravelT);
        destinationList = new ArrayList<>();
        
        t= new Thread(this);
        System.out.println(TimerSingleton.getInstance().getTimeString()+": Elevator "+ getId()+ " is running");
        t.setName("Elevator "+ getId());
        t.start();
    }
    /**
     * Checks whether the elevator is currently going up or down.
     * @return true if going up:false if otherwise.
     */
    public boolean goingUp(){
        if(getLocation()==1 && getDestinationList().isEmpty())
        {
            return true;
        }
        else if(getLocation()>1 && getDestinationList().isEmpty()){
            setChanging(true);
            return false;
        }
        else if(getLocation()>getNextDest()){
            return false;
        }
        else{
            return true;
        }
    }
    
    /**
     * Returns destination
     * @see #destinationList
     * @return the first destination on the list.
     */
    public int getNextDest(){
        return destinationList.get(0);
    }
    
    /**
     * Returns the destinationList
     * @see #destinationList
     * @return the arraylist of destinations.
     */
    private ArrayList<Integer> getDestinationList() {
        return destinationList;
    }
    
    /**
     * Sets the current location of the elevator
     * @param loc this is the location you are passing
     * @throws InvalidDataException when the location is either above the max floor or below 0
     */
    public void setLocation(int loc) throws InvalidDataException{
        if(loc>getMaxFloor()){
            throw new InvalidDataException("Floor "+loc + " doesn't exist: It is above "+ getMaxFloor());
        }
        if(loc<0){
            throw new InvalidDataException("Floor "+loc + " doesn't exist: It is below 0");
        }
        location=loc;
    }
    
    /**Returns current location
     * @return the current location of elevator.
     */
    public int getLocation(){
        return location;
    }
    
    /**
     * Sets the default floor.
     * @param floor this is the floor that will be the default floor
     * @throws InvalidDataException when the floor is either above the max floor or below 0
     */
    public void setDefault(int floor) throws InvalidDataException{
        if(floor>getMaxFloor()){
            throw new InvalidDataException("Floor "+floor + " doesn't exist: It is above "+ getMaxFloor());
        }
        if(floor<0){
            throw new InvalidDataException("Floor "+floor + " doesn't exist: It is below 0");
        }
        defaultFloor=floor;
    }
    
    /**Gets the default floor
     *@return the default floor
     */
    public int getDefault(){
        return defaultFloor;
    }
    /**
     * Sets the identification of the elevator
     * @param id this is the id
     * @throws InvalidDataException when the id is negative
     */
    public void setId(int id) throws InvalidDataException{
        if(id<0){
            throw new InvalidDataException("id cannot be below 0");
        }
        identification=id;
    }
    
    /**Returns the elevator id
     * @return identification number
     */
    public int getId(){
        return identification;
    }
     
    /**gets the amount of time needed to travel each floor
     * @return the travelPerFloor
     */
    private int getTravelPerFloor() {
        return travelPerFloor;
    }

    /**Gets the door operation time
     * @return the doorOperationTime
     */
    private int getDoorOperationTime() {
        return doorOperationTime;
    }

    /**Gets the Idle Time
     * @return the idleTime
     */
    private int getIdleTime() {
        return idleTime;
    }
    
    
    /**
     * Sets the maximum floor
     * @param floor this is the highest floor in the building
     * @throws InvalidDataException when the floor is 0 or below
     */
    public void setMaxFloor(int floor) throws InvalidDataException{
        if(floor<=0){
            throw new InvalidDataException("Max floor "+floor + " doesn't exist: It is below 0");
        }
        maxFloor=floor;
    }
   
    /**Returns the max floor
     * @return the highest floor in the building 
     */
    public int getMaxFloor(){
        return maxFloor;
    }
    
    /**
     * Sets the state of the running boolean
     * @param state true or false depending on whether the elevator is still operational
     */
    public void setRunning(boolean state){
        running=state;
    }
    
    /**Returns if the elevator is still running
     * @return whether or not the elevator is operational
     */
    public boolean getRunning(){
        return running;
    }
    /**
     * Sets the idle state
     * @param state true or false depending on if the elevator is idle
     */
    public void setIdle(boolean state){
        isIdle=state;
    }
    
    /**Returns whether or not the elevator is idle
     * @return idle state of the elevator 
     */
    public boolean getIdle(){
        return isIdle;
    }
    /**
     * Sets shutdown protocol to active or inactive
     * @param state true or false depending on if shutdown protocol has started
     */
    private void setShutdown(boolean state){
        shutdown=state;
    }
    
    /**Gets the arraylist for passengers
     * @return the passengers
     */
    private ArrayList<Person> getPassengers() {
        return passengers;
    }
    
    /**Returns whether or not Shutdown protocol is active
     * @return whether or not Shutdown protocol is active or inactive
     */
    private boolean getShutdown(){
        return shutdown;
    }
    
    /**Gets the max amount of people who can be in one elevator
     * @return the maxPassengerCount
     */
    public int getMaxPassengerCount() {
        return maxPassengerCount;
    }

    /** Sets the max amount of people that can be in one elevator
     * @param maxPassengerCount the maxPassengerCount to set
     */
    public void setMaxPassengerCount(int maxPassengerCount) throws InvalidDataException {
        if(maxPassengerCount<=0){
            throw new InvalidDataException("Max passenger count cannot be below 0 or equal to 0");
        }
        this.maxPassengerCount = maxPassengerCount;
    }

    /**Sets the time it takes to travel each floor
     * @param travelT the travelPerFloor to set
     */
    public void setTravelPerFloor(int travelT) throws InvalidDataException {
        if(travelT<=0){
            throw new InvalidDataException("Travel Time can not be below or equal to 0");
        }
        this.travelPerFloor = travelT;
    }

    /** Sets the time it takes for the door to open and close
     * @param doorOperationTime the DOOR_OPERATION_TIME to set
     */
    public void setDoorOperationTime(int doorOperationTime) throws InvalidDataException {
        if(doorOperationTime<=0){
            throw new InvalidDataException("The door operation time can not be below or equal to 0");
        }
        this.doorOperationTime = doorOperationTime;
    }
    /**
     * Sets the boolean that will tell whether or not the elevator is changing direction
     * @param state true or false depending if the elevator is adding pending destinations
     */
    private void setChanging(boolean state){
        changingDirection=state;
    }
    
    /**Returns whether or not elevator is changing directions
     * @return whether or not elevator is changing directions
     */
    public boolean getChanging(){
        return changingDirection;
    }
    /**
     * allows for remote shutdown of the elevator by changing the state of running.
     */
    @Override
    public void shutdown(){
        setShutdown(true);
        System.out.println(TimerSingleton.getInstance().getTimeString()+": Elevator "+ getId()+ " is shutdown");
    }
    
    /**
     * Opens and closes the elevator following proper timing of the door being open 
     * @throws InterruptedException when sleep is interrupted
     * @throws InvalidDataException  
     */
    public void doArrive() throws InterruptedException, InvalidDataException{
        System.out.println(TimerSingleton.getInstance().getTimeString() +": Elevator "+getId()+ ": Doors Open at floor "+ getLocation());
        synchronized(getDestinationList()){
            getDestinationList().remove(0);
        }
        ArrayList<Integer> found= new ArrayList<>();
        synchronized(getPassengers()){
            for (int i=0;i<getPassengers().size();i++) {
                Person x = getPassengers().get(i);
                if(x.getDestination()==getLocation()){
                    found.add(i);
                    System.out.println(TimerSingleton.getInstance().getTimeString()+": Person from Floor "+x.getLocation()+" leaves on floor "+ getLocation());
                }

            }
            Collections.sort(found);
            Collections.reverse(found);
            for (int i=0;i<found.size();i++) {
                
                int target=found.get(i);
                getPassengers().get(target).arrived();
                getPassengers().remove(target);
            }

            for(int i=0;i<FloorFacade.getInstance().getPopulation(location);i++){
                if(getPassengers().size()<getMaxPassengerCount()){
                    if(FloorFacade.getInstance().canCome(i,getLocation(),goingUp(),getDestinationList().isEmpty())){
                        Person target=FloorFacade.getInstance().getPersonFromFloor(getLocation(),i);
                        target.pickedUp();
                        getPassengers().add(target);
                        System.out.println(TimerSingleton.getInstance().getTimeString()+": Person from Floor "+target.getLocation()+" enters elevator "+ getId()+" to destination "+ target.getDestination());
                    }
                }
            }
        }
        if(getDestinationList().isEmpty()){
            setChanging(true);
            ElevatorController.getInstance().addPendingDest(getId());
        }
        if(getDestinationList().isEmpty()){
            findDestination();
        }
        setChanging(false);
        Thread.sleep(getDoorOperationTime()/TimerSingleton.getInstance().getRatio());
        System.out.println(TimerSingleton.getInstance().getTimeString()+": Elevator "+getId()+ ": Doors close at floor "+ getLocation());
    }
    
    /**
     * adds Destination to list going through the proper checks
     * @param destination floor that is being requested to go to
     * @see #destinationList
     * @throws InvalidDataException 
     * 1:destination is above maxFloor
     * 2:destination is below 0
     */
    @Override
    public void addDest(int destination) throws InvalidDataException {
        boolean travelingUp= goingUp();
        synchronized(this){
            if(destination>getMaxFloor()){
                throw new InvalidDataException("Floor "+destination + " doesn't exist: It is above "+ getMaxFloor());
            }
            if(destination<0){
                throw new InvalidDataException("Floor "+destination + " doesn't exist: It is below 0");
            }
            if((!getChanging() && travelingUp && destination<getLocation())||(!getChanging() && !travelingUp && destination>getLocation()) ){
                return;
            }
            if(getDestinationList().contains((Integer)destination)){
                return;
            }
            getDestinationList().add(destination);
            Collections.sort(getDestinationList());
            if(!travelingUp){
                Collections.reverse(getDestinationList());
            }
            if((getIdle() && getNextDest()!=getDefault())){
                setIdle(false);
            }
            this.notifyAll();
            System.out.println(TimerSingleton.getInstance().getTimeString()+": DESTINATION "+ destination +" IS ADDED TO ELEVATOR "+ getId()+"/"+destinationList.toString() );
        }
    }    
    /**
     * Run the thread until running is set to false
     * @see #running
     * @see #t
     * @see java.lang.Thread
     */
    @Override
    public void run() {
        long defaultTime=0;
        System.out.println(TimerSingleton.getInstance().getTimeString()+": Elevator "+getId()+" thread has started");
        while(getRunning()){
            synchronized(this){
                if(getRunning() && getDestinationList().isEmpty()){
                    defaultTime=System.currentTimeMillis()+(getIdleTime()/TimerSingleton.getInstance().getRatio());
                    try{
                        this.wait(getIdleTime()/TimerSingleton.getInstance().getRatio());
                    }
                    catch (InterruptedException ex){
                        setIdle(false);
                    }
                }
            }
            synchronized(this){
                if(getRunning() && !getIdle() && getDestinationList().isEmpty()&& defaultTime==System.currentTimeMillis()){
                    try {   
                        setChanging(true);
                        ElevatorController.getInstance().addPendingDest(getId());
                        if(getDestinationList().isEmpty()){
                            findDestination();
                        }
                        setChanging(false);
                        if(getDestinationList().isEmpty()){
                            addDest(getDefault());
                            setIdle(true);
                        }
                    } catch (InvalidDataException ex) {}
                    
                    if(getLocation()==getDefault()) {
                        System.out.println(TimerSingleton.getInstance().getTimeString()+": Elevator "+getId() +" is Idle");
                    }
                    else{
                        System.out.println(TimerSingleton.getInstance().getTimeString()+": Elevator "+getId() +" is going to default Floor");
                    }
                }
                else if(getRunning() &&!getDestinationList().isEmpty())
                {
                    long sleepLeft=System.currentTimeMillis()+(getTravelPerFloor()/TimerSingleton.getInstance().getRatio());
                    while(System.currentTimeMillis()<sleepLeft){
                        try{
                            Thread.sleep(getTravelPerFloor()/TimerSingleton.getInstance().getRatio());
                        }
                        catch(InterruptedException ex){
                            /*?*/
                        }
                    }
                    if(!goingUp()&&getLocation()!=getNextDest()){
                        int prev=getLocation()-1;
                        try {
                            setLocation(prev);
                        } catch (InvalidDataException ex) {}
                    }
                    else if(getLocation()!=getNextDest()){
                        int prev=getLocation()+1;
                        try {
                            setLocation(prev);
                        } catch (InvalidDataException ex) {}
                    }
                    if(getLocation()==getNextDest()){
                        System.out.println(TimerSingleton.getInstance().getTimeString()+": Elevator "+ getId()+" is now at floor "+ getLocation() + " /"+destinationList.toString());
                    }
                    else{
                        System.out.println(TimerSingleton.getInstance().getTimeString()+": Elevator "+getId()+ " is passing floor " + getLocation () +" /"+ destinationList.toString());
                    }
                }
                if( (getRunning() && !getDestinationList().isEmpty() && getLocation()==getNextDest()) || stopAtDest() || FloorFacade.getInstance().isSomeoneOnFloor(location,goingUp())){
                    try {
                            if(!getDestinationList().contains(getLocation())){
                                addDest(getLocation());
                            }
                            doArrive();
                    } catch (InterruptedException ex) {}
                    catch (InvalidDataException ex){}
                }
                if(getShutdown()==true && getLocation()==getDefault() && FloorFacade.getInstance().floorsEmpty() && getPassengers().isEmpty()){
                    setRunning(false);
                    System.out.println("Elevator "+ getId()+" : HAS STOPPED RUNNING"  );
                }
            }
        }
    }    
    
    /**
     * This creates a string representation of all the passengers
     * @return String of all passengers
     */
    public String toString(){
        synchronized(getPassengers()){
            return getPassengers().toString();
        }
    }
    /**
     * This looks through passengers list and adds passengers dest floor to elevator destinations
     * @throws InvalidDataException If the floor added is above max floor or below 0
     */
    private void findDestination() throws InvalidDataException{
        synchronized(getPassengers()){
          for(Person x: getPassengers()){
              if(x.getDestination()>getLocation()){
                  addDest(x.getDestination());
              }
          }
          if(getDestinationList().isEmpty()){
            for(Person x: getPassengers()){
                if(x.getDestination()<getLocation()){
                    addDest(x.getDestination());
                }
            }
          }
        }
    }
    /**
     * Checks to see if any passengers within passengerList has a destination on this floor
     * @return true if a passenger destination is on elevators current location
     */
    private boolean stopAtDest() {
        Person target=null;
        synchronized(getPassengers()){
            for(int i=0;i<getPassengers().size();i++){
                if(getPassengers().get(i).getDestination()==getLocation()) {
                    target=getPassengers().get(i);
                }
            }
        }
        if(target!=null) {
            return true;
        }
        return false;
    }
    /**
     * This checks the destination list to see if the location is already within the list
     * @param location the floor to check if exist in list
     * @return true if destination list has location within it
     */
    @Override
    public boolean checkDest(int location) {
        return (getDestinationList().contains(location));
    }


}