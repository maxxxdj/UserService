package com.example.demo.Controller;

import com.example.demo.Entities.User;
import com.example.demo.Exceptions.EmptyDBException;
import com.example.demo.Exceptions.UserNotFoundException;
import com.example.demo.Service.CachingService;
import com.example.demo.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    //SFL4J
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private CachingService cachingService;

    //kafka queue
    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;
    private static final String TOPIC = "USERS";

    @KafkaListener(topics = "USERS", groupId = "group_id")
    public void listen(String message) {
        try {
            User user = new ObjectMapper().readValue(message, User.class);
            logger.info("New message has been read from Kafka successfully!");
            cachingService.save(user);
            logger.info("New user with name {} saved into Mongo DB!", user.getName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @PostMapping("/createUser")
    public String createUser(@RequestBody User user) {
        kafkaTemplate.send(TOPIC, user);
        logger.info("User {} has been sent successfully to Kafka topic: {}",user.getName(),TOPIC);
        userService.save(user);
        logger.info("User with name {} was saved into the MySQL DB!", user.getName());

        return String.format("User with name %s was saved into the MySQL database.", user.getName());
    }

    @RequestMapping(value = "/users/{name}", method = RequestMethod.GET)
    //@Query("select name from users where name=?1")
    public List<User> getUserByName(@PathVariable String name) {
        List<User> usersByName = userService.getUsersByName(name);
        if (usersByName.isEmpty()) {
            logger.error("Database is empty!");
            throw new UserNotFoundException(name);
        } else {
            logger.info("GetUsersByName request has been returned successfully!");
            return usersByName;
        }
    }

    @GetMapping("/getAllUsers")
    public List<User> getUsers() {
        if (cachingService.findAll().isEmpty()) {
            logger.error("Database is empty!");
            throw new EmptyDBException();
        } else {
            logger.info("GetUsers from MongoDB request has been returned successfully!");
            return cachingService.findAll();
        }
    }

    @DeleteMapping("/deleteAllUsers")
    public String deleteAll() {
        userService.deleteAll();
        cachingService.deleteAll();
        logger.info("Databases are now empty!");
        return "Flush! :)";
    }


}

