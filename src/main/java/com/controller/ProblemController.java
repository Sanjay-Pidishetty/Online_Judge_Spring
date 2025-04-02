package com.controller;

import java.util.List;
import java.util.Optional;

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

import com.model.Problem;
import com.service.ProblemService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/problem")
@Api(tags = "Problem Management", description = "Operations related to Problems")
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
	
	@GetMapping("/getAll")
	public List<Problem> getAllProblems(){
		return problemService.getAllProblems();
	}
	
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProblem(@PathVariable String id) {
        boolean success = problemService.deleteById(id);
        if (success) {
            return ResponseEntity.ok("Problem deleted successfully!");
        } else {
            return ResponseEntity.status(404).body("Problem not found.");
        }
    }
	
	@GetMapping("/problem/{id}")
	public ResponseEntity<Problem> getProblemById(@PathVariable String id) {
		Optional<Problem> problem = problemService.getProblemById(id);
		if (problem.isPresent()) {
            return ResponseEntity.ok(problem.get());
        } else {
            return ResponseEntity.status(404).body(null);  // 404 Not Found
        }
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> updateProblem(@PathVariable String id,@RequestBody Problem updatedProblem) {
		boolean success = problemService.updateProblem(id, updatedProblem);
		if (success) {
            return ResponseEntity.ok("Problem updated successfully!");
        } else {
            return ResponseEntity.status(404).body("Problem not found to update.");
        }
	}
}
