package gui;

import ds.Manager;
import gui.Dashboard.DashboardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.*;

public class GUIStart extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Manager manager = new Manager();
        String dashBoardTitle = "ToDoList Admin Dashboard";

        manager.login("admin", "admin");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard/Dashboard.fxml"));
        Parent root = loader.load();

        DashboardController dashControl = loader.getController();
        dashControl.setManager(manager);
        dashControl.setStage(primaryStage);

        Scene scene = new Scene(root);
        primaryStage.setTitle(dashBoardTitle);
        primaryStage.setScene(scene);
        primaryStage.show();
        centerWindowOnScreen(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void centerWindowOnScreen(Stage stage){
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
}
