package com.example.demo.UserRepository;

import com.example.demo.Entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CachingRepository extends MongoRepository<User,Long> {
}