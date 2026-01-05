package com.airport.controller;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import com.airport.model.Airport;
import com.airport.model.AirportNames;
import com.airport.model.Flight;
import com.airport.model.FlightStatus;
import com.airport.model.Gate;
import com.airport.view.AirportDashboardPane;
import com.airport.view.AirportMenuBar;
import com.airport.view.AirportRootPane;
import com.airport.view.AirportSelectionPane;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class AirportController {
	
	private AirportRootPane view;
	private Airport airport;
	
	private AirportSelectionPane asp;
	private AirportDashboardPane adp;
	
	private AirportMenuBar amb;
	
	//data
	private ObservableList<Flight> activeObservableList;
	
	private ObservableList<Flight> departuresFlightListOL;
	private ObservableList<Flight> arrivalsFlightListOL;
	
	private FilteredList<Flight> filteredDepartureFlights;
	private SortedList<Flight> sortedDepartureFlights;
	
	private FilteredList<Flight> filteredArrivalFlights;
	private SortedList<Flight> sortedArrivalFlights;
	
	private FilteredList<Flight> activeFilteredFlights;
	
	
	private TableView<Flight> flightTable;
	
	private Deque<Flight> removedDepartureFlights; //being used as a stack
	private Deque<Flight> removedArrivalFlights; //being used as a stack
	
	//stack that stores the active stack
	private Deque<Flight> activeStack;
	
	//data + Airport fields/parameters
	private List<Flight> departuresFlightList;
	private List<Flight> arrivalsFlightList;
	
	//constants to store departure and arrival statuses separately
	private final ObservableList<FlightStatus> departureStatuses =
			FXCollections.observableArrayList(
					FlightStatus.SCHEDULED,
					FlightStatus.BOARDING,
					FlightStatus.BOARDED,
					FlightStatus.DELAYED,
					FlightStatus.CANCELLED
				);
	
	private final ObservableList<FlightStatus> arrivalStatuses =
			FXCollections.observableArrayList(
					FlightStatus.ARRIVING,
					FlightStatus.ARRIVED);
	
	
//	//buttons which will change name
//	private Button viewQueueBtn;
	
	
	public AirportController(AirportRootPane view) {
		this.view = view;

		//Creating the model FIRST
		this.airport = new Airport();
		
		//initialize view sub container fields
		asp = view.getAirportSelectionPane();
		adp = view.getAirportDashboardPane();
		amb = view.getAirportMenuBar();
		
		//Initialize stacks to store removed flights
		removedDepartureFlights = new ArrayDeque<>();
		removedArrivalFlights = new ArrayDeque<>();
		activeStack = new ArrayDeque<>();

		//Then initializing UI components
		initialiseData();
		initialiseTable();
		dashboardSwitch();
		
		//Set default dash board mode to Departures
	    //(initial selection does not trigger the toggle change listener)
		initialiseDeparturesMode();
		
		//Attaching the event handlers to UI controls
		attachEventHandlers();
	}
	
	
	private void initialiseData() {
		//populating status combo box
	    ObservableList<FlightStatus> enumList = FXCollections.observableArrayList();
	    enumList.addAll(FlightStatus.values());
		ComboBox<FlightStatus> statusCombo = adp.getStatusComboBox();
		
		statusCombo.setItems(enumList);
		statusCombo.setPromptText("Select Status");
		
		//populating list of departure and arrival flights for the UI and Airport model
		//this has been done in the LoadFlightsBtnHandler

		//initializing flight observable lists
		departuresFlightListOL = FXCollections.observableArrayList();
		arrivalsFlightListOL   = FXCollections.observableArrayList();

	    
	    //populating remaining model parameters
	    airport.addGate(new Gate(1, true));
	    airport.addGate(new Gate(2, true));
	    airport.addGate(new Gate(3, true));
	    airport.addGate(new Gate(4, true));
	    airport.addGate(new Gate(5, true));
	    airport.addGate(new Gate(6, true));
	    airport.addGate(new Gate(7, true));
	    airport.addGate(new Gate(8, true));
	    
	}
	
	
	private void initialiseTable() {
		filteredDepartureFlights = new FilteredList<>(departuresFlightListOL, p -> true);
		
		//Wrap in a SortedList for automatic column sorting
		sortedDepartureFlights = new SortedList<>(filteredDepartureFlights);

		//Bind the SortedList comparator to the TableView comparator
		sortedDepartureFlights.comparatorProperty().bind(adp.getFlightTable().comparatorProperty());
		
		
		//similar for arrivals
		filteredArrivalFlights = new FilteredList<>(arrivalsFlightListOL, p -> true);

		//Wrap in a SortedList for automatic column sorting
		sortedArrivalFlights = new SortedList<>(filteredArrivalFlights);

		//Bind the SortedList comparator to the TableView comparator
		sortedArrivalFlights.comparatorProperty().bind(adp.getFlightTable().comparatorProperty());
		
	}
	
	
	//Initializes all active variables and UI components for Departures mode.
	//This method is required because the default selected toggle does not
	//trigger the dash board switch change listener on application startup.
	private void initialiseDeparturesMode() {
		//setting items of the combo box
		adp.getStatusComboBox().setItems(departureStatuses);
		
		activeObservableList = departuresFlightListOL;
		activeFilteredFlights = filteredDepartureFlights;
		activeStack = removedDepartureFlights;
		
		adp.getFlightTable().setItems(sortedDepartureFlights);
		adp.getViewQueueBtn().setText("View Boarding Queue");
		adp.getProcessNextBtn().setText("Board Next Flight");
	}
	
	
	private void dashboardSwitch() {
		
		ToggleGroup searchGroup1 = adp.getSearchToggleGroup1();
		RadioButton rbDepartures = adp.getRbDepartures();
		RadioButton rbArrivals = adp.getRbArrivals();
		Button viewQueueBtn = adp.getViewQueueBtn();
		Button processNextBtn = adp.getProcessNextBtn();
		
		searchGroup1.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			
			if (newVal == rbDepartures) {
				//resetting the flight search input field and status combo box after switching
				adp.getFlightSearchField().clear();
				adp.getFlightSearchField().setPromptText("Enter Flight Number");
				
				adp.getStatusComboBox().setValue(null);
				
				//setting items of the combo box
				adp.getStatusComboBox().setItems(departureStatuses);
				
				//setting the active observable list
				activeObservableList = departuresFlightListOL;
				
				//setting the items of the table
				adp.getFlightTable().setItems(sortedDepartureFlights);
			    activeFilteredFlights = filteredDepartureFlights;
			    
			    //dynamically setting the name of the buttons
			    viewQueueBtn.setText("View Boarding Queue");
			    processNextBtn.setText("Board Next Flight");
			    
			    //setting the stack
			    activeStack = removedDepartureFlights;
			    
			}
			
			else if (newVal == rbArrivals) {
				//resetting the flight search input field and status combo box after switching
				adp.getFlightSearchField().clear();
				adp.getFlightSearchField().setPromptText("Enter Flight Number");
				
				adp.getStatusComboBox().setValue(null);
				
				//setting the items of the combo box
				adp.getStatusComboBox().setItems(arrivalStatuses);
				
				//setting the active observable list
				activeObservableList = arrivalsFlightListOL;
				
				//setting the items of the table
				adp.getFlightTable().setItems(sortedArrivalFlights);
			    activeFilteredFlights = filteredArrivalFlights;
			    
			    //dynamically setting the name of the buttons
			    viewQueueBtn.setText("View Arriving Queue");
			    processNextBtn.setText("Mark as Landed");
			    
			    //setting the stack
			    activeStack = removedArrivalFlights;
			    
			}
		});
		
	}
	
	
	private void resetActiveDashboard() {
	    activeFilteredFlights.setPredicate(flight -> true);
	}
	
	
	private void attachEventHandlers() {
		
		TextField flightSearchField = adp.getFlightSearchField();
		ComboBox<FlightStatus> statusCombo = adp.getStatusComboBox();
		ToggleGroup searchGroup2 = adp.getSearchToggleGroup2();
		
		RadioButton rbFlightNumber = adp.getRbFlightNumber();
		RadioButton rbStatus = adp.getRbStatus();
		
		//Live search by flight number
		flightSearchField.textProperty().addListener((obs, oldVal, newVal) -> {
				filterByFlightNumber(newVal);
		});
		
		
		//Live search by status
		statusCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
				filterByStatus(newVal);
		});
		
		
		//handler to manage toggle group logic
		searchGroup2.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null) {
				return;
			}
			
			//resets the dash board when switching between different search methods
			resetActiveDashboard();
			
			if (newVal == rbFlightNumber) {
				flightSearchField.clear();
				flightSearchField.setPromptText("Enter Flight Number");			
				
				flightSearchField.setVisible(true);
				statusCombo.setVisible(false);
				
			} else if (newVal == rbStatus) {
				statusCombo.setValue(null);
				
				statusCombo.setVisible(true);
				flightSearchField.setVisible(false);
			}
			
		});
		
		//attaching event handlers
		amb.addExitHandler(e -> System.exit(0));
		
		adp.addViewQueueBtnHandler(new ViewQueueBtnHandler());
		adp.addViewHistoryBtnHandler(new ViewHistoryBtnHandler());
		adp.addAssignGateBtnHandler(new AssignGateBtnHandler());
		adp.addProcessNextBtnHandler(new ProcessNextBtnHandler());
		adp.addUpdateStatusBtnHandler(new UpdateStatusBtnHandler());
		adp.addRemoveFlightBtnHandler(new RemoveFlightBtnHandler());;
		adp.addSystemsReportBtnHandler(new SystemsReportBtnHandler());
		adp.addUndoBtnHandler(new UndoBtnHandler());
		adp.addRestoreFlightBtnHandler(new RestoreFlightBtnHandler());
		asp.addLoadFlightsBtnHandler(new LoadFlightsBtnHandler());
		amb.addAboutHandler(new AboutHandler());
		
		
		//disabling remove button in case nothing is chosen or if the selected is boarded/arrived
		adp.getRemoveBtn().disableProperty().bind(
				Bindings.createBooleanBinding(() -> {
					Flight selected = adp.getFlightTable().getSelectionModel().getSelectedItem();
					
					return selected == null || selected.getStatus() == FlightStatus.BOARDED || selected.getStatus() == FlightStatus.ARRIVED;
				}, 
						adp.getFlightTable().getSelectionModel().selectedItemProperty()
				)
		);
		
		
		//disabling update button in case nothing is chosen
		adp.getUpdateStatusBtn().disableProperty().bind(
				Bindings.createBooleanBinding(() -> {
					Flight selected = adp.getFlightTable().getSelectionModel().getSelectedItem();
					
					return selected == null;
				},
				adp.getFlightTable().getSelectionModel().selectedIndexProperty()
				)		
		);
		
		
		//disabling load flights button until an airport is chosen
		asp.getLoadFlightsBtn().disableProperty().bind(
			    asp.getAirportCombo().getSelectionModel()
			        .selectedItemProperty().isNull()
			);

		
	}
	
	
	private void filterByFlightNumber(String input) {
		activeFilteredFlights.setPredicate(flight -> {
			if (input == null || input.isBlank()) {
				return true; //show all
			}
			return flight.getFlightNumber()
					.toLowerCase()
					.contains(input.toLowerCase());
		});
	}
	
	
	private void filterByStatus(FlightStatus status) {
		activeFilteredFlights.setPredicate(flight -> {
			if (status == null) {
				return true;
			}
			return flight.getStatus() == status;
		});
	}
	
	
	//methods to generate the CSV file path for departures and arrivals
	private String generateDeparturesCSVFilePath() {
		AirportNames selected = asp.getAirportCombo()
				.getSelectionModel().getSelectedItem();

		if (selected == null) {
			throw new IllegalStateException("No airport selected");
		}
		
		return "/csv/" + selected.getCode().toLowerCase() + "_departures.csv";
	}
	
	
	private String generateArrivalsCSVFilePath() {
		AirportNames selected = asp.getAirportCombo()
				.getSelectionModel().getSelectedItem();
		
		if (selected == null) {
			throw new IllegalStateException("No airport selected");
		}
		
		return "/csv/" + selected.getCode().toLowerCase() + "_arrivals.csv";
	}
	
	
	//event handlers for all the buttons
	private class ViewQueueBtnHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			ToggleGroup searchGroup1 = adp.getSearchToggleGroup1();
			String queue = "";
			
			if (searchGroup1.getSelectedToggle().getUserData().toString().equals("DEPARTURES")) {
				queue = airport.viewQueue(airport.getBoardingQueue());
			
			} else if (searchGroup1.getSelectedToggle().getUserData().toString().equals("ARRIVALS")) {
				queue = airport.viewQueue(airport.getArrivingQueue());
			}
			
			adp.setOutputTextArea(queue);
			
			//clearing the selections if any
			adp.getFlightTable().getSelectionModel().clearSelection();
			
		}
	}
	
	
	private class ViewHistoryBtnHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			String actionHistory = airport.viewActionHistory();
			
			adp.setOutputTextArea(actionHistory);
			
			//clearing the selections if any
			adp.getFlightTable().getSelectionModel().clearSelection();
		}
	}
	
	
	private class SystemsReportBtnHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			String systemsReport = airport.generateSystemReport();
			
			adp.setOutputTextArea(systemsReport);
			
			//clearing the selections if any
			adp.getFlightTable().getSelectionModel().clearSelection();
		}
	}
	
	private class AssignGateBtnHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			ToggleGroup searchGroup1 = adp.getSearchToggleGroup1();
			flightTable = adp.getFlightTable();

			Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();

			//shows error message in case no flight is selected
			if (selectedFlight == null) {
				alertDialogBuilder(AlertType.ERROR,
						"Selection Error",
						"No Flight Selected",
						"Please select a flight to assign a gate.");
				return;
			}
			
			//prevent gate assignment is flight is either BOARDED or ARRIVED
			if (selectedFlight.getStatus() == FlightStatus.BOARDED
					|| selectedFlight.getStatus() == FlightStatus.ARRIVED) {

				alertDialogBuilder(AlertType.ERROR,
						"Gate Assignment Error",
						"Flight Completed",
						"Cannot assign a gate to a flight that has already "
								+ selectedFlight.getStatus());
				return;
			}
			
			//preventing gate assignment if gate has already been assigned
			if (selectedFlight.getGateNumber() != -1) {
				alertDialogBuilder(AlertType.ERROR,
						"Gate Assignment Error",
						"Gate Already Assigned",
						"This flight already has a gate assigned.");
				return;
			}
			
			
			//prints output message
			String message = airport.assignGateToFlight(selectedFlight);
			adp.setOutputTextArea(message);
			
			
			//adds flight to boarding/arriving queue
			if (searchGroup1.getSelectedToggle().getUserData().toString().equals("DEPARTURES")) {
				airport.addToBoardingQueue(selectedFlight);
			
			} else if (searchGroup1.getSelectedToggle().getUserData().toString().equals("ARRIVALS")) {
				airport.addToArrivingQueue(selectedFlight);
			}
			

			//clearing the selection
			adp.getFlightTable().getSelectionModel().clearSelection();

		}
	}
	
	
	private class ProcessNextBtnHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			ToggleGroup searchGroup1 = adp.getSearchToggleGroup1();
			String message = "";
			
			if (searchGroup1.getSelectedToggle().getUserData().toString().equals("DEPARTURES")) {
				message = airport.processNextFlight(airport.getBoardingQueue());
			 
			} else if (searchGroup1.getSelectedToggle().getUserData().toString().equals("ARRIVALS")) {
				message = airport.processNextFlight(airport.getArrivingQueue());
			}
			
			adp.setOutputTextArea(message);
			adp.getFlightTable().refresh();
			
			//clearing the selections if any
			adp.getFlightTable().getSelectionModel().clearSelection();
		}
	}
	
	
	private class UpdateStatusBtnHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			ToggleGroup searchGroup1 = adp.getSearchToggleGroup1();
			
			//1. Get selected flight
			Flight selectedFlight = adp.getFlightTable().getSelectionModel().getSelectedItem();
			
			if (selectedFlight == null) {
				alertDialogBuilder(AlertType.ERROR, "No Flight Selected", 
						"Update Failed", "Please select a flight from the table");
				return;
			}
			
			//2. Create dialog to let user select a status
			Dialog<FlightStatus> dialog = new Dialog<>();
			dialog.setTitle("Update Flight Status");
			dialog.setHeaderText("Update status for flight + " + selectedFlight.getFlightNumber());
			
			//3. Buttons
			ButtonType updateBtn = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(updateBtn, ButtonType.CANCEL);
			
			//4. ComboBox
			ComboBox<FlightStatus> statusCombo = new ComboBox<>();
			
			if (searchGroup1.getSelectedToggle().getUserData().toString().equals("DEPARTURES")) {
				statusCombo.setItems(departureStatuses);
			
			} else if (searchGroup1.getSelectedToggle().getUserData().toString().equals("ARRIVALS")) {
				statusCombo.setItems(arrivalStatuses);
			}
			
			statusCombo.setPromptText("Select Status");
			
			//Layout
			VBox content = new VBox(10, new Label("New Status:"), statusCombo);
			dialog.getDialogPane().setContent(content);
			
			//5. Convert result
			dialog.setResultConverter(button -> {
				if (button == updateBtn) {
					return statusCombo.getValue();
				}
				return null;
			});
			
			//6. Handle result
			Optional<FlightStatus> result = dialog.showAndWait();
			
			result.ifPresent(newStatus -> {
				if (newStatus == null) {
					alertDialogBuilder(AlertType.ERROR, "No Status Selected",
							"Update Failed", "Please choose a status");
					return;
				}
				
				String message = airport.updateFlightStatus(selectedFlight.getFlightNumber(), newStatus);
				
				adp.setOutputTextArea(message);
				adp.getFlightTable().refresh();
			});
			
			//clearing the selection
			adp.getFlightTable().getSelectionModel().clearSelection();
			
		}
	}
	
	private class RemoveFlightBtnHandler implements EventHandler<ActionEvent> {
		public void handle (ActionEvent e) {
			Flight selectedFlight = adp.getFlightTable().getSelectionModel().getSelectedItem();
			ToggleGroup searchGroup1 = adp.getSearchToggleGroup1();
			String message = "";
			
			//confirmation dialog
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirm Removal");
			alert.setHeaderText("Remove Flight");
			alert.setContentText(
					"This will remove the flight " + selectedFlight.getFlightNumber() 
					+ " from the system including any queues and its assigned gate. Continue? "
					);

			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.isPresent() && result.get() == ButtonType.OK) {
				
				//free up the flight's gate
				for (Gate g: airport.getGates()) {
					if (g.getGateNumber() == selectedFlight.getGateNumber()) {
						g.setAvailable(true);
					}
				}
				
				//pushing flights into the related stack as well as removing from model and table view
				//in cases of both departure and arrival
				if (searchGroup1.getSelectedToggle().getUserData().toString().equals("DEPARTURES")) {
					removedDepartureFlights.push(selectedFlight);
					message = airport.removeFlightByNumber(selectedFlight.getFlightNumber(), airport.getBoardingQueue());
				
				} else if (searchGroup1.getSelectedToggle().getUserData().toString().equals("ARRIVALS")) {
					removedArrivalFlights.push(selectedFlight);
					message = airport.removeFlightByNumber(selectedFlight.getFlightNumber(), airport.getArrivingQueue());
				}
				
				//removing flight from the TableView (ObservableList)
				activeObservableList.remove(selectedFlight);
				
				//generating output message
				adp.setOutputTextArea(message);
			}
			
			//clearing the selection
			adp.getFlightTable().getSelectionModel().clearSelection();
		        
		}
	}
	
	private class UndoBtnHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			String message = airport.undoLastAction();
			
			adp.setOutputTextArea(message);
			
			//clearing the selections if any
			adp.getFlightTable().getSelectionModel().clearSelection();
		}
	}
	
	private class RestoreFlightBtnHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			// Note: Flights can be removed even if assigned a gate.
			// Restored flights must be reconfigured manually.
			if (!activeStack.isEmpty()) {
				//storing the last removed flight in a variable and removing it from the stack
				Flight lastFlight = activeStack.pop();
				
				//confirmation dialog
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirm Restore");
				alert.setHeaderText("Restore Flight");
				alert.setContentText(
					    "This will restore the most recently removed flight:\n"
					    + lastFlight.getFlightNumber() + "\n\n"
					    + "It will not be re-assigned to any gate or queue."
					);


				Optional<ButtonType> result = alert.showAndWait();
				
				if (result.isPresent() && result.get() == ButtonType.OK) {
					
					//adding the flight back to the active flight observable list so it can be displayed
					//on the table view again
					activeObservableList.add(lastFlight);
					
					//adding the flight to the original flights list from the Airport model
					airport.addFlight(lastFlight);
					
					//adding action to action history
					airport.getActionHistory().push("Restored flight " + lastFlight.getFlightNumber()
					+ " into the table view.");
					
					//generating output message
					adp.setOutputTextArea("Flight " + lastFlight.getFlightNumber() 
					+ " restored into the table view. Assign gate and add to queue manually if needed.");
				}
			}
			
			else {
				adp.setOutputTextArea("No previously removed flights are available for restoration.");
			}
			
			//clearing the selections if any
			adp.getFlightTable().getSelectionModel().clearSelection();
			
		}
	}
	
	
	private class LoadFlightsBtnHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			
			AirportNames selectedAirport = asp.getAirportCombo()
					.getSelectionModel().getSelectedItem();
			
			if (selectedAirport == null) {
				System.out.println("No airport selected!");
				return;
			}
			
			departuresFlightList = airport.readFlightsFromCSV(generateDeparturesCSVFilePath());
			
			arrivalsFlightList = airport.readFlightsFromCSV(generateArrivalsCSVFilePath());
			
			//clearing any existing flights
			airport.clearFlights();
			
			// populate model
			airport.addAllFlights(departuresFlightList);
			airport.addAllFlights(arrivalsFlightList);

			// populate observable lists for UI
			departuresFlightListOL.setAll(departuresFlightList);
			arrivalsFlightListOL.setAll(arrivalsFlightList);
	
			
			//clearing all existing stacks and queues
			airport.clearBoardingQueue();
			airport.clearArrivingQueue();
			airport.clearActionHistory();
			airport.freeAllGates();  //frees all gates
			removedDepartureFlights.clear();
			removedArrivalFlights.clear();
			activeStack.clear();
			
			adp.setOutputTextArea("Operations output will be displayed here....");
			
			//setting selected item in the dash board pane to departures
			adp.getSearchToggleGroup1().selectToggle(
					adp.getRbDepartures());
			
			//setting the title label in the dash board pane and centering it
			adp.getTitleLbl().setText("Airport Operations Dashboard\n"
					+ selectedAirport.getDisplayName()
					+ " (" + selectedAirport.getCode() + ")");
			
			adp.getTitleLbl().setTextAlignment(TextAlignment.CENTER);
			
			//enabling dash board tab
			view.enableDashboardTab();
			
			//changing tabs to show the dash board page
			view.changeTab(1);
			
		}
		
	}
	
	
	private class AboutHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			String header = "Airport Operations System — Version 2.0";
	        String content = "Welcome to the updated Airport Operations System!\n\n"
	                + "This version introduces a fully interactive JavaFX dashboard for multiple airports.\n"
	                + "- View departure and arrival flights for each airport.\n"
	                + "- Assign gates, board passengers, and manage flight operations.\n"
	                + "- Restore removed flights if needed.\n"
	                + "- Uses CSV files for flight data and displays airline logos for a visual experience.\n\n"
	                + "We hope this system enhances your airport operations management experience!";
	        
	        alertDialogBuilder(AlertType.INFORMATION, header, "About the System", content);
		}
	}
	
	
	
	
	//helper method to create alert dialogs
	private void alertDialogBuilder(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

}
