package com.example.springboot3youtube.service.impl;

import com.example.springboot3youtube.dto.respone.UserRespone;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true) // inject bean bằng contructor sử dụng thư viện lombook
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper ;

    @Override
    public User createUser(UserCreationRequest request) {
//        User user = new User();
        if (userRepository.existsByUsername(request.getUsername())){
            throw  new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);

//        user.setUsername(request.getUsername());
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
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
//        user.setUsername(request.getUsername());
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());
        return userMapper.toUserRespone(userRepository.save(user));
    }
}
