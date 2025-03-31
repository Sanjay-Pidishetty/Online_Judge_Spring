package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.Problem;
import com.service.ProblemService;

@RestController
@RequestMapping("/problem")
public class ProblemController {
	@Autowired
	private ProblemService problemService;
	
	@PostMapping("/add")
	public ResponseEntity<String> createProblem(@RequestBody Problem problem) {
		try {
            // Assume the UserService handles the registration logic
            boolean isAdded = problemService.addProblem(problem);

            if (isAdded) {
                return ResponseEntity.ok("Problem added successful.");
            } else {
                return ResponseEntity.status(400).body("Adding new problem failed. Please try again.");
            }
        } catch (Exception e) {
            // Handle any errors that occur during registration
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
	}
}
