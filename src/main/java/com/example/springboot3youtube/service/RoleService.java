package com.example.springboot3youtube.service;

import com.example.springboot3youtube.dto.request.RoleRequest;
import com.example.springboot3youtube.dto.respone.RoleRespone;

import java.util.List;

public interface RoleService {
  RoleRespone create(RoleRequest request);
  List<RoleRespone> getAll();
  void deleteRole(String role);
}
