package gui.ProjectForms.TaskForms;

import ds.*;
import gui.ProjectForms.ProjectInfoController;
import gui.Users.UsersController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskFormController implements Initializable {

    @FXML
    private TextField newTitle;
    @FXML
    private ComboBox<Task> taskList;

    private Manager manager = null;
    private ProjectInfoController projectInfoController = null;

    private String formAction = "ADD";
    private Task selectedTask;
    private Project selectedProject;

    public void setManager(Manager manager){
        this.manager = manager;
    }

    public void setProjectInfoController(ProjectInfoController pic){
        this.projectInfoController = pic;
    }

    public void setSelectedTask(Task task){
        this.selectedTask = task;
        selectTaskData();
    }

    public void setSelectedProject(int projectID) {
        try {
            selectedProject = manager.getProjectById(projectID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateTaskList();
    }

    public void generateTaskList(){
        ObservableList<Task> tasks = FXCollections.observableArrayList(selectedProject.getAllTasks());
        taskList.setItems(tasks);
    }

    public void newTaskConfirm(ActionEvent event) throws Exception {
        String title = newTitle.getText();

        if(title.length() < 4){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Title!");
            alert.setHeaderText(null);
            alert.setContentText("Title must be at least 4 symbols long!");
            alert.showAndWait();
            return;
        }

        Project taskProject = selectedProject;

        Task addToTask = taskList.getSelectionModel().getSelectedItem();

        if(formAction.equals("ADD")) {
            if(addToTask == null)
                manager.addTaskToProject(taskProject.getId(), title);
            else
                manager.addTaskToTask(addToTask.getId(), title);
        }
        else if(formAction.equals("EDIT")) {
            int taskID = selectedTask.getId();
            manager.editTaskInfo(taskID, title);
        }

        projectInfoController.showProjectTasks(taskProject);
        cancelForm();
    }

    public void selectTaskData() {
        newTitle.setText(selectedTask.getTitle());
        formAction = "EDIT";
    }

    public void cancelForm(){
        Stage currentStage = (Stage) newTitle.getScene().getWindow();
        currentStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
