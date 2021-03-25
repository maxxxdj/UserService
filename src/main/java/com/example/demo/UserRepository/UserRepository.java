package com.example.demo.UserRepository;

import com.example.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query
    public List<User> getUsersByName(String name);
    public List<User> getUserByPassword(String password);
}