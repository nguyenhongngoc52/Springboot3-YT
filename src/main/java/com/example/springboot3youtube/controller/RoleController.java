package com.example.springboot3youtube.controller;

import com.example.springboot3youtube.dto.request.ApiRespone;
import com.example.springboot3youtube.dto.request.PermissionRequest;
import com.example.springboot3youtube.dto.request.RoleRequest;
import com.example.springboot3youtube.dto.respone.PermissionRespone;
import com.example.springboot3youtube.dto.respone.RoleRespone;
import com.example.springboot3youtube.service.PermissionService;
import com.example.springboot3youtube.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
  private RoleService roleService;

  @PostMapping
  ApiRespone<RoleRespone> create(@RequestBody RoleRequest request){

    return ApiRespone.<RoleRespone>builder()
         .result(roleService.create(request))
         .build();
  }

  @GetMapping
  ApiRespone<List<RoleRespone>> getAll(){

    return ApiRespone.<List<RoleRespone>>builder()
         .result(roleService.getAll())
         .build();
  }

  @DeleteMapping("/{role}")
  ApiRespone<Void> delete(@PathVariable(name = "role") String role){
    roleService.deleteRole(role);
    return ApiRespone.<Void>builder()
         .build();
  }
}
