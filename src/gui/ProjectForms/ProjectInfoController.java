package gui.ProjectForms;

import com.sun.glass.ui.Pixels;
import ds.Manager;
import ds.Project;
import ds.Task;
import ds.User;
import gui.ProjectForms.TaskForms.TaskFormController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ResourceBundle;

public class ProjectInfoController implements Initializable {

    @FXML
    private Label projectTitle, creatorName;
    @FXML
    private ListView projectMembers;
    @FXML
    private TreeView<Task> projectTasks;

    private Manager manager = null;
    private ProjectsController projectsController = null;

    private int projectID = 0;
    private Task selectedTask = null;

    public void setManager(Manager manager){
        this.manager = manager;
    }

    public void setProjectsController(ProjectsController pc){
        this.projectsController = pc;
    }

    public void selectProjectData(Project project) {
        try {
            projectID = project.getId();
            projectTitle.setText(project.getTitle());
            creatorName.setText(project.getMembers().get(0).toString());
            showProjectMembers(project);
            showProjectTasks(project);
        } catch (Exception e){
            System.out.println("Not a valid selection.");
        }
    }

    public void loadNewTaskForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ProjectForms/TaskForms/TaskForm.fxml"));
        Parent root = loader.load();

        TaskFormController tfControl = loader.getController();
        tfControl.setManager(manager);
        tfControl.setProjectInfoController(this);
        tfControl.setSelectedProject(projectID);

        Scene scene = new Scene(root);
        Stage manageTasks = new Stage();
        manageTasks.setTitle("Add task");
        manageTasks.setScene(scene);
        manageTasks.initOwner(projectTitle.getScene().getWindow());
        manageTasks.initModality(Modality.APPLICATION_MODAL);
        manageTasks.showAndWait();
        projectsController.refillTable();
        centerWindowOnScreen(manageTasks);
    }

    public void loadNewTaskFormToEdit() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ProjectForms/TaskForms/TaskForm.fxml"));
        Parent root = loader.load();

        TaskFormController tfControl = loader.getController();
        tfControl.setManager(manager);
        tfControl.setProjectInfoController(this);
        tfControl.setSelectedProject(projectID);

        if(selectedTask != null) {
            if(!selectedTask.isDone())
                tfControl.setSelectedTask(this.selectedTask);
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Cannot edit task!");
                alert.setHeaderText(null);
                alert.setContentText("This task is already completed!");
                alert.showAndWait();
                return;
            }
        }

        Scene scene = new Scene(root);
        Stage manageTasks = new Stage();
        manageTasks.setTitle("Add task");
        manageTasks.setScene(scene);
        manageTasks.initOwner(projectTitle.getScene().getWindow());
        manageTasks.initModality(Modality.APPLICATION_MODAL);
        manageTasks.showAndWait();
        projectsController.refillTable();
        centerWindowOnScreen(manageTasks);
    }

    public void completeTask(){
        try {
            String completedTitle = selectedTask.getTitle() + " - COMPLETED";
            manager.editTaskInfo(selectedTask.getId(), completedTitle);
            manager.completeTask(selectedTask.getId());
            showProjectTasks(manager.getProjectById(projectID, true));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Tokio tasko nera");;
        }
    }

    private void showProjectMembers(Project project) {
        ObservableList<User> users = FXCollections.observableArrayList(project.getMembers());
        projectMembers.setItems(users);
    }

    private void createProjectTasksTree(TreeItem<Task> parentTask, Task subtask){
        TreeItem<Task> childTask = new TreeItem<>(subtask);
        parentTask.getChildren().add(childTask);

        for(Task task : subtask.getSubTasks())
            createProjectTasksTree(childTask, task);
    }

    public void showProjectTasks(Project project) throws Exception {
        TreeItem<Task> root = new TreeItem<>(new Task(0, "Tasks", null, null));
        projectTasks.setShowRoot(false);

        for(Task task : manager.getProjectTasks(project.getId())) {
            System.out.println(task.getSubTasks().size());
            if(task.isSubtask())
                createProjectTasksTree(root, task);
        }

        projectTasks.setRoot(root);
    }

    public void selectTask(){
        try {
            selectedTask = projectTasks.getSelectionModel().getSelectedItem().getValue();
        } catch (Exception e){
            System.out.println("Not a valid selection.");
        }
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
