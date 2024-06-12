package com.example.springboot3youtube.mapper;

import com.example.springboot3youtube.dto.request.PermissionRequest;
import com.example.springboot3youtube.dto.respone.PermissionRespone;
import com.example.springboot3youtube.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
  Permission toPermission(PermissionRequest request);
  PermissionRespone toPermissionRespone(Permission permission);
}
