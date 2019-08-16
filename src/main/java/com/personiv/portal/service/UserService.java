package com.personiv.portal.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personiv.portal.dao.UserDao;
import com.personiv.portal.model.PlainUser;
import com.personiv.portal.model.User;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	
	

	public User getUserById(Long id) {
		return userDao.findUserById(id);
	}
	public User getUserByUsername(String username) {
		return userDao.getUserByUsername(username);
	}
	
	
	public List<User> getUsers() {
		return userDao.getUsers();
	}
	
	public void updatePassword(String username, String password) {
		userDao.updatePassword(username, password);
	}
	
	public List<User> getUserByRole(String role){
		return userDao.getUserByRole(role);
	}
	
	public void addUser(User user) {
		userDao.addUser(user);
	}
	
	public void disableUser(User user) {
		userDao.disableUser(user);
	}
	
	public void enableUser(User user) {
		userDao.enableUser(user);
	}
	
	public void resetPassword(User user) {
		userDao.resetPassword(user);
	}
	public void updatePassword(PlainUser user) {
		userDao.updatePassword(user.getUsername(), user.getPassword());
		
	}
	public void updateUser(User user) {
		userDao.updateUser(user);
		
	}
	
}
