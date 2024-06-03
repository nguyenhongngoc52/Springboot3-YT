package com.example.springboot3youtube.service;

import com.example.springboot3youtube.request.UserUpdateRequest;
import com.example.springboot3youtube.entity.User;
import com.example.springboot3youtube.request.UserCreationRequest;

import java.util.List;

public interface UserService {
    User createUser(UserCreationRequest userCreationRequest);
    List<User> getUsers();

    User getUserById(String userId);

    User updateUser(String userId  , UserUpdateRequest userUpdateRequest);
}
