package com.airport.model;

import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Airport {
	
	private List<Flight> flights;
	private List<Gate> gates;
	private Queue<Flight> boardingQueue;
	private Queue<Flight> arrivingQueue;
	private Deque<String> actionHistory;  //being used as a stack

	public Airport() {
		flights = new ArrayList<>();
		gates = new ArrayList<>();
		boardingQueue = new ArrayDeque<>();
		arrivingQueue = new ArrayDeque<>();
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
	
	public void addAllFlights(List<Flight> flights) {
		if (flights == null) return;
		this.flights.addAll(flights);
	}
	
	public void removeFlight(Flight flight) {
		flights.remove(flight);
	}
	
	public void clearFlights() {
		flights.clear();
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
	
	public List<Gate> getGates() {
		return gates;
	}
	
	public Queue<Flight> getBoardingQueue() {
		return boardingQueue;
	}
	
	public Queue<Flight> getArrivingQueue() {
		return arrivingQueue;
	}
	
	public Deque<String> getActionHistory() {
	    return actionHistory;
	}
	
	public void clearBoardingQueue() {
		boardingQueue.clear();
	}
	
	public void clearArrivingQueue() {
		arrivingQueue.clear();
	}
	
	public void clearActionHistory() {
		actionHistory.clear();
	}
	
	public void freeAllGates() {
		
		for (Gate g: gates) {
			g.setAvailable(true);
		}
	}
	
	//airport logic methods are implemented from here onwards
	public String assignGateToFlight(Flight flight) {
		boolean assigned = false;   //using a boolean variable to keep track of assigned status
		String output = "";
		
		for (Gate g: gates) {
			if (g.isAvailable() == true) {
				flight.setGateNumber(g.getGateNumber());
				g.setAvailable(false);
				output = "Gate " + g.getGateNumber() + " successfully assigned to flight " + flight.getFlightNumber() + "\n";
				actionHistory.push("Assigned gate " + g.getGateNumber() + " to flight " + flight.getFlightNumber());
				assigned = true;
				break;   //stops loop once a gate is assigned
			}
		}
		
		if (!assigned) {
			output = "There are no available gates";
		}
		
		return output;
		
	}
	
	
	public String addToBoardingQueue(Flight flight) {
		String output = "";
		
		if (flight.getGateNumber() != -1) {
			boardingQueue.add(flight);
			output = "Successfully added flight " + flight.getFlightNumber() + " to the boarding queue";
			actionHistory.push("Added flight " + flight.getFlightNumber() + " to boarding queue");
		
			return output;
			
		} else {
			output = "ERROR: Flight has not been assigned a gate";
		}
		
		return output;
	}
	
	
	public String addToArrivingQueue(Flight flight) {
		String output = "";
		
		if (flight.getGateNumber() != -1) {
			arrivingQueue.add(flight);
			output = "Successfully added flight " + flight.getFlightNumber() + " to the arriving queue";
			actionHistory.push("Added flight " + flight.getFlightNumber() + " to arriving queue");
		
			return output;
			
		} else {
			output = "ERROR: Flight has not been assigned a gate";
		}
		
		return output;
	}
	
	
	public String processNextFlight(Queue<Flight> inputQueue) {
		String s1 = "";
		String s2 = "";
		FlightStatus status = null;
		
		if (inputQueue == boardingQueue) {
			s1 = "boarded from";
			s2 = "Boarding";
			status = FlightStatus.BOARDED;
		} else if (inputQueue == arrivingQueue) {
			s1 = "arrived in";
			s2 = "Arriving";
			status = FlightStatus.ARRIVED;
		}
		
		StringBuilder sb = new StringBuilder();
		
		if (!inputQueue.isEmpty()) {
			Flight first = inputQueue.remove();
			int firstGate = first.getGateNumber();
			
			first.setStatus(status);
			
			for (Gate g: gates) {
				if (g.getGateNumber() == firstGate) {
					g.setAvailable(true);
				}
			}
			
			first.setGateNumber(-1);
			
			sb.append("Flight " + first.getFlightNumber() + " " + s1 + " gate " + firstGate);
			
			actionHistory.push("Flight " + first.getFlightNumber() + " " + s1 + " gate " + firstGate);
			
			return sb.toString();
			
		} else {
			sb.append("No flights in " + s2 + " queue");
		}
		
		return sb.toString();
		
	}

	
	
	public String viewQueue(Queue<Flight> inputQueue) {
		String s1 = "";
		String s2 = "";
		
		if (inputQueue == boardingQueue) {
			s1 = "board";
			s2 = "Boarding";
		} else if (inputQueue == arrivingQueue) {
			s1 = "arrive";
			s2 = "Arriving";
		}
		
		StringBuilder sb = new StringBuilder();
		
		if (!inputQueue.isEmpty()) {
			sb.append("Flights waiting to " + s1 + ":\n");
			int serialCount = 1;
			
			for (Flight f: inputQueue) {
				sb.append(serialCount + ". " + f.getFlightNumber() + "\n");
				serialCount++;
			}
			return sb.toString();
			
		} else {
			sb.append(s2 + " queue is empty");
		}
		return sb.toString();
		
	}
	
	
	public String viewActionHistory() {
		String output = "";
		if (!actionHistory.isEmpty()) {
	        output = "Current Action History (most recent first):\n";
	        int count = 1;
	        for (String action : getActionHistory()) {  //prints most recent action first as per LIFO
	            output += count + ". " + action + "\n";
	            count++;
	        }
	        return output;
		} else {
			output = "No actions recorded";
		}
		return output;
	}
	
	
	public String undoLastAction() {
		String output = "";
		
		if (!actionHistory.isEmpty()) {
			//storing the undone action in a separate String variable using the peek() 
            //method so as to not accidentally delete the recently completed action
			String action = actionHistory.peek();  
			
			//now using to pop to delete the most recent action
			actionHistory.pop();
			
			//confirming the requested action has been undone
			output = "Undid action: " + action;
			
		} else {
			output = "No actions to undo";
		}
		
		return output;
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
	
	
	public String searchFlightsByStatus(FlightStatus status) {
		String output = "";
		boolean found = false;
		
		for (Flight f: flights) {
			if (f.getStatus() == status) {
				output += f.getFlightNumber() + " | " +
						 f.getOrigin() + " -> " +
						 f.getDestination() + " | " +
						 f.getDepartureTime() + "\n";
				found = true;
			}
		}
		
		if (!found) {
			output = "No flights with this status";
		}
		
		return output;
		
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
	
	
	public String updateFlightStatus(String flightNumber, FlightStatus newStatus) {
		for (Flight f: flights) {
			if (f.getFlightNumber().equalsIgnoreCase(flightNumber)) {
				f.setStatus(newStatus);
				
				String output = "CONFIRMATION: Updated flight " + flightNumber + " status to " + newStatus;
				actionHistory.push("Updated flight " + flightNumber + " status to " + newStatus);
				
				return output;
			}
		}
		
		return "ERROR: Flight could not be found"; 
	}
	
	
	public String removeFlightByNumber(String flightNumber, Queue<Flight> inputQueue) {
		
		for (Flight f: flights) {
			if (f.getFlightNumber().equalsIgnoreCase(flightNumber)) {

				f.setGateNumber(-1);
				removeFlight(f);
				inputQueue.remove(f);
				
				String output = "CONFIRMATION: Flight " + flightNumber + " has been removed from the system";
				actionHistory.push("Removed flight " + flightNumber + " from the system");
				
				return output;
			}
		}
		
		return "ERROR: Flight could not be found";
		
	}
	

	public String generateSystemReport() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("==== Airport System Report ====\n");

		sb.append("Total flights in system: " + flights.size() + "\n");

		sb.append("\n\n--- DELAYED FLIGHTS ---\n");
		sb.append(searchFlightsByStatus(FlightStatus.DELAYED));

		sb.append("\n\n--- CANCELLED FLIGHTS ---\n");	
		sb.append(searchFlightsByStatus(FlightStatus.CANCELLED));

		sb.append("\n\n--- SCHEDULED FLIGHTS ---\n");
		sb.append(searchFlightsByStatus(FlightStatus.SCHEDULED));

		sb.append("\n--- BOARDING FLIGHTS ---\n");
		sb.append(searchFlightsByStatus(FlightStatus.BOARDING));

		sb.append("\n\n--- BOARDED FLIGHTS ---\n");
		sb.append(searchFlightsByStatus(FlightStatus.BOARDED));
		
		sb.append("\n\n--- ARRIVING FLGIHTS ---\n");
		sb.append(searchFlightsByStatus(FlightStatus.ARRIVING));
		
		sb.append("\n--- ARRIVED FLGIHTS ---\n");
		sb.append(searchFlightsByStatus(FlightStatus.ARRIVED));
		
		sb.append("\n\nCurrent Boarding Queue:\n");
		sb.append(viewQueue(boardingQueue));
		
		sb.append("\n\nCurrent Arriving Queue:\n");
		sb.append(viewQueue(arrivingQueue));
		
		sb.append("\nRecent Actions:\n");
		sb.append(viewActionHistory());
		
		
		return sb.toString();
		
	}
	
	
	//method to read CSV files
	public List<Flight> readFlightsFromCSV(String resourcePath) {
	    List<Flight> flights = new ArrayList<>();

	    try (InputStream is = getClass().getResourceAsStream(resourcePath);
	         Scanner sc = new Scanner(is)) {

	        if (is == null) {
	            throw new RuntimeException("CSV not found: " + resourcePath);
	        }

	        // skip header
	        if (sc.hasNextLine()) sc.nextLine();

	        while (sc.hasNextLine()) {
	            String line = sc.nextLine();
	            String[] tokens = line.split(",");

	            String flightNumber = tokens[0].trim();
	            String airline = tokens[1].trim();
	            String origin = tokens[2].trim();
	            String destination = tokens[3].trim();
	            String time = tokens[4].trim();
	            FlightStatus status = FlightStatus.valueOf(tokens[5].trim());

	            flights.add(
	                new Flight(flightNumber, airline, origin, destination, time, status)
	            );
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return flights;
	}


}
