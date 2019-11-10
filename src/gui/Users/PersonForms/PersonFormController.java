package gui.Users.PersonForms;

import ds.Manager;
import ds.Person;
import ds.User;
import gui.Users.UsersController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonFormController implements Initializable {

    @FXML
    private TextField newLogin, newFirstName, newLastName, newEmail;
    @FXML
    private PasswordField newPass;

    private Manager manager = null;
    private UsersController usersController = null;
    private int personId = 0;
    private String formAction = "REGISTER";

    public void setManager(Manager manager){
        this.manager = manager;
    }

    public void setUsersController(UsersController uc){
        this.usersController = uc;
    }

    public void newPersonConfirm(ActionEvent event) throws Exception {
        String login = newLogin.getText();
        String pass = newPass.getText();
        String firstName = newFirstName.getText();
        String lastName = newLastName.getText();
        String email = newEmail.getText();

        if(login.length() < 4) {
            showInvalidInputError("Login");
            return;
        }
        else if(pass.length() < 4) {
            showInvalidInputError("Password");
            return;
        }
        else if(firstName.length() < 2) {
            showInvalidInputError("First name");
            return;
        }
        else if(lastName.length() < 2) {
            showInvalidInputError("Last name");
            return;
        }
        else if(email.length() < 8 && email.length() > 0) {
            showInvalidInputError("E-mail");
            return;
        }

        if(formAction.equals("REGISTER"))
            manager.registerPerson(login, pass, firstName, lastName, email);
        else if(formAction.equals("EDIT")) {
            manager.editPersonInfo(personId, firstName, lastName, email);
            usersController.getFormVBox().getChildren().clear();
        }

        usersController.refillTable();
        clearTextFieldsPerson();
    }

    private void showInvalidInputError(String inputField){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid " + inputField);
        alert.setHeaderText(null);
        switch (inputField){
            case "Login":
            case "Password":
                alert.setContentText(inputField + " must be at least 4 symbols long.");
                break;
            case "First name":
            case "Last name":
                alert.setContentText(inputField + " must be at least 5 symbols long.");
                break;
            case "E-mail":
                alert.setContentText(inputField + " must be at least 8 symbols long.");
                break;
        }
        alert.showAndWait();
    }

    public void selectPersonData(User user) {
        System.out.println(user);
        Person person = (Person) user;
        personId = person.getId();
        newLogin.setText(person.getLogin());
        newPass.setText(person.getPass());
        newFirstName.setText(person.getFirstName());
        newLastName.setText(person.getLastName());
        newEmail.setText(person.getEmail());
        formAction = "EDIT";
        allowInputOnAction(formAction);
    }

    private void clearTextFieldsPerson() {
        newLogin.setText("");
        newPass.setText("");
        newFirstName.setText("");
        newLastName.setText("");
        newEmail.setText("");
    }

    private void allowInputOnAction(String formAction){
        if(formAction.equals("REGISTER")){
            newLogin.setDisable(false);
            newPass.setDisable(false);
            newFirstName.setDisable(false);
            newLastName.setDisable(false);
            newEmail.setDisable(false);
        }
        else if(formAction.equals("EDIT")){
            newLogin.setDisable(true);
            newPass.setDisable(false);
            newFirstName.setDisable(false);
            newLastName.setDisable(false);
            newEmail.setDisable(false);
        }
    }

    public void cancelForm(){
        usersController.getFormVBox().getChildren().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allowInputOnAction(formAction);
    }
}
