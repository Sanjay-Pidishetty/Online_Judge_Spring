package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Problem;
import com.repository.ProblemRepository;

@Service
public class ProblemService {
	@Autowired
	ProblemRepository problemRepository;
	
	public boolean addProblem(Problem problem){
		problemRepository.save(problem);
		return true;
	}
}
