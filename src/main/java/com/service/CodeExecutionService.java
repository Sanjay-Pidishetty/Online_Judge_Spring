package com.service;

import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

@Service
public class CodeExecutionService {
	
	private final String targetDir = "code_output";
	private static final int MAX_OUTPUT_LINES = 100;
	private static final int MAX_OUTPUT_LENGTH = 10000;
	private static final long EXECUTION_TIMEOUT_SECONDS = 5; 

    public String executeCode(String language, String code, String input) throws Exception {
    	Path directoryPath = Paths.get(targetDir);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);  // Create the directory if it doesn't exist
        }
        if (language.equalsIgnoreCase("java")) {
            return executeJavaCode(code, input);
        } else if (language.equalsIgnoreCase("cpp")) {
            return executeCppCode(code);
        }
        throw new IllegalArgumentException("Unsupported language: " + language);
    }

    private String executeJavaCode(String code, String input) throws IOException {
        File file = new File(targetDir +"/Solution.java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(code);
        }

        // Compile Java code
        Process compileProcess = new ProcessBuilder("javac", targetDir + "/Solution.java").start();
        try {
            if (!compileProcess.waitFor(10, TimeUnit.SECONDS)) {
                compileProcess.destroy();
                return "Compilation timeout.";
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Compilation interrupted", e);
        }

        if (compileProcess.exitValue() != 0) {
            return "Compilation failed.";
        }

        // Execute Java code
        Process executeProcess = new ProcessBuilder("java", "-cp", targetDir, "Solution").start();
        
        // Provide input to the process
        if (input != null && !input.isEmpty()) {
            try (BufferedWriter processWriter = new BufferedWriter(new OutputStreamWriter(executeProcess.getOutputStream(), StandardCharsets.UTF_8))) {
                processWriter.write(input);
                processWriter.flush();
            }
        }
        
        return getOutput(executeProcess);
    }

    private String executeCppCode(String code) throws IOException {
        File file = new File(targetDir + "/Solution.cpp");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(code);
        }

        // Compile C++ code
        Process compileProcess = new ProcessBuilder("g++", targetDir + "/Solution.cpp", "-o", targetDir + "/Solution").start();
        try {
            if (!compileProcess.waitFor(10, TimeUnit.SECONDS)) {
                compileProcess.destroy();
                return "Compilation timeout.";
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Compilation interrupted", e);
        }

        if (compileProcess.exitValue() != 0) {
            return "Compilation failed.";
        }

        // Execute C++ code
        Process executeProcess = new ProcessBuilder(targetDir + "/Solution").start();
        return getOutput(executeProcess);
    }

    private String getOutput(Process process) throws IOException {
        StringBuilder output = new StringBuilder();
        int lineCount = 0;
        boolean timedOut = false;
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null && lineCount < MAX_OUTPUT_LINES) {
                if (output.length() + line.length() > MAX_OUTPUT_LENGTH) {
                    output.append("\n[Output truncated - exceeded maximum length]");
                    break;
                }
                output.append(line).append("\n");
                lineCount++;
            }
            
            if (lineCount >= MAX_OUTPUT_LINES) {
                output.append("[Output truncated - exceeded maximum lines]");
            }
        } finally {
            try {
                if (!process.waitFor(EXECUTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    process.destroy();
                    timedOut = true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                process.destroy();
            }
        }
        
        if (timedOut) {
            return "Execution timeout exceeded.";
        }
        
        return output.toString();
    }
}
