/**
 * Created by Alan Padilla Chua on 10/12/2016.
 *
 */
public class Project2 {
    private final static int maxPeople = 49; // needed to create the final thread
    public static void main(String args[]){
        Person.Elevator mainElevator = new Person.Elevator(); // create the static elevator
        Thread elevatorThread = new Thread(mainElevator);// create teh thread for the elevator
        Thread[] personThread = new Thread[maxPeople]; // create array to hold person threads
        elevatorThread.start(); // start the elevator thread

        for(int i = 0; i < personThread.length; i++){
            personThread[i] = new Thread(new Person()); // initialize person threads
            personThread[i].start(); // start threads
        }
        for(int i = 0; i < personThread.length; i++){
            try{
                personThread[i].join();  // join person threads
            }catch (InterruptedException e){
                e.printStackTrace(); //
            }
        }
    }
}