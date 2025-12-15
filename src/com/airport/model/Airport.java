package com.airport.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

public class Airport {
	
	private List<Flight> flights = new ArrayList<>();
	private List<Gate> gates = new ArrayList<>();
	private Queue<Flight> boardingQueue = new ArrayDeque<>();
	private Deque<String> actionHistory = new ArrayDeque<>();  //being used as a stack

	
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
	

}
