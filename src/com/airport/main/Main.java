package com.airport.main;

import com.airport.model.Airport;
import com.airport.model.Flight;
import com.airport.model.Gate;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Airport airport = new Airport();

        // Add gates
        airport.addGate(new Gate(1, true));
        airport.addGate(new Gate(2, true));

        // Add flight
        Flight f1 = new Flight();
        f1.setFlightNumber("AI101");
        f1.setOrigin("DXB");
        f1.setDestination("LHR");

        airport.addFlight(f1);
        airport.assignGateToFlight(f1);

        // Print flight info
        if (f1.getGateNumber() != -1) {
        	System.out.println("Flight " + f1.getFlightNumber() + " assigned to gate " + f1.getGateNumber());
        } else {
        	System.out.println("Flight " + f1.getFlightNumber() + " has not been assigned to a gate");
        }
        
	}

}
