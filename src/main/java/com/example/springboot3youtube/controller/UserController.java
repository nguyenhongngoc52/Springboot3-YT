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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
  private UserService userService;

  @PostMapping
  ApiRespone<User> createUser(@RequestBody @Valid UserCreationRequest userCreationRequest) {
    ApiRespone<User> result = new ApiRespone<>();
    result.setResult(userService.createUser(userCreationRequest));
    return result;
  }

  @GetMapping
  ApiRespone<List<UserRespone>> getUsers() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    log.info("Username: {}", authentication.getName());
    authentication.getAuthorities().forEach(logcheck -> {
      log.info(logcheck.getAuthority());
    });

    return ApiRespone.<List<UserRespone>>builder().result(userService.getUsers()).build();
  }

  @GetMapping("/{userId}")
  ApiRespone<UserRespone> getUser(@PathVariable("userId") String userId) {
    return ApiRespone.<UserRespone>builder()
         .result(userService.getUserById(userId)).build();
  }

  @GetMapping("/myInfo")
  ApiRespone<UserRespone> getMyInfo() {
    return ApiRespone.<UserRespone>builder()
         .result(userService.getMyInfo()).build();
  }

  @PutMapping("/{userId}")
  ApiRespone<UserRespone> updateUser(
       @PathVariable("userId") String userId,
       @RequestBody UserUpdateRequest request) {
    return ApiRespone.<UserRespone>builder()
         .result(userService.updateUser(userId, request)).build();
  }

}
