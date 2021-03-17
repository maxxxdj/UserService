package com.example.demo.Controller;

import com.example.demo.Entities.User;
import com.example.demo.Service.CachingServer;
import com.example.demo.UserRepository.CachingRepository;
import com.example.demo.Service.UserService;
import com.example.demo.config.KafkaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
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
    private CachingServer cachingServer;

    //kafka queue
    private final KafkaTemplate<String, User> kafkaTemplate =
            new KafkaTemplate<>(KafkaConfig.producerFactory());
    private static final String TOPIC = "USERS";

    @KafkaListener(topics = "USERS", groupId = "group_id")
    public void consumerListen(User user) {
        cachingServer.save(user);
    }

    //testing endpoint
    @GetMapping("/kafka/produce")
    public void produce() {
        User user1 = new User(2L, "Venci_22", "Vencislav", "venci+parola1");
        kafkaTemplate.send(TOPIC, user1);

    }

    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        kafkaTemplate.send(TOPIC, user);
        consumerListen(user);
        return userService.save(user);
    }

    @RequestMapping(value = "/users/{name}", method = RequestMethod.GET)
    @Query("select name from users where name=?1")
    public List<User> getUserByName(@PathVariable String name) {
        return userService.getUsersByName(name);
    }


    @GetMapping("/getUsers")
    public List<User> getUsers() {
        return cachingServer.findAll();
    }

}

