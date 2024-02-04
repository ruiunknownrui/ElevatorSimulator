SYSC 3303 Lab Group 4 - Iteration 1

Contributors:
  Ahmed Moazzam
  - Setup project structure and created repo.
  - Created Floor class and Floor tests.
  - Helped with Scheduler and Elevator classes.
  Adam Arrhaoui
  - Created unit tests structure.
  - UML and Sequence Diagrams
  Rebecca Li
  - Scheduler class and tests
  - Helped with Elevator class
  Xianqi Wang
  - Helped with Elevator class
  - Debug and documentation of other classes.

Files:
  src/:
  - Main.java, main function where threads are created and ran.
  - input.txt, input file with data.
  
  src/Data:
  - Direction.java, enum class for Direction
  - Event.java, data class for Events
  
  src/States:
  - SchedulerState.java, state machine for scheduler (In Progress)
  
  src/Subsystems:  
  - Elevator.java, class for Elevator subsystem
  - Floor.java, class for Floor subsystem
  - Scheduler.java, class for Scheduler subsystem
  - RequestBuffer.java, class for thread-safe buffer used by Scheduler

  test/:
  - TestSuite.java, runs all tests

  test/Data:
  - EventTest.java, unit testing class for Event class

  test/Subsystems:
  - ElevatorTest.java, unit tests for Elevator class
  - FloorTest.java, unit tests for Floor class
  - SchedulerTests.java, unit tests for Scheduler class

Instructions:
  After downlading the project and opening in IntelliJ, simply run the main function in Main.java.
  You may get a file not found error, if so open the Floor class and change the file path for the
  input file to the actual path of the file on your machine, you can find this path by right 
  clicking on the input.txt file in the Intellij file explorer then 'Copy Path/Reference'/'Path from Content Root'.

  

