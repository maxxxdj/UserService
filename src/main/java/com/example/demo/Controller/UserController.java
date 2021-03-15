package com.example.demo.Controller;

import com.example.demo.Entities.User;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        return userService.save(user);

    }

    @RequestMapping(value = "/users/{name}",method = RequestMethod.GET)
    @Query("select name from users where name=?1")
    public List<User> getUserByName(@PathVariable String name) {
        return userService.getUsersByName(name);
    }


    @GetMapping("/getUsers")
    public List<User> getUsers() {
        return userService.findAll();
    }

}