package com.example.springboot3youtube.service.impl;

import com.example.springboot3youtube.dto.request.PermissionRequest;
import com.example.springboot3youtube.dto.respone.PermissionRespone;
import com.example.springboot3youtube.entity.Permission;
import com.example.springboot3youtube.mapper.PermissionMapper;
import com.example.springboot3youtube.repository.PermissionRepository;
import com.example.springboot3youtube.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {
  private PermissionRepository permissionRepository;
  private PermissionMapper permissionMapper;
  @Override
  public PermissionRespone create(PermissionRequest request) {
    Permission permission = permissionMapper.toPermission(request);
    permission = permissionRepository.save(permission);
    return permissionMapper.toPermissionRespone(permission);
  }

  @Override
  public List<PermissionRespone> getAll() {
    var permissions = permissionRepository.findAll();
    return permissions.stream().map(permissionMapper::toPermissionRespone).toList();
  }

  public void deletePermission(String permissionName){
    permissionRepository.deleteById(permissionName);
  }
}
