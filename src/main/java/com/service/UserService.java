package com.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.model.User;
import com.repository.UserRepository;

@Service
public class UserService {
	
	static BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
	
	@Autowired
	private UserRepository userRepository;
	
	public boolean registerUser(User user){
		if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            return false; // User already exists
        }
		user.setPassword(getPasswordHash(user.getPassword()));
		user.setCreatedDate(new Date());
		User createdUser = userRepository.save(user);
		return (createdUser != null) ? true : false;
	}
	
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	public Optional<User> getUserById(String id){
		return userRepository.findById(id);
	}
	
	public Optional<User> getUserByUserName(String userName){
		return userRepository.findByUserName(userName);
	}
	
	public boolean updateUser(String id, User updatedUser){
		Optional<User> user = getUserById(id);
		if(user.isPresent()) {
			updatedUser.setId(id);
			userRepository.save(updatedUser);
			return true;
		}		
		else {
			return false;
		}
	}
	
	public boolean deleteUser(String id) {
		Optional<User> user = getUserById(id);
		if(user.isPresent()) {
			userRepository.deleteById(id);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean authenticateUser(String userName, String actualPassword){
		Optional<User> user = userRepository.findByUserName(userName);
		return user.isPresent() && encoder.matches(actualPassword, user.get().getPassword());
	}
	
	public String getPasswordHash(String password) {
		return encoder.encode(password);
	}
}
