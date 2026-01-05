package com.airport.view;

import com.airport.model.AirportNames;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AirportSelectionPane extends BorderPane {
	
	private ComboBox<AirportNames> airportCombo;
	private Button loadFlightsBtn;
	
	public AirportSelectionPane() {
		
		// ==== Setting Top
		// ===== Title =====
	    Label title = new Label("Airport Operations Dashboard");
	    title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

	    HBox titleBox = new HBox(title);
	    titleBox.setAlignment(Pos.CENTER);
	    
	    titleBox.setPadding(new Insets(30, 0, 60, 0));
	    
	    setTop(titleBox);
	    
	    
	    // ==== Setting Center
	    // ===== Title
	    Label selectionLbl = new Label("Select an airport to view its operations dashboard:");
	    selectionLbl.setStyle("-fx-font-size: 18px;");
	    
	    HBox selectionBox = new HBox(selectionLbl);
	    selectionBox.setAlignment(Pos.CENTER);
	    
	    // ===== Combo Box
	    airportCombo = new ComboBox<>();
	    airportCombo.getItems().addAll(AirportNames.values());
	    
	    airportCombo.setPromptText("Select Airport");
	    
	    HBox airportComboBox = new HBox(airportCombo);
	    airportComboBox.setAlignment(Pos.CENTER);
	    
	    // ===== Button
	    loadFlightsBtn = new Button("Load Flights");
	    
	    HBox loadFlightsBtnBox = new HBox(loadFlightsBtn);
	    loadFlightsBtnBox.setAlignment(Pos.CENTER);
	    
	    // ===== Container to store all Hbox's as a VBox
	    VBox centerContainer = new VBox(selectionBox,
	    		                       airportComboBox,
	    		                       loadFlightsBtnBox);
	    
	    centerContainer.setSpacing(25);
	    
	    
	    setCenter(centerContainer);
		
	}
	
	
	public ComboBox<AirportNames> getAirportCombo() {
		return airportCombo;
	}
	
	
	public Button getLoadFlightsBtn() {
		return loadFlightsBtn;
	}
	
	
	public void addLoadFlightsBtnHandler(EventHandler<ActionEvent> handler) {
		loadFlightsBtn.setOnAction(handler);
	}
	

}
