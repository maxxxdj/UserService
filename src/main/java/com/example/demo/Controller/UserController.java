package com.example.demo.Controller;

import com.example.demo.Entities.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping("/createUser")
    public String createUser(User user) {
        repository.save(user);
        return String.format("User %s created.", user.getUsername());
    }

    @GetMapping("/getUsers")
    public List<User> getUsers() {
        return repository.findAll();
    }

}