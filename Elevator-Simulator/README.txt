SYSC 3303 Lab Group 4 - Iteration 2

Contributors:
  Ahmed Moazzam
  - Created Motor, Door, ElevatorButton and corresponding test classes.
  Adam Arrhaoui
  - Created Elevator State Diagram
  - Help with elevator state
  Rebecca Li
  - SchedulerStateTest and SchedulerState class
  - Create Scheduler State diagram
  Xianqi Wang
  - Helped with SchedulerState class.
  - Debug and add scheduler state to the proper place

Files:
  src/:
  - Main.java, main function where threads are created and ran.
  - input.txt, input file with data.
  
  src/Data:
  - Direction.java, enum class for Direction
  - Event.java, data class for Events
  
  src/States:
  - SchedulerState.java, state machine for scheduler (In Progress)
  - Door.java, state machine for door.
  - Motor.java, state machine for moving speed.
  - ElevatorButton.java, state machine for elevator button
  
  src/Subsystems:  
  - Elevator.java, class for Elevator subsystem
  - Floor.java, class for Floor subsystem
  - Scheduler.java, class for Scheduler subsystem
  - RequestBuffer.java, class for thread-safe buffer used by Scheduler

  test/:
  - TestSuite.java, runs all tests

  test/States:
  - SchedulerStateTest.java, unit testing class for SchedulerState class.
  - DoorTest.java, unit testing class for Door class.
  - MotorTest.java, unit testing class for Motor class.
  - ElevatorButtonTest.java, unit testing class for ElevatorButton class.

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

  

