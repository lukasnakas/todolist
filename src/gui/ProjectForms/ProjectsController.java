package gui.ProjectForms;

import ds.Manager;
import ds.Project;
import gui.Dashboard.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectsController implements Initializable {

    @FXML
    private TableView newProjectTable;
    @FXML
    private VBox formVBox;

    private Manager manager = null;
    private Stage stage = null;

    private String dashBoardTitle = "ToDoList Admin Dashboard";

    public void setManager(Manager manager) {
        this.manager = manager;
        refillTable();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ProjectFormController loadNewProjectForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectForm.fxml"));
        Node projectForm = loader.load();

        ProjectFormController pfControl = loader.getController();
        pfControl.setManager(manager);
        pfControl.setProjectsController(this);

        formVBox.getChildren().clear();
        formVBox.getChildren().add(projectForm);

        return pfControl;
    }

    public ProjectInfoController showProjectInfo() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectInfo.fxml"));
        Node projectInfo = loader.load();

        ProjectInfoController piControl = loader.getController();
        piControl.setManager(manager);
        piControl.setProjectsController(this);

        Project selectedProject = (Project) newProjectTable.getSelectionModel().getSelectedItem();
        piControl.selectProjectData(selectedProject);

        formVBox.getChildren().clear();
        formVBox.getChildren().add(projectInfo);

        return piControl;
    }

    public void editProject() throws IOException {
        Project project = (Project) newProjectTable.getSelectionModel().getSelectedItem();
        if(project != null) {
            ProjectFormController pfControl = loadNewProjectForm();
            pfControl.selectProjectData(project);
        }
        // TODO: show error "Project is not selected"
    }

    public void deleteProject() throws Exception {
        Project project = (Project) newProjectTable.getSelectionModel().getSelectedItem();
        int projectID = project.getId();
        manager.deleteProject(projectID);
        refillTable();
    }

    public void exitUserManager(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Dashboard/Dashboard.fxml"));
        Parent root = loader.load();

        DashboardController dashControl = loader.getController();
        dashControl.setManager(manager);
        dashControl.setStage(stage);

        Scene scene = new Scene(root);
        stage.setTitle(dashBoardTitle);
        stage.setScene(scene);
        stage.show();
        centerWindowOnScreen(stage);
    }

    public void centerWindowOnScreen(Stage stage){
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public void refillTable(){
        if (manager != null) {
            newProjectTable.getItems().clear();
            try {
                newProjectTable.getItems().addAll(manager.getP());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public VBox getFormVBox(){
        return formVBox;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<String, Project> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<String, Project> column2 = new TableColumn<>("Title");
        column2.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<String, Project> column3 = new TableColumn<>("Members");
        column3.setCellValueFactory(new PropertyValueFactory<>("members"));

        TableColumn<String, Project> column4 = new TableColumn<>("Tasks");
        column4.setCellValueFactory(new PropertyValueFactory<>("tasks"));

        newProjectTable.getColumns().add(column1);
        newProjectTable.getColumns().add(column2);
        newProjectTable.getColumns().add(column3);
        newProjectTable.getColumns().add(column4);
    }
}
