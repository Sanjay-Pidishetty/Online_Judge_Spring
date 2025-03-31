package com.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.model.User;
import com.service.CodeExecutionService;
import com.service.UserService;

@RestController
@RequestMapping("/api")
public class CodeController {

    @Autowired
    private CodeExecutionService codeExecutionService;

    @PostMapping("/execute")
    public ResponseEntity<String> executeCode(@RequestBody Map<String, Object> requestBody) {
        try {
            // Extract the parameters from the map
            String language = (String) requestBody.get("language");
            String code = (String) requestBody.get("code");

            // Execute the code using the service
            String result = codeExecutionService.executeCode(language, code);

            // Return the result of code execution
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Handle any errors that occur during execution
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
