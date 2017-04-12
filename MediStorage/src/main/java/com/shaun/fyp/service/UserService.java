package com.shaun.fyp.service;

import com.shaun.fyp.dao.UserDao;
import com.shaun.fyp.model.User;

/*
 * This is the user service layer.
 * This adds an extra layer between the UserResource & UserDao.
 * Provides a better separation of concerns
 */
public class UserService {

	UserDao userDao = UserDao.getInstance();

	public User getUserByUserName(String userName) {
		return userDao.getUserByUserName(userName);
	}

	public User addNewUser(User user) {
		return userDao.addUser(user);
	}

}

