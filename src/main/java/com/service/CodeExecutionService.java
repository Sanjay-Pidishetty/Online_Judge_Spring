package com.service;

import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.*;

@Service
public class CodeExecutionService {
	
	private final String targetDir = "code_output"; 

    public String executeCode(String language, String code) throws Exception {
    	Path directoryPath = Paths.get(targetDir);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);  // Create the directory if it doesn't exist
        }
        if (language.equalsIgnoreCase("java")) {
            return executeJavaCode(code);
        } else if (language.equalsIgnoreCase("cpp")) {
            return executeCppCode(code);
        }
        throw new IllegalArgumentException("Unsupported language: " + language);
    }

    private String executeJavaCode(String code) throws IOException {
        File file = new File(targetDir +"/Solution.java");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(code);
        writer.close();

        // Compile Java code
        Process compileProcess = new ProcessBuilder("javac", targetDir + "/Solution.java").start();
        try {
            compileProcess.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException("Compilation interrupted", e);
        }

        if (compileProcess.exitValue() != 0) {
            return "Compilation failed.";
        }

        // Execute Java code
        Process executeProcess = new ProcessBuilder("java", "-cp", targetDir, "Solution").start();
        return getOutput(executeProcess);
    }

    private String executeCppCode(String code) throws IOException {
        File file = new File(targetDir + "/Solution.cpp");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(code);
        writer.close();

        // Compile C++ code
        Process compileProcess = new ProcessBuilder("g++", targetDir + "/Solution.cpp", "-o", targetDir + "/Solution").start();
        try {
            compileProcess.waitFor();
        } catch (InterruptedException e) {
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        return output.toString();
    }
}
