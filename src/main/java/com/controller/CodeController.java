package com.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.service.CodeExecutionService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api")
@Api(tags = "Code Management", description = "Operations related to code execution")
public class CodeController {

    @Autowired
    private CodeExecutionService codeExecutionService;

    private static final Set<String> ALLOWED_LANGUAGES;
    private static final int MAX_CODE_LENGTH = 50000;
    private static final int MAX_INPUT_LENGTH = 10000;
    private static final Pattern CONTROL_CHARS = Pattern.compile("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]");
    private static final Map<String, Pattern> DANGEROUS_PATTERNS;
    
    static {
        Set<String> languages = new HashSet<>();
        languages.add("java");
        languages.add("cpp");
        ALLOWED_LANGUAGES = Collections.unmodifiableSet(languages);
        
        Map<String, Pattern> patterns = new HashMap<>();
        patterns.put("java", Pattern.compile(
            ".*(Runtime\\.getRuntime|ProcessBuilder|System\\.exit|File\\(|FileWriter|FileReader|" +
            "FileInputStream|FileOutputStream|Socket|ServerSocket|exec\\(|deleteOnExit|" +
            "java\\.lang\\.reflect|ClassLoader|URLClassLoader).*",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        ));
        patterns.put("cpp", Pattern.compile(
            ".*(system\\s*\\(|popen|exec[lv]|fork|remove\\s*\\(|unlink|chmod|chown|" +
            "fstream|ofstream|ifstream|<fstream>|<filesystem>).*",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        ));
        DANGEROUS_PATTERNS = Collections.unmodifiableMap(patterns);
    }

    @PostMapping("/execute")
    public ResponseEntity<String> executeCode(@RequestBody Map<String, Object> requestBody) {
        try {
            String language = validateLanguage(String.valueOf(requestBody.get("language")));
            String code = validateCode(String.valueOf(requestBody.get("code")), language);
            String input = sanitizeInput(requestBody.get("input") != null ? String.valueOf(requestBody.get("input")) : null);

            String result = codeExecutionService.executeCode(language, code, input);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Execution failed");
        }
    }

    private String validateLanguage(String language) {
        if (language == null || language.equals("null") || !ALLOWED_LANGUAGES.contains(language.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Invalid language. Allowed: java, cpp");
        }
        return language.toLowerCase(Locale.ROOT);
    }

    private String validateCode(String code, String language) {
        if (code == null || code.equals("null") || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Code is required");
        }
        if (code.length() > MAX_CODE_LENGTH) {
            throw new IllegalArgumentException("Code exceeds maximum length of " + MAX_CODE_LENGTH);
        }
        
        Pattern pattern = DANGEROUS_PATTERNS.get(language);
        if (pattern != null && pattern.matcher(code).matches()) {
            throw new IllegalArgumentException("Code contains prohibited operations");
        }
        return code;
    }

    private String sanitizeInput(String input) {
        if (input == null) return null;
        if (input.length() > MAX_INPUT_LENGTH) {
            throw new IllegalArgumentException("Input exceeds maximum length of " + MAX_INPUT_LENGTH);
        }
        return CONTROL_CHARS.matcher(input).replaceAll("");
    }
}
