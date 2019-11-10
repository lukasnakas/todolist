package ds;

import javafx.scene.control.TreeItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Task implements Serializable {
		private int id;
		private static int idCounter = 1;
		private String title;
		private Date createdOn, completedOn, editedOn;
		private User createdBy, completedBy, editedBy;
		private boolean done = false;
		private ArrayList<Task> subTasks = new ArrayList<>();
		private Project project;
		boolean isSubtask = true;
		
		public Task(int id, String title, Project project, User createdBy) {
			this.title = title;
			this.createdBy = createdBy;
			this.createdOn = new Date();
			this.project = project;
			this.id = id;
		}

		public ArrayList<Task> getAllSubTasks(){
			ArrayList<Task> allSubTasks = new ArrayList<>();
			for(Task subTask : this.subTasks) {
				allSubTasks.add(subTask);
				allSubTasks.addAll(subTask.getAllSubTasks());
			}
			return allSubTasks;
		}
		
		public void addTask(Task newTask) {
			subTasks.add(newTask);
		}

		@Override
		public String toString() {
			return title;
		}

	public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Date getCompletedOn() {
			return completedOn;
		}

		public void setCompletedOn(Date completedOn) {
			this.completedOn = completedOn;
		}

		public User getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(User createdBy) {
			this.createdBy = createdBy;
		}

		public User getCompletedBy() {
			return completedBy;
		}

		public void setCompletedBy(User completedBy) {
			this.completedBy = completedBy;
		}

		public boolean isDone() {
			return done;
		}

		public void setDone(boolean done) {
			this.done = done;
		}

		public ArrayList<Task> getSubTasks() {
			return subTasks;
		}

		public void setSubTasks(ArrayList<Task> subTasks) {
			this.subTasks = subTasks;
		}

		public Project getProject() {
			return project;
		}

		public void setProject(Project project) {
			this.project = project;
		}

		public int getId() {
			return id;
		}

		public void setId(int id){
			this.id = id;
		}

		public Date getCreatedOn() {
			return createdOn;
		}

		public Date getEditedOn() {
			return editedOn;
		}

		public void setEditedOn(Date editedOn) {
			this.editedOn = editedOn;
		}

		public User getEditedBy() {
			return editedBy;
		}

		public void setEditedBy(User editedBy) {
			this.editedBy = editedBy;
		}

		public static void setIdCounter(int idCounter) {
			Task.idCounter = idCounter;
		}

		public void setCreatedOn(Date createdOn) {
			this.createdOn = createdOn;
		}

	public static int getIdCounter() {
		return idCounter;
	}

	public void setSubtask(boolean subtask) {
		isSubtask = subtask;
	}

	public boolean isSubtask() {
		return isSubtask;
	}
}
