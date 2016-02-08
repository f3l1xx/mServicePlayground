package de.fb;

import java.util.UUID;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

public class User {

	private UUID id;
	
	@Length(min=3,max=50)
	private String firstName;
	
	@Length(min=3,max=50)
	private String lastName;
	
	@Email
	private String email;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
