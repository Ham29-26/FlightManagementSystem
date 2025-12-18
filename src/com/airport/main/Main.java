package com.airport.main;

import com.airport.model.Airport;
import com.airport.model.Flight;
import com.airport.model.FlightStatus;
import com.airport.model.Gate;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Airport airport = new Airport();

        // Add gates
//        airport.addGate(new Gate(10, true));
//        airport.addGate(new Gate(20, true));

        // Add flight
        Flight f10 = new Flight("AI101", "Delhi", "Prague", "22:55");

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
        Flight f1 = new Flight("EK123", "Dubai", "New York", "09:35");
        airport.addFlight(f1);
        airport.assignGateToFlight(f1);
        

        Flight f2 = new Flight("QR456", "Sydney", "Kuala Lampur", "16:10");
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
        for (String action : airport.getActionHistory()) {  //prints most recent action first as per LIFO
            System.out.println(count + ". " + action);
            count++;
        }
        
        //testing undoLastAction method
        System.out.println("\nTesting undoLastAction()");
        System.out.println("Current Action History (most recent first):");
        int count1 = 1;
        for (String action : airport.getActionHistory()) {  //prints most recent action first as per LIFO
            System.out.println(count1 + ". " + action);
            count1++;
        }
        
        System.out.println();
        airport.undoLastAction();
        
        System.out.println("\nNew Action History (most recent action should be removed or popped):");
        int count2 = 1;
        for (String action : airport.getActionHistory()) {  //prints most recent action first as per LIFO
            System.out.println(count2 + ". " + action);
            count2++;
        }
        
        //testing further by undoing another action
        System.out.println();
        airport.undoLastAction();
        
        System.out.println("\nNew Action History (most recent action should be removed or popped):");
        int count3 = 1;
        for (String action : airport.getActionHistory()) {  //prints most recent action first as per LIFO
            System.out.println(count3 + ". " + action);
            count3++;
        }
        
        System.out.println("\nTesting status of flight " + f1.getFlightNumber() + ": " + f1.getStatus());
        System.out.println("Testing status of flight " + f2.getFlightNumber() + ": " + f2.getStatus());
        
        
        //testing new searchFlightByNumber method
        System.out.println("\nTesting flight search:");
        airport.searchFlightByNumber("EK123");
        System.out.println();
        airport.searchFlightByNumber("QR999"); // should not exist
        
        
        //testing new searchFlightByStatus method
        System.out.println("\nFlights with status SCHEDULED:");
        airport.searchFlightsByStatus(FlightStatus.SCHEDULED); 

        System.out.println("\nFlights with status BOARDED:");
        airport.searchFlightsByStatus(FlightStatus.BOARDED); 
        
        System.out.println("\nFlights with status DELAYED:");
        airport.searchFlightsByStatus(FlightStatus.DELAYED); //should not exist

	}

}
