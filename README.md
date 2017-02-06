# Operating-Systems-Project-2
Elevator Simulation
In this project threads are used to simulate people using an elevator to reach their floor.
This project is similar to the “barbershop” example in the textbook. The threads to be used are as follows:

Person:
1) 49 people are in line at the elevator at the beginning of the simulation (1 thread per person).
2) Each person begins at floor 1.
3) Each person randomly picks a floor from 2 to 10.
4) A person will wait for an elevator to arrive at floor 1.
5) A person will board the elevator only if there is room.
6) Once at the destination floor, the person exits the elevator.

Elevator:
1) There is 1 elevator (1 thread for the elevator).
2) The elevator can only hold 7 people.
3) The elevator begins on floor 1.
4) The elevator leaves after the 7th person enters.

Main
1) Creates all threads and joins all person threads.
2) When last person reaches their floor, the simulation ends.

Other rules:
1) Each activity of each thread should be printed with identification (e.g., person 1).
2) A thread may not use sleeping as a means of coordinating with other threads.
3) Busy waiting (polling) is not allowed.
4) Mutual exclusion should be kept to a minimum to allow the most concurrency.
5) The semaphore value may not obtained and used as a basis for program logic.
6) All activities of a thread should only be output by that thread.

Sample output:
Your project’s output should match the wording of the sample output:
Elevator door opens at floor 1
Person 0 enters elevator to go to floor 5
Person 1 enters elevator to go to floor 2
Person 2 enters elevator to go to floor 8
Person 3 enters elevator to go to floor 4
Person 4 enters elevator to go to floor 6
Person 5 enters elevator to go to floor 7
Person 6 enters elevator to go to floor 2
Elevator door closes
Elevator door opens at floor 2
Person 1 leaves elevator
Person 6 leaves elevator
Elevator door closes
Elevator door opens at floor 4
Person 3 leaves elevator
Elevator door closes
Elevator door opens at floor 5
Person 0 leaves elevator
Elevator door closes
Elevator door opens at floor 6
Person 4 leaves elevator
Elevator door closes
Elevator door opens at floor 7
Person 5 leaves elevator
Elevator door closes
Elevator door opens at floor 8
Person 2 leaves elevator
Elevator door closes
Elevator door opens at floor 1
…
Simulation done
