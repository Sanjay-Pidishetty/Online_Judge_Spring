package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.config.CustomUserDetails;
import com.model.AuthRequest;
import com.model.AuthResponse;
import com.model.User;
import com.security.JwtUtil;
import com.service.UserService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/user")
@Api(tags = "User Management", description = "Operations related to Users")
public class UserController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
    private JwtUtil jwtUtil;
	
	@Autowired
    private UserService userService;
	
	@PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            boolean isRegistered = userService.registerUser(user);

            if (isRegistered) {
                return ResponseEntity.ok("Registration successful.");
            } else {
                return ResponseEntity.status(400).body("Registration failed. Please try again.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Registration failed");
        }
    }
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
		try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            List<String> roles = ((CustomUserDetails) authentication.getPrincipal()).getRoles();

            String token = jwtUtil.generateToken(authRequest.getUsername(), roles);

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid username or password.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Login failed");
        }
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
