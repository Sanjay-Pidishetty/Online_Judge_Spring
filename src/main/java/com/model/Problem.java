package com.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;



@Document(collection = "problems")
public class Problem {
	@Id
	private String id;
	
	@NotNull
	private String title;
	
	@NotNull
	private String description;
	
	@NotNull
	private String language;
	
	private String input;
	private String output;
	private String difficulty;
	private String constraints;
	private int timeLimit;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
}
