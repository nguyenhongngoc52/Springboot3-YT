package com.example.springboot3youtube.service;

import com.example.springboot3youtube.dto.request.UserUpdateRequest;
import com.example.springboot3youtube.dto.respone.UserRespone;
import com.example.springboot3youtube.entity.User;
import com.example.springboot3youtube.dto.request.UserCreationRequest;

import java.util.List;

public interface UserService {
    User createUser(UserCreationRequest userCreationRequest);
    List<User> getUsers();

    UserRespone getUserById(String userId);

    UserRespone updateUser(String userId  , UserUpdateRequest userUpdateRequest);
}
