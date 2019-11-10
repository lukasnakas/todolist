package gui.ProjectForms;

import ds.Manager;
import ds.Project;
import ds.Task;
import ds.User;
import javafx.beans.Observable;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectFormController implements Initializable {

    @FXML
    private TextField newTitle;
    @FXML
    private ComboBox newCreator;
    @FXML
    private Label membersLabel;

    private Manager manager = null;
    private ProjectsController projectsController = null;
    private ObservableList<User> selectedMembers = null;

    private int projectID = 0;
    private String formAction = "ADD";

    public void setManager(Manager manager){
        this.manager = manager;
        generateCreatorList();
    }

    public void setProjectsController(ProjectsController pc){
        this.projectsController = pc;
    }

    public void newProjectConfirm(ActionEvent event) throws Exception {
        String title = newTitle.getText();

        if(title.length() < 4){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Title");
            alert.setHeaderText(null);
            alert.setContentText("Title must be at least 4 symbols long!");
            alert.showAndWait();
            return;
        }

        User creator = (User) newCreator.getSelectionModel().getSelectedItem();

        if(selectedMembers == null)
            selectedMembers = FXCollections.observableArrayList(creator);

        if(formAction.equals("ADD")) {
            Project newProject = manager.addProject(title, creator.getId());

            for(User member : selectedMembers)
                manager.addProjectMember(newProject.getId(), member.getId());
        }
        if(formAction.equals("EDIT")) {
            Project editedProject = manager.editProjectInfo(projectID, title);

            for(User member : selectedMembers)
                manager.addProjectMember(editedProject.getId(), member.getId());

            projectsController.getFormVBox().getChildren().clear();
        }

        projectsController.refillTable();
        clearTextFieldsProject();
    }

    public void editProjectMembers() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ProjectForms/ProjectAddMembers.fxml"));
        Parent root = loader.load();

        ProjectAddMembersController pamControl = loader.getController();
        pamControl.setManager(manager);
        pamControl.setProjectFormController(this);

        Scene scene = new Scene(root);
        Stage addMembers = new Stage();
        addMembers.setTitle("Add members");
        addMembers.setScene(scene);
        addMembers.initOwner(newTitle.getScene().getWindow());
        addMembers.initModality(Modality.APPLICATION_MODAL);
        addMembers.showAndWait();
        centerWindowOnScreen(addMembers);
    }

    private void generateCreatorList(){
        ObservableList<User> availableCreators = null;
        try {
            availableCreators = FXCollections.observableArrayList(manager.getAllActiveUsers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        newCreator.setItems(availableCreators);
    }

    public void setSelectedMembers(ObservableList<User> selectedMembers){
        this.selectedMembers = selectedMembers;
    }

    public Label getMembersLabelText(){
        return this.membersLabel;
    }

    public void selectProjectData(Project project) {
        projectID = project.getId();
        newTitle.setText(project.getTitle());
        newCreator.setPromptText(project.getMembers().get(0).toString());
        formAction = "EDIT";
        allowInputOnAction(formAction);
    }

    private void allowInputOnAction(String formAction){
        if(formAction.equals("ADD")){
            newTitle.setDisable(false);
            newCreator.setDisable(false);
        }
        else if(formAction.equals("EDIT")){
            newTitle.setDisable(false);
            newCreator.setDisable(true);
        }
    }

    private void clearTextFieldsProject() {
        newTitle.setText("");
        //newCreator.setText("");
    }

    public void cancelForm(){
        projectsController.getFormVBox().getChildren().clear();
    }

    public void centerWindowOnScreen(Stage stage){
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
