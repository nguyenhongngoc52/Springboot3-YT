package com.example.springboot3youtube.controller;

import com.example.springboot3youtube.request.UserUpdateRequest;
import com.example.springboot3youtube.entity.User;
import com.example.springboot3youtube.request.UserCreationRequest;
import com.example.springboot3youtube.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping()
    User createUser(@RequestBody UserCreationRequest userCreationRequest){
       return userService.createUser(userCreationRequest);
    }

    @GetMapping
    List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable("userId") String userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    User updateUser(
            @PathVariable("userId") String userId,
            @RequestBody UserUpdateRequest request){
        return userService.updateUser(userId,request);
    }

}
