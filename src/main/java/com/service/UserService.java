package com.service;

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
}
