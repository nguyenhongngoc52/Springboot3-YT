package com.example.springboot3youtube.service;

import com.example.springboot3youtube.dto.request.PermissionRequest;
import com.example.springboot3youtube.dto.respone.PermissionRespone;

import java.util.List;

public interface PermissionService {
  PermissionRespone create(PermissionRequest request);
  List<PermissionRespone> getAll();
  void deletePermission(String permissionName);
}
