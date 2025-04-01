package com.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.User;
import com.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
    private UserService userService;
	
	@PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            // Assume the UserService handles the registration logic
            boolean isRegistered = userService.registerUser(user);

            if (isRegistered) {
                return ResponseEntity.ok("Registration successful.");
            } else {
                return ResponseEntity.status(400).body("Registration failed. Please try again.");
            }
        } catch (Exception e) {
            // Handle any errors that occur during registration
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
	
	@PostMapping("/login")
	public String login(@RequestBody Map<String, String> request){
		String userName = request.get("username");
		String password = request.get("password");
		
		if(userName == null || password == null) {
			return "Invalid input";
		}
		
		boolean authenticated = userService.authenticateUser(userName, password);
        return authenticated ? "Login successful" : "Invalid username or password";
	}
	
	@GetMapping("/getAll")
	public List<User> getAllUsers(){
		return userService.getAllUsers();
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable String id){
		boolean success = userService.deleteUser(id);
		if(success) {
			return ResponseEntity.ok("User deleted successfully!");
		}
		else {
			return ResponseEntity.status(404).body("Failed to delete user");
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateUser(@PathVariable String id,@RequestBody User updatedUser){
		boolean success = userService.updateUser(id, updatedUser);
		if(success) {
			return ResponseEntity.ok("User updated successfully");
		}
		else {
			return ResponseEntity.status(404).body("Failed to update user");
		}
	}
}
