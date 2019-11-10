package gui.ProjectForms;

import ds.Manager;
import ds.Project;
import ds.User;
import gui.Dashboard.DashboardController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProjectAddMembersController implements Initializable {

    @FXML
    private ListView members;

    private Manager manager = null;
    private ProjectFormController projectFormController;

    private ObservableList<User> selectedMembers;

    public void setManager(Manager manager) {
        this.manager = manager;
        showAvailableMembers();
    }

    public void setProjectFormController(ProjectFormController projectFormController){
        this.projectFormController = projectFormController;
    }

    private void showAvailableMembers(){
        ObservableList<User> availableMembers = null;
        try {
            availableMembers = FXCollections.observableArrayList(manager.getAllActiveUsers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        members.setItems(availableMembers);
    }

    public void selectMembers(ActionEvent event){
        selectedMembers = members.getSelectionModel().getSelectedItems();
        projectFormController.setSelectedMembers(selectedMembers);
        projectFormController.getMembersLabelText().setText("Members [" + selectedMembers.size() + " selected]");
        closeAddMembersForm();
    }

    public void closeAddMembersForm(){
        Stage currentStage = (Stage) members.getScene().getWindow();
        currentStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        members.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
}
