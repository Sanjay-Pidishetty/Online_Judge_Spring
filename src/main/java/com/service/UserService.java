package com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.User;
import com.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public boolean registerUser(User user){
		if (userRepository.findByUserName(user.getUserName()) != null) {
            return false; // User already exists
        }
        userRepository.save(user);
        return true;
	}
	
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	public Optional<User> getUser(String id){
		return userRepository.findById(id);
	}
	
	public boolean updateUser(String id, User updatedUser){
		Optional<User> user = getUser(id);
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
		Optional<User> user = getUser(id);
		if(user.isPresent()) {
			userRepository.deleteById(id);
			return true;
		}
		else {
			return false;
		}
	}
}
