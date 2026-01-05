package com.airport.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

public class AirportMenuBar extends MenuBar {
	
	private MenuItem aboutItem, exitItem;
	
	public AirportMenuBar() {
		//temp var for menus and menu items within this MenuBar
		Menu menu;
		
		menu = new Menu("_File");
		
		exitItem = new MenuItem("E_xit");
		exitItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+X"));
		menu.getItems().add(exitItem);

		this.getMenus().add(menu);   


		menu = new Menu("_Help");

		aboutItem = new MenuItem("_About");
		aboutItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+A"));
		menu.getItems().add(aboutItem);

		this.getMenus().add(menu); 
	}
	
	//these methods allow handlers to be externally attached to this menubar and used by the controller
	public void addAboutHandler(EventHandler<ActionEvent> handler) {
		aboutItem.setOnAction(handler);
	}

	public void addExitHandler(EventHandler<ActionEvent> handler) {
		exitItem.setOnAction(handler);
	}
	

}
