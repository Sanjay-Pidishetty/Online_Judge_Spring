package com.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.model.Problem;

public interface ProblemRepository extends MongoRepository<Problem, String>{

}
