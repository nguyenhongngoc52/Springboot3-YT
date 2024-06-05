package com.example.springboot3youtube.controller;

import com.example.springboot3youtube.dto.request.ApiRespone;
import com.example.springboot3youtube.dto.request.UserUpdateRequest;
import com.example.springboot3youtube.dto.respone.UserRespone;
import com.example.springboot3youtube.entity.User;
import com.example.springboot3youtube.dto.request.UserCreationRequest;
import com.example.springboot3youtube.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {
    private UserService userService;

    @PostMapping
    ApiRespone<User> createUser(@RequestBody @Valid UserCreationRequest userCreationRequest){
        ApiRespone<User> result = new ApiRespone<>();
        result.setResult(userService.createUser(userCreationRequest));
       return result;
    }

    @GetMapping
    List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    UserRespone getUser(@PathVariable("userId") String userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}")
    UserRespone updateUser(
            @PathVariable("userId") String userId,
            @RequestBody UserUpdateRequest request){
        return userService.updateUser(userId,request);
    }

}
