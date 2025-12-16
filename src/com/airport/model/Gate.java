package com.airport.model;

public class Gate {
	
	private int gateNumber;
	private boolean isAvailable;
	
	public Gate(int gateNumber, boolean isAvailable) {
		super();
		this.gateNumber = gateNumber;
		this.isAvailable = isAvailable;
	}
	
	//getter and setter methods pre-generated
	public int getGateNumber() {
		return gateNumber;
	}
	public void setGateNumber(int gateNumber) {
		this.gateNumber = gateNumber;
	}
	
	
	public boolean isAvailable() {
		return isAvailable;
	}
	
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	

	

}
