package ds;

import java.io.Serializable;

public class Person extends User implements Serializable {
	private String firstName, lastName;
	
	public Person(int id, String login, String pass, String firstName, String lastName, String email) {
		super(id, login, pass, email);
		this.firstName = firstName;
		this.lastName = lastName;
		setUserType("Person");
	}
	
	@Override
	public String toString() {
		return firstName + " " + lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}
