package ds;

import javafx.scene.control.TreeItem;

import java.io.Serializable;
import java.util.ArrayList;

public class Project extends TreeItem<Project> implements Serializable
{
	private int id;
	private static int idCounter = 1;
	private String title;
	private ArrayList<User> members = new ArrayList<>();
	private ArrayList<Task> tasks = new ArrayList<>();
	
	public Project(int id, String title, User creator) {
		this.title = title;
		members.add(creator);
		this.id = id;
	}

	public Project(int id, String title) {
		this.title = title;
		this.id = id;
	}

	public void addMember(User userToAdd) {
		if(!members.contains(userToAdd))
			members.add(userToAdd);
	}
	
	public void addTask(Task taskToAdd) {
		tasks.add(taskToAdd);
	}
	
	public ArrayList<Task> getAllTasks(){
		ArrayList<Task> allTasks = new ArrayList<Task>();
		allTasks.addAll(this.tasks);
		for(Task task : this.tasks)
			allTasks.addAll(task.getAllSubTasks());
		return allTasks;
	}
	
	@Override
	public String toString() {
		return title.charAt(0) + title.substring(1, title.length());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<User> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<User> members) {
		this.members = members;
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	public int getId() {
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public static void setIdCounter(int idCounter) {
		Project.idCounter = idCounter;
	}
	
}
