package com.airport.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

public class Airport {
	
	private List<Flight> flights;
	private List<Gate> gates;
	private Queue<Flight> boardingQueue;
	private Deque<String> actionHistory;  //being used as a stack

	public Airport() {
		flights = new ArrayList<>();
		gates = new ArrayList<>();
		boardingQueue = new ArrayDeque<>();
		actionHistory = new ArrayDeque<>();
	}
	
	public Airport(List<Flight> flights, List<Gate> gates, Queue<Flight> boardingQueue, Deque<String> actionHistory) {
		super();
		this.flights = flights;
		this.gates = gates;
		this.boardingQueue = boardingQueue;
		this.actionHistory = actionHistory;
	}

	//helper methods for adding and removing items
	public void addFlight(Flight flight) {
		flights.add(flight);
	}
	
	public void removeFlight(Flight flight) {
		flights.remove(flight);
	}
	
	
	public void addGate(Gate gate) {
		gates.add(gate);
	}
	
	public void removeGate(Gate gate) {
		gates.remove(gate);
	}
	
	public List<Flight> getFlights() {
		return flights;
	}
	
	public Deque<String> getActionHistory() {
	    return actionHistory;
	}
	
	//airport logic methods are implemented from here onwards
	public void assignGateToFlight(Flight flight) {
		boolean assigned = false;   //using a boolean variable to keep track of assigned status
		
		for (Gate g: gates) {
			if (g.isAvailable() == true) {
				flight.setGateNumber(g.getGateNumber());
				g.setAvailable(false);
				actionHistory.push("Assigned gate " + g.getGateNumber() + " to flight " + flight.getFlightNumber());
				assigned = true;
				break;   //stops loop once a gate is assigned
				
			}
		}
		
		if (!assigned) {
			System.out.println("There are no available gates");
		}
		
	}
	
	
	public void addToBoardingQueue(Flight flight) {
		if (flight.getGateNumber() != -1) {
			boardingQueue.add(flight);
			actionHistory.push("Added flight " + flight.getFlightNumber() + " to boarding queue");
			System.out.println("Successfully added flight " + flight.getFlightNumber() + " to the boarding queue");
		} else {
			System.out.println("ERROR: Flight has not been assigned a gate");
		}
	}
	
	
	public void boardNextFlight() {
		if (!boardingQueue.isEmpty()) {
			Flight first = boardingQueue.remove();
			int firstGate = first.getGateNumber();
			
			first.setStatus(FlightStatus.BOARDED);
			
			for (Gate g: gates) {
				if (g.getGateNumber() == firstGate) {
					g.setAvailable(true);
				}
			}
			
			first.setGateNumber(-1);
			
			actionHistory.push("Flight " + first.getFlightNumber() + " boarded from gate " + firstGate);
			
			System.out.println("Flight " + first.getFlightNumber() + " boarded from gate " + firstGate);
			
		} else {
			System.out.println("No flights in boarding queue");
		}
	}
	
	
	public void viewBoardingQueue() {
		if (!boardingQueue.isEmpty()) {
			System.out.println("Flights waiting to board:");
			int serialCount = 1;
			
			for (Flight f: boardingQueue) {
				System.out.println(serialCount + ". " + f.getFlightNumber());
				serialCount++;
			}
			
		} else {
			System.out.println("Boarding queue is empty");
		}
	}
	
	
	public void viewActionHistory() {
		if (!actionHistory.isEmpty()) {
	        System.out.println("Current Action History (most recent first):");
	        int count = 1;
	        for (String action : getActionHistory()) {  //prints most recent action first as per LIFO
	            System.out.println(count + ". " + action);
	            count++;
	        }
		} else {
			System.out.println("No actions recorded");
		}
	}
	
	
	public void undoLastAction() {
		if (!actionHistory.isEmpty()) {
			//storing the undone action in a separate String variable using the peek() 
            //method so as to not accidentally delete the recently completed action
			String action = actionHistory.peek();  
			
			//now using to pop to delete the most recent action
			actionHistory.pop();
			
			//confirming the requested action has been undone
			System.out.println("Undid action: " + action);
		} else {
			System.out.println("No actions to undo");
		}
	}
	
	
	public void searchFlightByNumber(String flightNumber) {

		for (Flight f: flights) {
			if (f.getFlightNumber().equalsIgnoreCase(flightNumber)) {
				System.out.println("Flight Found:");
				System.out.println("Flight Number: " + f.getFlightNumber());
				System.out.println("From: " + f.getOrigin());
				System.out.println("To: " + f.getDestination());
				System.out.println("Departure Time: " + f.getDepartureTime());
				System.out.println("Status: " + f.getStatus());

				if (f.getStatus() == FlightStatus.BOARDED) {
					System.out.println("Gate Number: Flight has been boarded so gate is free");
				} else {
					System.out.println("Gate Number: " + f.getGateNumber());				
				}
				return;
			}
		}
		
		System.out.println("Flight " + flightNumber + " not found.");
	}
	
	
	public void searchFlightsByStatus(FlightStatus status) {
		boolean found = false;
		
		for (Flight f: flights) {
			if (f.getStatus() == status) {
				System.out.println(
		                f.getFlightNumber() + " | " +
		                f.getOrigin() + " -> " +
		                f.getDestination() + " | " +
		                f.getDepartureTime()
		            );
				found = true;
			}
		}
		
		if (!found) {
//			System.out.println("No flights found with status: " + status);
			System.out.println("No flights with this status");
		}
		
	}
	
	
	public void sortFlightsByDepartureTime() {
		Collections.sort(flights, (f1, f2) -> {
			LocalTime t1 = LocalTime.parse(f1.getDepartureTime(), DateTimeFormatter.ofPattern("HH:mm"));
		    LocalTime t2 = LocalTime.parse(f2.getDepartureTime(), DateTimeFormatter.ofPattern("HH:mm"));
		    return t1.compareTo(t2);
		});
	}
	
	
	public void sortFlightsByStatus() {
		Collections.sort(flights, (f1, f2) -> f1.getStatus().ordinal() - f2.getStatus().ordinal());
	}
	
	
	public void updateFlightStatus(String flightNumber, FlightStatus newStatus) {
		
		for (Flight f: flights) {
			if (f.getFlightNumber().equalsIgnoreCase(flightNumber)) {
				f.setStatus(newStatus);
				
				actionHistory.push("Updated flight " + flightNumber + " status to " + newStatus);
				System.out.println("CONFIRMATION: Updated flight " + flightNumber + " status to " + newStatus);
				return;
			}
		}
		
		System.out.println("ERROR: Flight could not be found"); 
	}
	
	
	public void removeFlightByNumber(String flightNumber) {
		
		for (Flight f: flights) {
			if (f.getFlightNumber().equalsIgnoreCase(flightNumber)) {

				f.setGateNumber(-1);
				removeFlight(f);
				boardingQueue.remove(f);
				
				actionHistory.push("Flight " + flightNumber + " has been deleted from the system");
				System.out.println("CONFIRMATION: Flight " + flightNumber + " has been deleted from the system");
				return;
			}
		}
		
		System.out.println("ERROR: Flight could not be found");
	}
	

	public void generateSystemReport() {
		System.out.println("==== Airport System Report ====");

		System.out.println("Total flights in system: " + flights.size());
		System.out.println();

		System.out.println("--- DELAYED FLIGHTS ---");
		searchFlightsByStatus(FlightStatus.DELAYED);
		System.out.println();

		System.out.println("--- CANCELLED FLIGHTS ---");	
		searchFlightsByStatus(FlightStatus.CANCELLED);
		System.out.println();

		System.out.println("--- SCHEDULED FLIGHTS ---");
		searchFlightsByStatus(FlightStatus.SCHEDULED);
		System.out.println();

		System.out.println("--- BOARDING FLIGHTS ---");
		searchFlightsByStatus(FlightStatus.BOARDING);
		System.out.println();

		System.out.println("--- BOARDED FLIGHTS ---");
		searchFlightsByStatus(FlightStatus.BOARDED);
		System.out.println();
		
		System.out.println("Current Boarding Queue:");
		viewBoardingQueue();
		System.out.println();
		
		System.out.println("Recent Actions:");
		viewActionHistory();
	}
	


}
