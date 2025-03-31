package com.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	User findByUserName(String username);
}
