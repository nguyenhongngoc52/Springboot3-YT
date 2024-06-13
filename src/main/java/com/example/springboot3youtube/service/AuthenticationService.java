package com.example.springboot3youtube.service;

import com.example.springboot3youtube.dto.request.AuthenticationRequest;
import com.example.springboot3youtube.dto.request.IntrospectRequest;
import com.example.springboot3youtube.dto.request.LogoutRequest;
import com.example.springboot3youtube.dto.request.RefreshRequest;
import com.example.springboot3youtube.dto.respone.AuthenticationRespone;
import com.example.springboot3youtube.dto.respone.IntrospectRespone;
import com.nimbusds.jose.JOSEException;
import org.hibernate.validator.internal.util.logging.Log;

import java.text.ParseException;

public interface AuthenticationService {
  AuthenticationRespone authenticate(AuthenticationRequest authenticationRequest);

  IntrospectRespone introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException;

  void logout(LogoutRequest request) throws ParseException, JOSEException;

  AuthenticationRespone refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
