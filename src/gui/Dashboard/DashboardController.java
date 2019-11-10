package gui.Dashboard;

import ds.Manager;
import gui.ProjectForms.ProjectsController;
import gui.Users.UsersController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private PieChart userStats;
    @FXML
    private AreaChart projectStats;

    private Manager manager = null;
    private Stage stage = null;

    public void setManager(Manager manager) {
        this.manager = manager;
        showUserStats();
        showProjectStats();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void close(ActionEvent event) {
        manager.logout();
        Platform.exit();
    }

    public void openUserManager(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Users/Users.fxml"));
        Parent root = loader.load();

        UsersController userControl = loader.getController();
        userControl.setManager(manager);
        userControl.setStage(stage);

        Scene scene = new Scene(root);
        stage.setTitle("User Management");
        stage.setScene(scene);
        stage.show();
        centerWindowOnScreen(stage);
    }

    public void openProjectManager(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ProjectForms/Projects.fxml"));
        Parent root = loader.load();

        ProjectsController projectControl = loader.getController();
        projectControl.setManager(manager);
        projectControl.setStage(stage);

        Scene scene = new Scene(root);
        stage.setTitle("Project Management");
        stage.setScene(scene);
        stage.show();
        centerWindowOnScreen(stage);
    }

    public void centerWindowOnScreen(Stage stage){
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public void showUserStats() {
        if (manager != null) {
            int[] count = new int[0];
            try {
                count = manager.getUsersCounts();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Persons - " + count[0], count[0]),
                            new PieChart.Data("Companies - " + count[1], count[1]));
            userStats.setTitle("User statistics");
            userStats.setData(pieChartData);
        }
    }

    public void showProjectStats() {
        if (manager != null) {
            XYChart.Series seriesTasks = new XYChart.Series();
            seriesTasks.setName("Tasks per project");

            int[][] data = new int[0][];
            try {
                data = manager.getProjectNumbers();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int[] row : data)
                seriesTasks.getData().add(new XYChart.Data(row[0], row[1]));

            projectStats.getData().addAll(seriesTasks);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //showUserStats();
    }
}
