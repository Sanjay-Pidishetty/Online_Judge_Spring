package com.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;

@Document(collection = "users")
public class User {
	@Id
	private String id;
	
	@NotNull
	private String userName;
	
	@NotNull
	private String email;
	
	private String password;
	
	private int solvedProblems;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getSolvedProblems() {
		return solvedProblems;
	}

	public void setSolvedProblems(int solvedProblems) {
		this.solvedProblems = solvedProblems;
	}
}
