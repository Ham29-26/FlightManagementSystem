package com.airport.model;

public class Flight {
	
	private String flightNumber;
	private String origin;
	private String destination;
	private String departureTime;
	private FlightStatus status;
	private int gateNumber;
	
	public Flight() {
		status = FlightStatus.SCHEDULED;
		gateNumber = -1;
	}
	
	public Flight(String flightNumber, String origin, String destination, String departureTime, FlightStatus status,
			int gateNumber) {
		super();
		this.flightNumber = flightNumber;
		this.origin = origin;
		this.destination = destination;
		this.departureTime = departureTime;
		this.status = status;
		this.gateNumber = gateNumber;
	}

	//getter and setter methods pre-generated
	public String getFlightNumber() {
		return flightNumber;
	}
	
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	
	
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	
	
	public FlightStatus getStatus() {
		return status;
	}
	public void setStatus(FlightStatus status) {
		this.status = status;
	}
	
	
	public int getGateNumber() {
		return gateNumber;
	}
	public void setGateNumber(int gateNumber) {
		this.gateNumber = gateNumber;
	}
	
	

}
