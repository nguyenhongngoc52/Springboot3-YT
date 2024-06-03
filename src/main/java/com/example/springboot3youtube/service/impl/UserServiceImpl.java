package com.example.springboot3youtube.service.impl;

import com.example.springboot3youtube.request.UserUpdateRequest;
import com.example.springboot3youtube.entity.User;
import com.example.springboot3youtube.repository.UserRepository;
import com.example.springboot3youtube.request.UserCreationRequest;
import com.example.springboot3youtube.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(UserCreationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String userId) {
        Integer id = Integer.valueOf(userId);
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("user not found"));
    }

    @Override
    public User updateUser(String userId , UserUpdateRequest request) {
        User user = getUserById(userId);
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        return userRepository.save(user);
    }
}
