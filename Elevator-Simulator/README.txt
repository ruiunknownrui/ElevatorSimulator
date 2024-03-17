SYSC 3303 Lab Group 4 - Iteration 3

Contributors:
  Ahmed Moazzam
  - Help with elevator
  Adam Arrhaoui
  - Help with elevator receiving network and object to byte convert
  Rebecca Li
  - update Scheduler, SchedulerState, SchedulerTest, SchedulerStateTest
  - Created Ack, ElevatorInfo, SchedulerReceiveHandler, SchedulerSendHandler, SchedulerUpdateHandler
  - Implemented RPC in Floor
  - Updated README
  Xianqi Wang
  -

Files:
  src/:
  - Main.java, main function where threads are created and ran.  - not used in this iteration
  - input.txt, input file with data.
  
  src/Data:
  - Direction.java, enum class for Direction
  - Event.java, data class for Events
  - Ack.java, data class for acknowledgment message
  - ElevatorInfo.java, data class used for updating current elevator floor to scheduler
  
  src/States:
  - SchedulerState.java, state machine for scheduler (In Progress)
  - Door.java, state machine for door.
  - Motor.java, state machine for moving speed.
  - ElevatorButton.java, state machine for elevator button
  
  src/Subsystems:  
  - Elevator.java, class for Elevator subsystem
  - ElevatorNetworkHandler.java, class for Elevator to send and receive request
  - Floor.java, class for Floor subsystem
  - Scheduler.java, class for Scheduler subsystem
  - SchedulerReceiveHandler.java, class for Scheduler to receive request from floor
  - SchedulerSendHandler.java, class for Scheduler to send request to elevators
  - SchedulerUpdateHandler.java, class for Scheduler to received the elevator's updated location.
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
  After downlading the project and opening in IntelliJ, first run Scheduler, then run Floor and Elevator
  You may get a file not found error, if so open the Floor class and change the file path for the
  input file to the actual path of the file on your machine, you can find this path by right 
  clicking on the input.txt file in the Intellij file explorer then 'Copy Path/Reference'/'Path from Content Root'.

  

