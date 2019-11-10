package ds;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
	private int id;
	private static int idCounter = 1;
	private String login, pass, email, userType;
	private boolean active = true;
	private ArrayList<Project> projects = new ArrayList<Project>();
	
	public User(int id, String login, String pass, String email) {
		this.login = login;
		this.pass = pass;
		this.email = email;
		this.id = id;
	}
	
	public void addProject(Project project) {
		if(!projects.contains(project))
			projects.add(project);
	}
	
	public void removeProject(Project project) {
		projects.remove(project);
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", pass=" + pass + ", email=" + email + ", active=" + active
				+ "]";
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getId() {
		return id;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public ArrayList<Project> getProjects() {
		return projects;
	}

	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}

	public static void setIdCounter(int idCounter) {
		User.idCounter = idCounter;
	}
	
}
