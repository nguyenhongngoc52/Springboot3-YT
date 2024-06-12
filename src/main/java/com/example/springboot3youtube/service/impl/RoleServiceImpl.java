package com.example.springboot3youtube.service.impl;

import com.example.springboot3youtube.dto.request.RoleRequest;
import com.example.springboot3youtube.dto.respone.RoleRespone;
import com.example.springboot3youtube.mapper.RoleMapper;
import com.example.springboot3youtube.repository.PermissionRepository;
import com.example.springboot3youtube.repository.RoleRepository;
import com.example.springboot3youtube.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
  private RoleRepository roleRepository;
  private RoleMapper roleMapper;
  private PermissionRepository permissionRepository;

  @Override
  public RoleRespone create(RoleRequest request) {
    var role = roleMapper.toRole(request);
    var permissions = permissionRepository.findAllById(request.getPermissions());
    role.setPermissions(new HashSet<>(permissions));
    role = roleRepository.save(role);
    return roleMapper.toRoleRespone(role);
  }

  @Override
  public List<RoleRespone> getAll() {

    return roleRepository.findAll()
         .stream().map(roleMapper::toRoleRespone).toList();
  }

  @Override
  public void deleteRole(String role) {
    roleRepository.deleteById(role);
  }
}
