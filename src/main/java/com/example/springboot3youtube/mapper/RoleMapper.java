package com.example.springboot3youtube.mapper;

import com.example.springboot3youtube.dto.request.RoleRequest;
import com.example.springboot3youtube.dto.respone.RoleRespone;
import com.example.springboot3youtube.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  @Mapping(target = "permissions" , ignore = true)
  Role toRole(RoleRequest request);
  RoleRespone toRoleRespone(Role role);
}
