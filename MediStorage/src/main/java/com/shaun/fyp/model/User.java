package com.shaun.fyp.model;

import javax.persistence.Id;

/*
 * This is the User POJO
 */
public class User {
	
	@Id
	private String _id;
	private String fullname;
	private String userName;
	private String password;
	private String title;
	
	public User() { }

	public User(String _id, String fullname, String userName, String password, String title) {
		this._id = _id;
		this.fullname = fullname;
		this.userName = userName;
		this.password = password;
		this.title = title;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}	

}
