package com.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findByUserName(String username);
}
