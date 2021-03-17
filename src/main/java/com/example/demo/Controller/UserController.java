package com.example.demo.Controller;

import com.example.demo.Entities.User;
import com.example.demo.Service.CachingService;
import com.example.demo.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CachingService cachingService;

    //kafka queue
    @Autowired
    private KafkaTemplate<String,User> kafkaTemplate;
    private static final String TOPIC = "USERS";

    @KafkaListener(topics = "USERS",groupId = "group_id")
    public void listen(String message){
        try {
            User user = new ObjectMapper().readValue(message, User.class);
            cachingService.save(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        kafkaTemplate.send(TOPIC,user);
        return userService.save(user);
    }

    @RequestMapping(value = "/users/{name}", method = RequestMethod.GET)
    @Query("select name from users where name=?1")
    public List<User> getUserByName(@PathVariable String name) {
        return userService.getUsersByName(name);
    }


    @GetMapping("/getUsers")
    public List<User> getUsers() {
        return cachingService.findAll();
    }

}

