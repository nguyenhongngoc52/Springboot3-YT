package com.example.springboot3youtube.controller;

import com.example.springboot3youtube.dto.request.ApiRespone;
import com.example.springboot3youtube.dto.request.PermissionRequest;
import com.example.springboot3youtube.dto.respone.PermissionRespone;
import com.example.springboot3youtube.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@Slf4j
public class PermissionController {
  private PermissionService permissionService;

  @PostMapping
  ApiRespone<PermissionRespone> create(@RequestBody PermissionRequest request){

    return ApiRespone.<PermissionRespone>builder()
         .result(permissionService.create(request))
         .build();
  }

  @GetMapping
  ApiRespone<List<PermissionRespone>> getAll(){

    return ApiRespone.<List<PermissionRespone>>builder()
         .result(permissionService.getAll())
         .build();
  }

  @DeleteMapping("/{permission}")
  ApiRespone<Void> delete(@PathVariable(name = "permission") String permission){
    permissionService.deletePermission(permission);
    return ApiRespone.<Void>builder()
         .build();
  }
}
