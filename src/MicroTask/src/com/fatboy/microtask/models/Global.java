package com.fatboy.microtask.models;

import java.util.List;

import android.app.Application;

public class Global extends Application {
	
	private List<User> users;
	private User currentUser;
	
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public User getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

}
