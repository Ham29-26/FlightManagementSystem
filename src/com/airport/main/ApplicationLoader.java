package com.airport.main;

import com.airport.controller.AirportController;
import com.airport.view.AirportRootPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationLoader extends Application {

	private AirportRootPane view;

    @Override
    public void init() {
        view = new AirportRootPane();
        new AirportController(view);
    }

    @Override
    public void start(Stage stage) {
        stage.setMinWidth(1000);
        stage.setMinHeight(700);

        stage.setTitle("Airport Operations System");
        stage.setScene(new Scene(view, 1200, 800));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
