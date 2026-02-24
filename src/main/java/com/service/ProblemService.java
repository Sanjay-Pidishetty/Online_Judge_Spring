package com.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.model.Problem;
import com.repository.ProblemRepository;

@Service
public class ProblemService {
	private static final Logger logger = LoggerFactory.getLogger(ProblemService.class);
	
	@Autowired
	ProblemRepository problemRepository;
	
	public boolean addProblem(Problem problem){
		try {
			problemRepository.save(problem);
			return true;
		} catch (IllegalArgumentException e) {
			logger.error("Invalid problem data: {}", e.getMessage());
			return false;
		} catch (DataAccessException e) {
			logger.error("Database error while adding problem: {}", e.getMessage());
			return false;
		}
	}
	
	public List<Problem> getAllProblems(){
		return problemRepository.findAll();
	}
	
	public boolean deleteById(String id) {
		try {
			problemRepository.deleteById(id);
			return true;
		}
		catch(EmptyResultDataAccessException e) {
			return false;
		}
	}
	
	public Optional<Problem> getProblemById(String id) {
        return problemRepository.findById(id);
    }
	
	public boolean updateProblem(String id, Problem updatedProblem) {
        Optional<Problem> existingProblem = problemRepository.findById(id);
        if (existingProblem.isPresent()) {
            updatedProblem.setId(id); // Ensure the ID is retained
            problemRepository.save(updatedProblem);
            return true;
        }
        return false;
    }
}
