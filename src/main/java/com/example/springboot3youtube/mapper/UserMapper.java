package com.example.springboot3youtube.mapper;

import com.example.springboot3youtube.dto.respone.UserRespone;
import com.example.springboot3youtube.entity.User;
import com.example.springboot3youtube.dto.request.UserCreationRequest;
import com.example.springboot3youtube.dto.request.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUser(UserCreationRequest userCreationRequest);
  void updateUser(@MappingTarget User user , UserUpdateRequest userUpdateRequest); //define là request sẽ mapping vào user

//  @Mapping(source ="firstName" ,target = "lastName") mapping tuwf fisrtname sang lastname
//  @Mapping(source ="firstName" ,ignore = "lastName") ko mapping field lastName
  UserRespone toUserRespone(User user);
}
