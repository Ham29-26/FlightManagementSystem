Airport Operations System v2.0
Airport Operations System is a JavaFX-based application designed to manage airport flight operations in a user-friendly way. It allows you to view, update, and manage departure and arrival flights for multiple airports.

Features
•	Multi-airport support (JFK, IST, SIN, CMN, SYD, LHR, ADD, GRU, etc.)
•	Read flight data from CSV files for departures and arrivals
•	Display airline logos dynamically from resources
•	Dashboard for departures and arrivals with clear system output
•	Assign and manage gates for flights
•	Update flight statuses (Scheduled, Boarding, Arriving, etc.)
•	Restore removed flights with the restore button
•	Clear and refresh flight data when switching airports
•	Smooth and responsive JavaFX UI

Usage
1.	Select an airport from the dropdown menu.
2.	Click Load Flights to populate the dashboard.
3.	View departures and arrivals in separate tabs.
4.	Select a flight to assign gates, update status, or perform other operations.
5.	Use the System Output area to track actions and system messages.

Files and Structure
•	src/resources/csv/ → CSV files for each airport’s flights
•	src/resources/logos/ → Airline logo images
•	src/com/airport/... → Java classes for controllers, models, and views

About
This project has been updated to JavaFX to improve the UI experience.
Version 2.0 introduces multiple airports, CSV-based flight data, airline logos, and enhanced dashboard functionality.
