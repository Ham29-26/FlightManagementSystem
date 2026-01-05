package com.airport.model;

public enum AirportNames {
	
	JFK("JFK", "New York John F Kennedy International Airport – JFK"),
	DXB("DXB", "Dubai International Airport - DXB"),
    IST("IST", "Istanbul Airport - IST"),
    SIN("SIN", "Singapore Changi International Airport - SIN"),
    CMN("CMN", "Casablanca Mohammed V International Airport - CMN"),
    ADD("ADD", "Addis Ababa Bole International Airport - ADD"),
    GRU("GRU", "Sao Paulo Guarulhos International Airport - GRU"),
    SYD("SYD", "Sydney Kingsford Smith International Airport - SYD"),
    LHR("LHR", "London Heathrow International Airport - LHR");
	
	
	private final String code;
	private final String displayName;
	
	AirportNames(String code, String displayName) {
		this.code = code;
		this.displayName = displayName;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
	
	
	private void test() {
		
	}
}
