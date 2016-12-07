import java.util.ArrayList;
import java.util.concurrent.*;
/**
 * Created by Alan Padilla Chua on 10/24/2016.
 * Person class
 */
public class Person implements Runnable {
    /**
     * Elevator
     *     -inner static class
     *     - only one instance of Elevator is needed
     *     - all person threads should have references to static inner class
     * */
    public static class Elevator implements Runnable{
        private final static int maxCapacity = 7; // maxCapacity = holds the amount of people that can be in elevator
        private final static int lobbyFloor = 1; // initial floor where elevator start
        private static int exitCount = 0; // exit count that
        private static ArrayList<Person> PersonList = new ArrayList<>(); // arrayList holds the persons inside the elevator
        private static int currentFloor,currentCapacity; // variables to keep track of how many people inside and floor

        public static Semaphore door = new Semaphore(0,true); // door has 7 permits for 7 people to enter into elevator
        public static Semaphore Elevatorfull = new Semaphore(0,true); // signals when the elevator is full


        Elevator(){ //constructor called when thread is created
            PersonList.clear(); // cleans the list
            currentCapacity = 0; // sets the capacity to 0
            setCurrentFloor(lobbyFloor); // starts the elevator to the lobby floor
        }

        public static int getMaxCapacity(){
            return maxCapacity;
        }

       public static int getCurrentCapacity(){
            return currentCapacity;
        }
        public static void setCurrentFloor(int floor){
            currentFloor = floor;
        }

       public static int getCurrentFloor(){
            return currentFloor;
        }

        // detects weather the all person wanting to go to
        //takes in current floor level and checks what the next floor will be
        // if they are equal then it hasnt changed if not then it has
        public boolean lowestChange(int oldLow){
            return (oldLow == gotoFloor());
        }
        // Outputs message that the door has opened with the current floor number
        public static void OpenDoor(){
            System.out.println("Elevator door opens at floor "
                    + getCurrentFloor());
        }
        // outputs that the door is now closed
        public void CloseDoor(){
            System.out.println("Elevator door closes");
        }

