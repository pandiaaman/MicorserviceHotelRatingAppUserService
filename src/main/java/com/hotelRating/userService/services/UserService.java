package com.hotelRating.userService.services;

import java.util.List;

import com.hotelRating.userService.models.User;

public interface UserService {

	//save one user
	public User saveUser(User user);
	//get all users
	public List<User> getAllUsers();
	//get one user using userid
	public User getUserById(String userId);
	//delete user
	public boolean deleteUser(User user);
	//update User
	public User updateUser(User user);
}
