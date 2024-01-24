package com.zero.weightTracker.controller;

import com.zero.weightTracker.entity.User;
import com.zero.weightTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/private")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/allUsers")
    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

}
