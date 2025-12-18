package com.airport.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
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
	

}
