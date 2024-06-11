package com.example.springboot3youtube.service.impl;

import com.example.springboot3youtube.dto.request.AuthenticationRequest;
import com.example.springboot3youtube.dto.request.IntrospectRequest;
import com.example.springboot3youtube.dto.respone.AuthenticationRespone;
import com.example.springboot3youtube.dto.respone.IntrospectRespone;
import com.example.springboot3youtube.exception.AppException;
import com.example.springboot3youtube.exception.ErrorCode;
import com.example.springboot3youtube.repository.UserRepository;
import com.example.springboot3youtube.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
  private UserRepository userRepository;

  @NonFinal
  protected  static final String SIGNER_KEY = "4wrC2npX87qo9RF8e9DCo3XE6Sw6NPDcVjXAHw7Mc0zVJDofIMquOeKrWMy3bJus";
  @Override
  public AuthenticationRespone authenticate(AuthenticationRequest request) {
    var token = generateToken(request.getUserName());
    return AuthenticationRespone.builder()
         .token(token)
         .authenticated(true)
         .build();
  }

  @Override
  public IntrospectRespone introspect(IntrospectRequest request) {
    var token = request.getTooken();
    try {
      JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
      SignedJWT signedJWT = SignedJWT.parse(token);
      var verified = signedJWT.verify(verifier);
      Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();
      return IntrospectRespone.builder().valid(verified && expityTime.after(new Date())).build() ;
    } catch (JOSEException | ParseException e) {
      throw new RuntimeException(e);
    }
  }

  private String generateToken(String userName){
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
         .subject(userName)
         .issuer("security.com")
         .issueTime(new Date())
         .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
         .claim("customClaim","Custom")
         .build();
    Payload payload = new Payload(jwtClaimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(header,payload);
    try {
      jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }
}
