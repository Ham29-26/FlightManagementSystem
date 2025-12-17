package com.airport.main;

import com.airport.model.Airport;
import com.airport.model.Flight;
import com.airport.model.Gate;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Airport airport = new Airport();

        // Add gates
//        airport.addGate(new Gate(10, true));
//        airport.addGate(new Gate(20, true));

        // Add flight
        Flight f10 = new Flight();
        f10.setFlightNumber("AI101");
        f10.setOrigin("DXB");
        f10.setDestination("LHR");

        airport.addFlight(f10);
        airport.assignGateToFlight(f10);

        // Print flight info
        if (f10.getGateNumber() != -1) {
        	System.out.println("Flight " + f10.getFlightNumber() + " assigned to gate " + f10.getGateNumber());
        } else {
        	System.out.println("Flight " + f10.getFlightNumber() + " has not been assigned to a gate");
        }
        
        
        //testing the gate queue 
        System.out.println("\nTesting Gate queue logic.....");
        
     // 1. Add gates
        Gate g1 = new Gate();
        g1.setGateNumber(1);
        g1.setAvailable(true);
        airport.addGate(g1);

        Gate g2 = new Gate();
        g2.setGateNumber(2);
        g2.setAvailable(true);
        airport.addGate(g2);

        // 2. Add flights and assign gates
        Flight f1 = new Flight();
        f1.setFlightNumber("EK123");
        airport.addFlight(f1);
        airport.assignGateToFlight(f1);
        

        Flight f2 = new Flight();
        f2.setFlightNumber("QR456");
        airport.addFlight(f2);
        airport.assignGateToFlight(f2);

        // 3. Add flights to boarding queue
        airport.addToBoardingQueue(f1);
        airport.addToBoardingQueue(f2);

        // 4. View queue
        System.out.println();
        airport.viewBoardingQueue();

        // 5. Board next flight
        System.out.println();
        airport.boardNextFlight();

        // 6. View queue again
        System.out.println();
        airport.viewBoardingQueue();
        
     // 7. Board next flight
        System.out.println();
        airport.boardNextFlight();

        // 8. View queue again
        System.out.println();
        airport.viewBoardingQueue();
        
        
        //printing action history
        System.out.println("\nAction History (most recent first):");
        int count = 1;
        for (String action : airport.getActionHistory()) {  // assuming you add a getter for actionHistory
            System.out.println(count + ". " + action);
            count++;
        }
        
        
	}

}
