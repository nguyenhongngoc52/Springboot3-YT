package com.example.springboot3youtube.service.impl;

import com.example.springboot3youtube.dto.respone.UserRespone;
import com.example.springboot3youtube.enums.Role;
import com.example.springboot3youtube.exception.AppException;
import com.example.springboot3youtube.exception.ErrorCode;
import com.example.springboot3youtube.mapper.UserMapper;
import com.example.springboot3youtube.dto.request.UserUpdateRequest;
import com.example.springboot3youtube.entity.User;
import com.example.springboot3youtube.repository.UserRepository;
import com.example.springboot3youtube.dto.request.UserCreationRequest;
import com.example.springboot3youtube.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true) // inject bean bằng contructor sử dụng thư viện lombook
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper ;
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserCreationRequest request) {

        if (userRepository.existsByUsername(request.getUsername())){
            throw  new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public List<UserRespone> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserRespone).toList();
    }

    @Override
    public UserRespone getUserById(String userId) {
        Integer id = Integer.valueOf(userId);
        return userMapper.toUserRespone(userRepository.findById(id).orElseThrow(()->new RuntimeException("user not found")));
    }

    @Override
    public UserRespone updateUser(String userId , UserUpdateRequest request) {
        Integer id = Integer.valueOf(userId);
        User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("user not found"));
        userMapper.updateUser(user,request);
        return userMapper.toUserRespone(userRepository.save(user));
    }
}
