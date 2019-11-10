package gui.Users.CompanyForms;

import ds.Company;
import ds.Manager;
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

public class CompanyFormController implements Initializable {

    @FXML
    private TextField newLogin, newTitle, newEmail;
    @FXML
    private PasswordField newPass;

    private Manager manager = null;
    private UsersController usersController = null;
    private int userId = 0;
    private String formAction = "REGISTER";

    public void setManager(Manager manager){
        this.manager = manager;
    }

    public void setUsersController(UsersController uc){
        this.usersController = uc;
    }

    public void newCompanyConfirm(ActionEvent event) throws Exception {
        String login = newLogin.getText();
        String pass = newPass.getText();
        String title = newTitle.getText();
        String email = newEmail.getText();

        if(login.length() < 4) {
            showInvalidInputError("Login");
            return;
        }
        else if(pass.length() < 4) {
            showInvalidInputError("Password");
            return;
        }
        else if(title.length() < 5) {
            showInvalidInputError("Title");
            return;
        }
        else if(email.length() < 8 && email.length() > 0) {
            showInvalidInputError("E-mail");
            return;
        }

        if(formAction.equals("REGISTER"))
            manager.registerCompany(login, pass, title, email);
        else if(formAction.equals("EDIT")) {
            manager.editCompanyInfo(userId, title, email);
            usersController.getFormVBox().getChildren().clear();
        }

        usersController.refillTable();
        clearTextFieldsCompany();
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
            case "Title":
                alert.setContentText(inputField + " must be at least 5 symbols long.");
                break;
            case "E-mail":
                alert.setContentText(inputField + " must be at least 8 symbols long.");
                break;
        }
        alert.showAndWait();
    }

    public void selectCompanyData(User user) {
        Company company = (Company) user;
        userId = company.getId();
        newLogin.setText(company.getLogin());
        newPass.setText(company.getPass());
        newTitle.setText(company.getTitle());
        newEmail.setText(company.getEmail());
        formAction = "EDIT";
        allowInputOnAction(formAction);
    }

    private void clearTextFieldsCompany() {
        newLogin.setText("");
        newPass.setText("");
        newTitle.setText("");
        newEmail.setText("");
    }

    private void allowInputOnAction(String formAction){
        if(formAction.equals("REGISTER")){
            newLogin.setDisable(false);
            newPass.setDisable(false);
            newTitle.setDisable(false);
            newEmail.setDisable(false);
        }
        else if(formAction.equals("EDIT")){
            newLogin.setDisable(true);
            newPass.setDisable(false);
            newTitle.setDisable(false);
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
