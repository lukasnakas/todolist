package gui.Users;

import ds.Manager;
import ds.User;
import gui.Dashboard.DashboardController;
import gui.Users.CompanyForms.CompanyFormController;
import gui.Users.PersonForms.PersonFormController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UsersController implements Initializable {

    @FXML
    private TableView usersTable;
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

    @FXML
    public PersonFormController loadNewPersonForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PersonForms/PersonForm.fxml"));
        Node personForm = loader.load();

        PersonFormController pfControl = loader.getController();
        pfControl.setManager(manager);
        pfControl.setUsersController(this);

        formVBox.getChildren().clear();
        formVBox.getChildren().add(personForm);

        return pfControl;
    }

    public CompanyFormController loadNewCompanyForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CompanyForms/CompanyForm.fxml"));
        Node companyForm = loader.load();

        CompanyFormController cfControl = loader.getController();
        cfControl.setManager(manager);
        cfControl.setUsersController(this);

        formVBox.getChildren().clear();
        formVBox.getChildren().add(companyForm);

        return cfControl;
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

    public void refillTable() {
        if (manager != null) {
            usersTable.getItems().clear();
            try {
                usersTable.getItems().addAll(manager.getAllUsers());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void editUserData() throws IOException {
        User user = (User)usersTable.getSelectionModel().getSelectedItem();
        if(user != null) {
            if (user.getUserType().equals("Person")) {
                PersonFormController pfControl = loadNewPersonForm();
                pfControl.selectPersonData(user);
            } else {
                CompanyFormController cfControl = loadNewCompanyForm();
                cfControl.selectCompanyData(user);
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid User");
            alert.setHeaderText(null);
            alert.setContentText("User is not selected!");
            alert.showAndWait();
            return;
        }
    }

    public void toggleUserActive(){
        User user = (User)usersTable.getSelectionModel().getSelectedItem();
        if(user != null) {
            try{
                System.out.println(user.isActive());
                if (user.isActive())
                    manager.toggleUserActive(user.getId(), false);
                else
                    manager.toggleUserActive(user.getId(), true);
                refillTable();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid User");
            alert.setHeaderText(null);
            alert.setContentText("User is not selected!");
            alert.showAndWait();
            return;
        }
    }

    public VBox getFormVBox(){
        return formVBox;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<String, User> column1 = new TableColumn<>("ID");
        column1.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<String, User> column2 = new TableColumn<>("Login");
        column2.setCellValueFactory(new PropertyValueFactory<>("login"));

        TableColumn<String, User> column3 = new TableColumn<>("E-Mail");
        column3.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<String, User> column4 = new TableColumn<>("User Type");
        column4.setCellValueFactory(new PropertyValueFactory<>("userType"));

        TableColumn<String, User> column5 = new TableColumn<>("Is activated");
        column5.setCellValueFactory(new PropertyValueFactory<>("active"));

        TableColumn<String, User> column6 = new TableColumn<>("Projects");
        column6.setCellValueFactory(new PropertyValueFactory<>("projects"));

        usersTable.getColumns().add(column1);
        usersTable.getColumns().add(column2);
        usersTable.getColumns().add(column3);
        usersTable.getColumns().add(column4);
        usersTable.getColumns().add(column5);
        usersTable.getColumns().add(column6);
    }
}
