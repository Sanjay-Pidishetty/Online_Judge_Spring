package com.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
	@Id
	private String id;

	private String userName;

	private String email;

	private String password;

	private Date createdDate;

	private String role;

	private int solvedProblems;
	
	public User() {
		
	}

	public User(String userName, String email, String password, String role) {
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public User(String userName, String email, String password, Date createdDate, String role, int solvedProblems) {
		super();
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.createdDate = createdDate;
		this.role = role;
		this.solvedProblems = solvedProblems;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
