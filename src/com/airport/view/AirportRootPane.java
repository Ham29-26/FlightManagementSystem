package com.airport.view;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;

public class AirportRootPane extends BorderPane {
	
	private AirportSelectionPane asp;
	private AirportDashboardPane adp;
	private AirportMenuBar amb;
	private TabPane tp;
	private Tab selectionTab;
	private Tab dashboardTab;
	
	public AirportRootPane() {
		//create tab and disable tabs from being closed
		tp = new TabPane();	
		tp.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		//create panes
		asp = new AirportSelectionPane();
		adp = new AirportDashboardPane();
		
		//create tabs with panes created
		selectionTab = new Tab("Select Airport", asp);
		dashboardTab = new Tab("Displaying Dashboard", adp);
		
		//add tabs to tab pane
		tp.getTabs().addAll(selectionTab, dashboardTab);
		
		//initially disabling dash board tab
		dashboardTab.setDisable(true);
		
		//create menu bar
		amb = new AirportMenuBar();
		
		//add menu bar and tab pane to this root pane
		this.setTop(amb);
		this.setCenter(tp);
	}

	//methods allowing sub-containers to be accessed by the controller.
	public AirportSelectionPane getAirportSelectionPane() {
		return asp;
	}

	public AirportDashboardPane getAirportDashboardPane() {
		return adp;
	}

	public AirportMenuBar getAirportMenuBar() {
		return amb;
	}
	
	//method to enable dash board tab
	public void enableDashboardTab() {
	    dashboardTab.setDisable(false);
	}
	
	//method to allow the controller to change tabs
	public void changeTab(int index) {
		tp.getSelectionModel().select(index);
	}
	

}
