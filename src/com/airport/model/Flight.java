package com.airport.model;

public class Flight {
	
	private String flightNumber;
	private String airline;
	private String origin;
	private String destination;
	private String departureTime;
	private FlightStatus status;
	private int gateNumber;
	private String airlineLogoPath;
	
	public Flight() {
		status = FlightStatus.SCHEDULED;
		gateNumber = -1;
	}
	
	public Flight(String flightNumber, String airline, String origin, String destination, String departureTime, FlightStatus status,
			int gateNumber) {
		super();
		this.flightNumber = flightNumber;
		this.airline = airline;
		this.origin = origin;
		this.destination = destination;
		this.departureTime = departureTime;
		this.status = status;
		this.gateNumber = gateNumber;
		
		//automatically sets the logo
		airlineLogoPath = generateLogoPath(airline);
	}
	
	//another constructor specifically used in the controller for the arrival flights 
	public Flight(String flightNumber, String airline, String origin, String destination, String departureTime, FlightStatus status) {
		super();
		this.flightNumber = flightNumber;
		this.airline = airline;
		this.origin = origin;
		this.destination = destination;
		this.departureTime = departureTime;
		this.status = status;
		gateNumber = -1;
		
		//automatically sets the logo
		airlineLogoPath = generateLogoPath(airline);
	}
	
	//more realistic default constructor which sets flight status to scheduled and gate to -1 which mean its not been assigned yet
	//we will be using this constructor in the controller for the departure flights
	public Flight(String flightNumber, String airline, String origin, String destination, String departureTime) {
		this.flightNumber = flightNumber;
		this.airline = airline;
		this.origin = origin;
		this.destination = destination;
		this.departureTime = departureTime;
		status = FlightStatus.SCHEDULED;
		gateNumber = -1;
		
		//automatically sets the logo
		airlineLogoPath = generateLogoPath(airline);
	}
	

	//getter and setter methods pre-generated
	public String getFlightNumber() {
		return flightNumber;
	}
	
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	
	
	public String getAirline() {
		return airline;
	}
	
	public void setAirline(String airline) {
		this.airline = airline;
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
	
	
	public String getAirlineLogoPath() {
		return airlineLogoPath;
	}
	
	public void setAirlineLogoPath(String airlineLogoPath) {
		this.airlineLogoPath = airlineLogoPath;
	}
	
	
	//helper method to generate logo path and verify the path exists
	public String generateLogoPath(String airline) {
		//convert airline name to lower case, remove spaces to match the file name
		String fileName = airline.toLowerCase().replaceAll(" ", "") + ".png";
		
		//build the path relative to resources folder
		String resourcePath = "/logos/" + fileName;
		
		//check if the resource exists
		if (getClass().getResource(resourcePath) != null) {
			return resourcePath;
		} else {
			//fallback to a default "no-logo" image
			return "/logos/no-logo.png";
		}
	}
	
	
	@Override
	public String toString() {
		return flightNumber + " | " + origin + " -> " + destination + " | " + departureTime;
	}
	
	private void test() {
		
	}
	

}
