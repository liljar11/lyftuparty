package com.ru.usty.elevator;

public class Elevator implements Runnable {
	int peopleInElevator;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			peopleInElevator = 0;
			peopleInElevator = ElevatorScene.scene.getNumberOfPeopleInElevator(1);
			if(ElevatorScene.elevatorsMayDie){
				return;
			}
			
			for(int i = 0; i < (6 - peopleInElevator); i++){
				
			ElevatorScene.semaphore1.release(); //signal
			System.out.println("býr til pláss í lyfut" +i);
		}
			
			
			System.out.println("buinnn að bíða");
			
			try {
				ElevatorScene.scene.personInElevatorCountMutex.acquire();
				peopleInElevator = ElevatorScene.scene.getNumberOfPeopleInElevator(1);
				if(peopleInElevator == 6){
					ElevatorScene.scene.personInElevatorCountMutex.release();
				}
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				peopleInElevator = ElevatorScene.scene.getNumberOfPeopleInElevator(1);
				System.out.println("er að fylla lyftu "+ peopleInElevator);
				for(int i = 0; i < (6- peopleInElevator); i++){
					System.out.println("fyllir lyftur" +i);
					try {
						ElevatorScene.semaphore1.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //signal
				
			}
			for(int i = 0; i < peopleInElevator; i++){
				System.out.println("hleypir fólki út" +i);

			ElevatorScene.elevatorGoOutMutex.release();
			}
			
		
	}
	
	}
	
}