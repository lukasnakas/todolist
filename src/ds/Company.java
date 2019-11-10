package ds;

import java.io.Serializable;

public class Company extends User implements Serializable {
	private String title;

	public Company(int id, String login, String pass, String title, String email) {
		super(id, login, pass, email);
		this.title = title;
		setUserType("Company");
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
	
}