        // method outputs the floor that the elevator should visit first
        // returns the lowest floor so it goes up in order
        // reads through arraylist
        public int gotoFloor(){
            int lowest_floor = 11; // lowestfloor is set to something higher than floors available
            if(!PersonList.isEmpty()) {// if list not empty
                for (Person user : PersonList) { // for each loop iterates through array list
                    if (user.getRequestedFloor() < lowest_floor) {//
                        lowest_floor = user.getRequestedFloor();
                    }
                } // once loop is done
                return lowest_floor; // return lowest floor
            }
            else // if list is empty then return to lobby
                return 1; // return 1 go to lobby
        }
        // Process floors is the main driver of the elevator
        // it gets the first floor and signals to every thread
        // that is waiting for the floor
        // when list is empty return to lobby
        //
        public void processFloors(){
            int nextFloor = gotoFloor(); // get the floor
            while(nextFloor != 1) {// while list is not empty
                setCurrentFloor(nextFloor);// go to the door
                System.out.println("Elevator door opens at floor " + getCurrentFloor()); // print that its at a new floor
                if(!PersonList.isEmpty()) { // if the list is not empty
                    boolean cont = true; // cont controls loop
                    while (cont){ //while there are still people wanting that floor
                        for(int i = 0; i < getCurrentCapacity(); i++){ //run a loop for the current length of list
                            if(PersonList.get(i).getRequestedFloor() == getCurrentFloor()) { // if the floor matches their request
                                signalSem(wantedFloor[PersonList.get(i).getId()]);  //signal to user that they are at requested floor
                                waitSem(leftElevator);  // wait until they left
                            }
                            cont = lowestChange(nextFloor); // check if people still want that floor
                        }
                    }
                    CloseDoor(); // close the door
                }
                nextFloor = gotoFloor(); // get the next floor
            }
            PersonList.clear(); // clear the list
            setCurrentFloor(nextFloor); // return to lobby
        }
        // Method called from Person thread
        // used to add persons to arraylist that holds
        // passangers inside the arraylist
        public void enterElevator(Person user){
            PersonList.add(user); // add the user
            currentCapacity++; // increase the capacity count
            System.out.println("\tPerson " + user.getId() // output
                    + " enters elevator to get to floor "
                    + user.getRequestedFloor());
        }
        // Leave elevator is called by person thread
        // deletes user from array list
        // increases exit count
        public void leaveElevator(Person user){ // takes user
            System.out.println("\tPerson " + user.getId()
                    + " leaves elevator"); // prints out that user left the elevator
            PersonList.remove(user); // removes user from list
            exitCount++; // increase exit count
            currentCapacity--; // decreases elevator count
        }
        //signal method that passes argument to semaphore
        private void signalSem(Semaphore sem, int amt){
            sem.release(amt);
        }
        //signal method for binary semaphores
        private void signalSem(Semaphore sem){
            sem.release();
        }
        //wait method that tries to acquire
        private void waitSem(Semaphore sem){
            try {
                sem.acquire();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        // method that ends the simulation
        // when the last person leaves elevator
        private void checkFinal(){
            if(exitCount == 49){
                System.out.println("Simulation Done");
                System.exit(0);
            }
        }
        // run method called when thread is alive
        public void run(){
            prepareWantedSem(); // initializes the semaphore array
            while (true){ // elevator runs until kill signal
                OpenDoor(); // opens the door
                signalSem(door,7);// allow 7 people to enter enter elevator
                signalSem(Person.capacity,7); //allow 7 people to enter
                waitSem(Elevatorfull); // wait for elevator to close
                CloseDoor(); // close the door
                processFloors(); // process floor
                checkFinal(); // check if that was the last person
            }

        }
    }
    /****************************************************************************/
    private int id,requestedFloor; // local variables for each person
    private static int count = 0; //count is static
    private static Elevator mainElevator = new Elevator(); // static referenced to elevator

    private static Semaphore enterLobby = new Semaphore(49,true); // 49 people enter lobby
    private static Semaphore enterMutex = new Semaphore(1,true); // enter semaphore for mutual exclusion
    private static Semaphore capacity = new Semaphore(0,true); // semaphore waits for signal to allow people in elevator
    private static Semaphore wantedFloor[] = new Semaphore[49]; // semaphore array each thread waits for their specific semaphore
    private static Semaphore leftElevator = new Semaphore(0,true);  // semaphore that elevator thread waits for
    private static Semaphore exitMutex = new Semaphore(1,true);// leave semaphore for mutual exclusion
    // method initialise semaphore array
    private static void prepareWantedSem (){
        for(int i = 0; i < wantedFloor.length; i++){
            wantedFloor[i] = new Semaphore(0,true);
        }
    }
    //method signal semaphore that are passed into it
    private void signalSem(Semaphore sem){
        sem.release();
    }
    // wait method tries to acquire semaphore
    private void waitSem(Semaphore sem){
        try {
            sem.acquire();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // method checks if elevator is at max capacity
    // if it is then signal to elevator that its full
    private void checkIfFull(){
        if(Elevator.getCurrentCapacity() == Elevator.getMaxCapacity()){ // if capacity is equal to max capacity
            signalSem(Elevator.Elevatorfull);// signal to elevator that its full
        }
    }
    // person constructor
    Person(){
        id = count; // id is set to count
        count++; // count is increased
        requestedFloor = ThreadLocalRandom.current().nextInt(2,10+1); //random variable chosen for floor
    }
    private int getRequestedFloor(){return requestedFloor;} // return requested Floor

    private int getId() {return id;} // return id

    public void run(){
        waitSem(enterLobby); // 49 people enter lobby
        waitSem(capacity);// 7 people enter Elevator
        waitSem(Elevator.door); // wait for door to open
        waitSem(enterMutex);
            mainElevator.enterElevator(this); // enter with mutual exclusion
        signalSem(enterMutex);
        checkIfFull(); //check if elevator is full to signal the elevator
        waitSem(wantedFloor[this.getId()]); // semaphore waits for its semaphore to check if its ready
        waitSem(exitMutex); //
            mainElevator.leaveElevator(this);// leave elevator
        signalSem(exitMutex); //
        signalSem(leftElevator); // signal to elevator that it left
    }
}
