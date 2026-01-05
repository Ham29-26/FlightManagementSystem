package com.airport.view;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.airport.model.Flight;
import com.airport.model.FlightStatus;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AirportDashboardPane extends BorderPane {
	
	 // ===== Top (search) =====
	private Label titleLbl;
	
    private TextField flightNoField;
    private ComboBox<FlightStatus> statusCombo;
    private ToggleGroup searchGroup1;
    private ToggleGroup searchGroup2;
    
    private RadioButton departures;
    private RadioButton arrivals;
    
    private RadioButton byFlightNo;
    private RadioButton byStatus;

    // ===== Center (table) =====
    private TableView<Flight> flightTable;

    // ===== Bottom (output) =====
    private TextArea outputTxt;
    
    // ===== Right (buttons) =====
    private Button assignGateBtn;
    private Button processNextBtn;
    private Button updateStatusBtn;
    private Button removeFlightBtn;
    
    private Button viewQueueBtn;
    private Button viewHistoryBtn;
    private Button systemsReportBtn;
    
    private Button undoBtn;
    private Button restoreFlightBtn;
    
    //creating logo cache to prevent lagging
    private final Map<String, Image> logoCache = new HashMap<>();
	
	public AirportDashboardPane() {
		buildTop();
		buildCenter();
		buildBottom();
		buildRight();
	}
	
	
	private void buildTop() {
	
		// ===== Title =====		
	    titleLbl = new Label("Airport Operations Dashboard");
	    titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

	    HBox titleBox = new HBox(titleLbl);
	    titleBox.setAlignment(Pos.CENTER);
	    
	    
	 // ===== Selection label =====
	    Label selectDashboardLbl = new Label("Select dashboard:");
	    selectDashboardLbl.setStyle("-fx-font-weight: bold;");

	    // ===== Radio buttons =====
	    departures = new RadioButton("Departures");
	    arrivals = new RadioButton("Arrivals");
	    
	    departures.setUserData("DEPARTURES");
	    arrivals.setUserData("ARRIVALS");

	    searchGroup1 = new ToggleGroup();
	    departures.setToggleGroup(searchGroup1);
	    arrivals.setToggleGroup(searchGroup1);
	    departures.setSelected(true);

	    // Stack radio buttons vertically
	    VBox dashboardRadioBox = new VBox(departures, arrivals);
	    dashboardRadioBox.setSpacing(5);
	    dashboardRadioBox.setAlignment(Pos.CENTER_LEFT);

	    // Combine label + radio buttons in a horizontal row
	    HBox selectDashboardContainer = new HBox(20, selectDashboardLbl, dashboardRadioBox);
	    selectDashboardContainer.setAlignment(Pos.CENTER);
	    

	    // ===== Search label =====
	    Label searchLbl = new Label("Search by:");

	    HBox searchLabelBox = new HBox(searchLbl);
	    searchLabelBox.setAlignment(Pos.CENTER);
	    

	    // ===== Radio buttons =====
	    byFlightNo = new RadioButton("Flight Number");
	    byStatus = new RadioButton("Status");

	    searchGroup2 = new ToggleGroup();
	    byFlightNo.setToggleGroup(searchGroup2);
	    byStatus.setToggleGroup(searchGroup2);
	    byFlightNo.setSelected(true);

	    VBox searchRadioBox = new VBox(byFlightNo, byStatus);
	    searchRadioBox.setSpacing(5);
	    

	    // ===== Search inputs =====
	    flightNoField = new TextField();
	    flightNoField.setPromptText("Enter Flight Number");

	    statusCombo = new ComboBox<>();
	    statusCombo.setVisible(false);
	    
	    statusCombo.setButtonCell(new ListCell<>() {
	        @Override
	        protected void updateItem(FlightStatus item, boolean empty) {
	            super.updateItem(item, empty);

	            if (item == null || empty) {
	                setText("Select Status");
	            } else {
	                setText(item.toString());
	            }
	        }
	    });


	    StackPane searchInputPane = new StackPane(flightNoField, statusCombo);

	    HBox searchInputRow = new HBox(searchRadioBox, searchInputPane);
	    searchInputRow.setSpacing(15);
	    searchInputRow.setAlignment(Pos.CENTER);
	   

	    // ===== Top container =====
	    VBox topBox = new VBox(
	            titleBox,
	            selectDashboardContainer,
	            searchLabelBox,
	            searchInputRow
	    );

	    topBox.setSpacing(20);
	    topBox.setPadding(new Insets(15));
	    topBox.setAlignment(Pos.CENTER);
	    

	    setTop(topBox);

	}
	
	
	private void buildCenter() {
		
		flightTable = new TableView<>();
		
		//creating table columns
		TableColumn<Flight, ImageView> logoCol = new TableColumn<>("Logo");
		TableColumn<Flight, String> flightNoCol = new TableColumn<>("Flight No");
		TableColumn<Flight, String> airlineCol = new TableColumn<>("Airline");
		TableColumn<Flight, String> originCol = new TableColumn<>("Origin");
		TableColumn<Flight, String> destinationCol = new TableColumn<>("Destination");
		TableColumn<Flight, String> departureCol = new TableColumn<>("Departure");
		TableColumn<Flight, String> statusCol = new TableColumn<>("Status");
		
		
		logoCol.setReorderable(false);
		flightNoCol.setReorderable(false);
		airlineCol.setReorderable(false);
		originCol.setReorderable(false);
		destinationCol.setReorderable(false);
		departureCol.setReorderable(false);
		statusCol.setReorderable(false);

		
		
		flightTable.getColumns().addAll(
				List.of(logoCol,
						flightNoCol,
						airlineCol,
						originCol,
						destinationCol,
						departureCol,
						statusCol)
				);
		
		flightTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		
		//set row height to fit logos
		flightTable.setFixedCellSize(30);
		
		
		//retrieving data for flight table
		logoCol.setCellValueFactory(flightData -> {
		    String path = flightData.getValue().getAirlineLogoPath();

		    if (path == null) {
		        return new SimpleObjectProperty<>(null);
		    }

		    Image img = logoCache.get(path);

		    if (img == null) {
		        InputStream is = getClass().getResourceAsStream(path);
		        if (is != null) {
		            img = new Image(is);
		            logoCache.put(path, img);
		        }
		    }

		    if (img != null) {
		        ImageView iv = new ImageView(img);
		        iv.setFitWidth(40);
		        iv.setFitHeight(40);
		        iv.setPreserveRatio(true);
		        iv.setSmooth(true);
		        return new SimpleObjectProperty<>(iv);
		    }

		    return new SimpleObjectProperty<>(null);
		});
		
		

		// center the logos in the column
		logoCol.setCellFactory(column -> new TableCell<Flight, ImageView>() {
		    @Override
		    protected void updateItem(ImageView item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty || item == null) {
		            setGraphic(null);
		        } else {
		            setGraphic(item);
		            setAlignment(Pos.CENTER); // center the image
		        }
		    }
		});
		
		
		flightNoCol.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
		airlineCol.setCellValueFactory(new PropertyValueFactory<>("airline"));
		originCol.setCellValueFactory(new PropertyValueFactory<>("origin"));
		destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
		departureCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		
		
		VBox centerContainer = new VBox(flightTable);
		centerContainer.setPadding(new Insets(20));
		
		setCenter(centerContainer);
		
	}
	
	
	private void buildBottom() {
		
		Label outputLbl = new Label("System Output");
		outputLbl.setStyle("-fx-font-weight: bold;");
		
		outputTxt = new TextArea();
		outputTxt.setEditable(false);
		outputTxt.setWrapText(true);
		
		//setting font
		outputTxt.setFocusTraversable(false);
		outputTxt.setStyle("""
		    -fx-font-family: Consolas;
		    -fx-font-size: 12px;
		""");

		
		//sizing the text area
		outputTxt.setMinHeight(120);
		outputTxt.setMaxHeight(200);
		outputTxt.setMaxHeight(Double.MAX_VALUE);
		
		VBox bottomContainer = new VBox(6, outputLbl, outputTxt);
		bottomContainer.setSpacing(6);
		bottomContainer.setPadding(new Insets(8));
		bottomContainer.setMinHeight(220);
	    bottomContainer.setPrefHeight(250);
		
		//allowing VBox to grow
		VBox.setVgrow(outputTxt, Priority.ALWAYS);
		
		setBottom(bottomContainer);
		
	}
	
	
	private void buildRight() {
		
		Label opsLabel = new Label("Flight Operations");
	    opsLabel.setStyle("-fx-font-weight: bold;");
	    
	    
	    assignGateBtn = new Button("Assign Gate");
	    processNextBtn = new Button("Board Next Flight");
	    updateStatusBtn = new Button("Update Flight Status");
	    
	    removeFlightBtn = new Button("Remove Flight");
	    
	    //creating tool tip for remove flight button
	    Tooltip removeTooltip = new Tooltip(
	            "Removes the selected flight from the system.\n\n"
	          + "• Deletes the flight from the table and queues.\n"
	          + "• Frees the assigned gate."
	    );

	    removeFlightBtn.setTooltip(removeTooltip);

	    
	    
	    VBox opsBox = new VBox(
	    		opsLabel,
	    		assignGateBtn,
	    		processNextBtn,
	    		updateStatusBtn, 
	    		removeFlightBtn
	    		);
	    
	    opsBox.setSpacing(8);
	    
	    
	    //==== Views Section ====
	    Label viewLabel = new Label("Views");
	    viewLabel.setStyle("-fx-font-weight: bold;");
	    
	    
	    viewQueueBtn = new Button("View Boarding Queue");
	    viewHistoryBtn = new Button("View Action History");
	    systemsReportBtn = new Button("Generate Systems Report");
	    
	    
	    VBox viewBox = new VBox(
	    		viewLabel,
	    		viewQueueBtn,
	    		viewHistoryBtn,
	    		systemsReportBtn
	    		);
	    
	    viewBox.setSpacing(8);
	    
	    
	    //==== Undo Action ====
	    Label undoLabel = new Label("Undo");
	    undoLabel.setStyle("-fx-font-weight: bold;");
	    
	    
	    undoBtn = new Button("Undo Last Action");
	    
	    //creating tool tip for undo button
	    Tooltip undoTooltip = new Tooltip(
	            "Placeholder button to demonstrate undo functionality.\n\n"
	          + "• Removes the most recent action from the stack only..\n"
	          + "• No actual changes are applied."
	    );

	    undoBtn.setTooltip(undoTooltip);
	    
	    restoreFlightBtn = new Button("Restore Last Removed Flight");
	    
	   //creating tool tip for restore flight button
	    Tooltip restoreTooltip = new Tooltip(
	            "Restores the flight to the table view only.\n\n"
	          + "• Gate is not reassigned automatically.\n"
	          + "• Queue placement must be done manually."
	    );

	    restoreFlightBtn.setTooltip(restoreTooltip);

	    
	    
	    VBox undoBox = new VBox(
	    		undoLabel,
	    		undoBtn,
	    		restoreFlightBtn);
	    
	    undoBox.setSpacing(8);
	    
	    
	    //==== Main Right Container ====
	    VBox rightContainer = new VBox(
	    		opsBox,
	    		new Separator(),
	    		viewBox,
	    		new Separator(),
	    		undoBox
	    		);
	    
	    rightContainer.setSpacing(15);
	    rightContainer.setPadding(new Insets(15));
	    rightContainer.setPrefWidth(220);
	    
	    
	    //Make all buttons full width
	    rightContainer.getChildren().stream()
        .filter(n -> n instanceof VBox)
        .map(n -> (VBox) n)
        .flatMap(v -> v.getChildren().stream())
        .filter(n -> n instanceof Button)
        .forEach(b -> ((Button) b).setMaxWidth(Double.MAX_VALUE));
	    
	    
	    setRight(rightContainer);
		
	}
	
	
	// ==== Getters for Controller ====
	public Label getTitleLbl() {
		return titleLbl;
	}
	
	public TableView<Flight> getFlightTable() {
	    return flightTable;
	}

	public TextField getFlightSearchField() {
	    return flightNoField;
	}

	public ComboBox<FlightStatus> getStatusComboBox() {
	    return statusCombo;
	}
	
	public ToggleGroup getSearchToggleGroup1() {
		return searchGroup1;
	}

	public ToggleGroup getSearchToggleGroup2() {
	    return searchGroup2;
	}

	public TextArea getOutputTextArea() {
	    return outputTxt;
	}
	
	public Button getRemoveBtn() {
		return removeFlightBtn;
	}
	
	public void setOutputTextArea(String text) {
		outputTxt.setText(text);
	}
	
	public RadioButton getRbFlightNumber() {
		return byFlightNo;
	}
	
	public RadioButton getRbStatus() {
		return byStatus;
	}
	
	public RadioButton getRbDepartures() {
		return departures;
	}
	
	public RadioButton getRbArrivals() {
		return arrivals;
	}
	
	public Button getViewQueueBtn() {
		return viewQueueBtn;
	}
	
	public Button getProcessNextBtn() {
		return processNextBtn;
	}
	
	public Button getUpdateStatusBtn() {
		return updateStatusBtn;
	}
	
	//methods to attach event handlers to controller
	public void addAssignGateBtnHandler(EventHandler<ActionEvent> handler) {
		assignGateBtn.setOnAction(handler);
	}
	
	public void addProcessNextBtnHandler(EventHandler<ActionEvent> handler) {
		processNextBtn.setOnAction(handler);
	}
	
	public void addUpdateStatusBtnHandler(EventHandler<ActionEvent> handler) {
		updateStatusBtn.setOnAction(handler);
	}
	
	public void addRemoveFlightBtnHandler(EventHandler<ActionEvent> handler) {
		removeFlightBtn.setOnAction(handler);
	}
	
	
	public void addViewQueueBtnHandler(EventHandler<ActionEvent> handler) {
		viewQueueBtn.setOnAction(handler);
	}
	
	public void addViewHistoryBtnHandler(EventHandler<ActionEvent> handler) {
		viewHistoryBtn.setOnAction(handler);
	}
	
	public void addSystemsReportBtnHandler(EventHandler<ActionEvent> handler) {
		systemsReportBtn.setOnAction(handler);
	}
	
	
	public void addUndoBtnHandler(EventHandler<ActionEvent> handler) {
		undoBtn.setOnAction(handler);
	}
	
	public void addRestoreFlightBtnHandler(EventHandler<ActionEvent> handler) {
		restoreFlightBtn.setOnAction(handler);
	}
	

}
