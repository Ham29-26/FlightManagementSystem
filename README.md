# ✈️ Airport Operations System

A JavaFX-based desktop application that simulates real-world airport operations including flight scheduling, gate assignment, and departure/arrival management across multiple international airports.

The system is built using the MVC (Model-View-Controller) architecture and demonstrates GUI development, event-driven programming, file handling using CSV, and the use of data structures such as stacks and queues to manage airport operations.

---

## 🚀 Features

### 🛫 Airport Selection System
- Select from multiple international airports:
  - JFK, LHR, DXB, IST, SIN, CMN, SYD, ADD, GRU
- Dynamic loading of airport-specific flight data
- Dashboard locked until an airport is selected and loaded

---

### ✈️ Flight Dashboard (Departures & Arrivals)
- Separate views for:
  - Departure flights
  - Arrival flights
- Flight details include:
  - Flight number
  - Airline
  - Origin & destination
  - Departure time
  - Status
  - Airline logo (graphical display)

---

### 🧠 Flight Management Operations
- Assign gates to flights
- Update flight status (Scheduled, Boarding, Arriving, Boarded, Arrived)
- Prevent invalid operations (e.g., assigning gates to completed flights)
- Remove and restore flights

---

### 🧾 Data Handling (CSV Integration)
- Flight data stored externally in CSV files:
  - /resources/csv/<airport>_departures.csv
  - /resources/csv/<airport>_arrivals.csv
- Data dynamically loads based on selected airport
- Easy updates without modifying source code

---

### 📦 Data Structures Used
- Queues
  - Used for boarding and arrival sequences
- Stacks
  - Used for action history and restore functionality
- ObservableLists
  - Used for JavaFX TableView binding and UI updates

---

### 🖼 Airline Logo System
- Airline logos loaded dynamically from resources
- Automatic mapping based on airline name
- Fallback image handling for missing logos
- Optimized image caching for performance

---

### 🧭 System Controls
- Menu Bar:
  - File → Exit
  - Help → About system information
- Load Flights button initializes airport data
- System reset when switching airports

---

## 🛠 Technologies Used

- Java
- JavaFX
- MVC Architecture
- CSV File Handling
- Object-Oriented Programming
- Stacks and Queues
- ObservableLists
- ImageView (JavaFX)

---

## 📂 Project Structure

FlightManagementSystem
│
├── src
│   ├── com.airport.main
│   │   └── ApplicationLoader.java
│   ├── com.airport.controller
│   ├── com.airport.model
│   ├── com.airport.view
│   └── resources
│       ├── csv
│       └── logos

---

## ▶️ How to Run

1. Clone or download the repository
2. Open in Eclipse or IntelliJ IDEA
3. Ensure JavaFX is configured
4. Run:
   ApplicationLoader.java
5. Select an airport and load flights

---

## 📚 What I Learned

- JavaFX GUI development
- MVC architecture
- Event-driven programming
- CSV file handling
- Stacks and queues in real systems
- ObservableList data binding
- Resource management (images + data files)
- Modular system design

---

## 🔮 Future Improvements

- Live flight updates simulation
- Delay tracking system
- Database integration instead of CSV
- Advanced search/filter system
- Improved UI animations
- Map-based airport selection

---

## ✍️ Project Context

This project was developed as a personal aviation-inspired simulation system to explore real-world airport operations, data structures, and JavaFX application design.
