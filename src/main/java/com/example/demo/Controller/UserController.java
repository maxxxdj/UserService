package com.example.demo.Controller;

import com.example.demo.Entities.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository repository;


    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        return repository.save(user);
    }

    @RequestMapping(value = "/users/{name}",method = RequestMethod.GET)
    @Query("select name from users where name=?1")
    public List<User> getUserByName(@PathVariable String name) {
        return repository.getUsersByName(name);
    }


    @GetMapping("/getUsers")
    public List<User> getUsers() {
        return repository.findAll();
    }

}