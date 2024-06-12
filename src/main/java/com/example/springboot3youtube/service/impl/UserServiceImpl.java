package com.example.springboot3youtube.service.impl;

import com.example.springboot3youtube.dto.request.UserCreationRequest;
import com.example.springboot3youtube.dto.request.UserUpdateRequest;
import com.example.springboot3youtube.dto.respone.UserRespone;
import com.example.springboot3youtube.entity.User;
import com.example.springboot3youtube.enums.Role;
import com.example.springboot3youtube.exception.AppException;
import com.example.springboot3youtube.exception.ErrorCode;
import com.example.springboot3youtube.mapper.UserMapper;
import com.example.springboot3youtube.repository.UserRepository;
import com.example.springboot3youtube.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // inject bean bằng contructor sử dụng thư viện lombook
public class UserServiceImpl implements UserService {
  private UserRepository userRepository;
  private UserMapper userMapper;
  private PasswordEncoder passwordEncoder;

  @Override
  public User createUser(UserCreationRequest request) {

    if (userRepository.existsByUsername(request.getUsername())) {
      throw new AppException(ErrorCode.USER_EXISTED);
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

  //    @PostAuthorize("returnObject.username == authentication.name") // ko xem được thông tin của người khác mà chỉ xem được thông tin của người đăng nhập
  // chỉ lấy được thông tin của chính mình
  @Override
  public UserRespone getUserById(String userId) {
    Integer id = Integer.valueOf(userId);
    return userMapper.toUserRespone(userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found")));
  }

  @Override
  public UserRespone updateUser(String userId, UserUpdateRequest request) {
    Integer id = Integer.valueOf(userId);
    User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
    userMapper.updateUser(user, request);
    return userMapper.toUserRespone(userRepository.save(user));
  }

  // trong security khi 1 request được xác thực thành công thì thông tin đăng nhập sẽ được lưu trong SecurityContextHolder
  @Override
  public UserRespone getMyInfo() {
    var context = SecurityContextHolder.getContext();
    String name = context.getAuthentication().getName();
    User user = userRepository.findByUsername(name).orElseThrow(
         () -> new AppException(ErrorCode.USER_EXISTED)
    );
    return userMapper.toUserRespone(user);
  }


}
