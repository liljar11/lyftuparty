package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * The base function definitions of this class must stay the same
 * for the test suite and graphics to use.
 * You can add functions and/or change the functionality
 * of the operations at will.
 *
 */

public class ElevatorScene {
	
	public static Semaphore semaphore1;
	
	public static Semaphore personCountMutex;
	
	public static Semaphore elevatorWaitMutex;
	
	public static Semaphore elevatorGoOutMutex;
	
	public static ElevatorScene scene;
	
	public static Semaphore personInElevatorCountMutex;
	
	public static int personInElevatorCount = 0;
	
	public static boolean elevatorsMayDie;

	//TO SPEED THINGS UP WHEN TESTING,
	//feel free to change this.  It will be changed during grading
	public static final int VISUALIZATION_WAIT_TIME = 500;  //milliseconds

	private int numberOfFloors;
	private int numberOfElevators;

	private Thread elevatorThread = null;
	
	ArrayList<Integer> personCount; //use if you want but
									//throw away and
									//implement differently
									//if it suits you

	//Base function: definition must not change
	//Necessary to add your code in this one
	public void restartScene(int numberOfFloors, int numberOfElevators) {
		
		elevatorsMayDie = true;
		
		if(elevatorThread != null){
			if(elevatorThread.isAlive()){
				try {
					elevatorThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		elevatorsMayDie = false;
		
		scene = this;
		semaphore1 = new Semaphore(0);
		personInElevatorCountMutex = new Semaphore(1);
		personCountMutex = new Semaphore(1);
		elevatorWaitMutex = new Semaphore(1);
		elevatorGoOutMutex = new Semaphore(0);
		Thread elevatorThread = new Thread(new Elevator());
		elevatorThread.start();
		
		
			
		
		/**
		 * Important to add code here to make new
		 * threads that run your elevator-runnables
		 * 
		 * Also add any other code that initializes
		 * your system for a new run
		 * 
		 * If you can, tell any currently running
		 * elevator threads to stop
		 */

		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;

		personCount = new ArrayList<Integer>();
		for(int i = 0; i < numberOfFloors; i++) {
			this.personCount.add(0);
		}
	}

	//Base function: definition must not change
	//Necessary to add your code in this one
	public Thread addPerson(int sourceFloor, int destinationFloor) {


		Thread thread = new Thread(new Person(sourceFloor, destinationFloor));
		thread.start();
		
		
		/**
		 * Important to add code here to make a
		 * new thread that runs your person-runnable
		 * 
		 * Also return the Thread object for your person
		 * so that it can be reaped in the testSuite
		 * (you don't have to join() yourself)
		 */

		//dumb code, replace it!
		incrementNumberOfPeopleWaitingAtFloor(sourceFloor);
		return thread;  //this means that the testSuite will not wait for the threads to finish
	}
	//Thread thread = new Thread(Person(sourceFloor, destinationFloor));
	//thread.start();
	//Base function: definition must not change, but add your code
	public int getCurrentFloorForElevator(int elevator) {

		//dumb code, replace it!
		return 1;
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleInElevator(int elevator) {
		
		/*//dumb code, replace it!
		switch(elevator) {
		case 1: return 1;
		case 2: return 4;
		default: return 3;
		}
		*/
		return ElevatorScene.personInElevatorCount;
	}
	public void decrementNumberOfPeopleInElevator(int elevator){
		try {
			ElevatorScene.personInElevatorCountMutex.acquire();
					ElevatorScene.personInElevatorCount -= 1;
					System.out.println("m�nkar f�lk � lyftu");
			ElevatorScene.personInElevatorCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void incrementNumberOfPeopleInElevator(int elevator){
		try {
			System.out.println("fj�lgar f�lk � lyftu");
			ElevatorScene.personInElevatorCountMutex.acquire();
			ElevatorScene.personInElevatorCount += 1;
			ElevatorScene.personInElevatorCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void decrementNumberOfPeopleWaitingAtFloor(int floor){
		try {
			ElevatorScene.personCountMutex.acquire();
				personCount.set(floor,(personCount.get(floor) - 1));
			ElevatorScene.personCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void incrementNumberOfPeopleWaitingAtFloor(int floor){
		try {
			ElevatorScene.personCountMutex.acquire();
				personCount.set(floor,(personCount.get(floor) + 1));
			ElevatorScene.personCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleWaitingAtFloor(int floor) {

		return personCount.get(floor);
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfFloors(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfElevators() {
		return numberOfElevators;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfElevators(int numberOfElevators) {
		this.numberOfElevators = numberOfElevators;
	}

	//Base function: no need to change unless you choose
	//				 not to "open the doors" sometimes
	//				 even though there are people there
	public boolean isElevatorOpen(int elevator) {

		return isButtonPushedAtFloor(getCurrentFloorForElevator(elevator));
	}
	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public boolean isButtonPushedAtFloor(int floor) {

		return (getNumberOfPeopleWaitingAtFloor(floor) > 0);
	}

}