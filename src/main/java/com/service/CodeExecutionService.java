package com.service;

import org.springframework.stereotype.Service;
import java.io.*;

@Service
public class CodeExecutionService {

    public String executeCode(String language, String code) throws Exception {
        if (language.equalsIgnoreCase("java")) {
            return executeJavaCode(code);
        } else if (language.equalsIgnoreCase("cpp")) {
            return executeCppCode(code);
        }
        throw new IllegalArgumentException("Unsupported language: " + language);
    }

    private String executeJavaCode(String code) throws IOException {
        File file = new File("Solution.java");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(code);
        writer.close();

        // Compile Java code
        Process compileProcess = new ProcessBuilder("javac", "Solution.java").start();
        try {
            compileProcess.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException("Compilation interrupted", e);
        }

        if (compileProcess.exitValue() != 0) {
            return "Compilation failed.";
        }

        // Execute Java code
        Process executeProcess = new ProcessBuilder("java", "Solution").start();
        return getOutput(executeProcess);
    }

    private String executeCppCode(String code) throws IOException {
        File file = new File("Solution.cpp");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(code);
        writer.close();

        // Compile C++ code
        Process compileProcess = new ProcessBuilder("g++", "Solution.cpp", "-o", "Solution").start();
        try {
            compileProcess.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException("Compilation interrupted", e);
        }

        if (compileProcess.exitValue() != 0) {
            return "Compilation failed.";
        }

        // Execute C++ code
        Process executeProcess = new ProcessBuilder("./Solution").start();
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
