package com.example.springboot3youtube.service;

import com.example.springboot3youtube.dto.request.AuthenticationRequest;
import com.example.springboot3youtube.dto.request.IntrospectRequest;
import com.example.springboot3youtube.dto.respone.AuthenticationRespone;
import com.example.springboot3youtube.dto.respone.IntrospectRespone;

public interface AuthenticationService {
  AuthenticationRespone authenticate(AuthenticationRequest authenticationRequest);

  IntrospectRespone introspect(IntrospectRequest introspectRequest);
}
