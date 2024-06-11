package com.example.springboot3youtube.controller;

import com.example.springboot3youtube.dto.request.ApiRespone;
import com.example.springboot3youtube.dto.request.AuthenticationRequest;
import com.example.springboot3youtube.dto.request.IntrospectRequest;
import com.example.springboot3youtube.dto.respone.AuthenticationRespone;
import com.example.springboot3youtube.dto.respone.IntrospectRespone;
import com.example.springboot3youtube.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class AuthenticationController {
  private AuthenticationService authenticationService;
  @PostMapping("/token")
  ApiRespone<AuthenticationRespone> authenticate(@RequestBody AuthenticationRequest request){
    var result = authenticationService.authenticate(request);
    return ApiRespone.<AuthenticationRespone>builder()
         .result(result)
         .build();
  }

  @PostMapping("/introspect")
  ApiRespone<IntrospectRespone> authenticate(@RequestBody IntrospectRequest request){
    var result = authenticationService.introspect(request);
    return ApiRespone.<IntrospectRespone>builder()
         .result(result)
         .build();
  }
}
