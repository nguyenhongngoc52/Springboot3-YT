package com.example.springboot3youtube.service.impl;

import com.example.springboot3youtube.dto.request.AuthenticationRequest;
import com.example.springboot3youtube.dto.request.IntrospectRequest;
import com.example.springboot3youtube.dto.respone.AuthenticationRespone;
import com.example.springboot3youtube.dto.respone.IntrospectRespone;
import com.example.springboot3youtube.entity.User;
import com.example.springboot3youtube.exception.AppException;
import com.example.springboot3youtube.exception.ErrorCode;
import com.example.springboot3youtube.repository.UserRepository;
import com.example.springboot3youtube.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @NonFinal
  protected static final String SIGNER_KEY = "4wrC2npX87qo9RF8e9DCo3XE6Sw6NPDcVjXAHw7Mc0zVJDofIMquOeKrWMy3bJus";

  @Override
  public AuthenticationRespone authenticate(AuthenticationRequest request) {
    var user = userRepository.findByUsername(request.getUserName())
         .orElseThrow(() -> new AppException((ErrorCode.USER_NOT_EXISTED)));
    boolean authenticated = passwordEncoder.matches(request.getPassWord(), user.getPassword());
    if (!authenticated) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }
    var token = generateToken(user);
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
      return IntrospectRespone.builder().valid(verified && expityTime.after(new Date())).build();
    } catch (JOSEException | ParseException e) {
      throw new RuntimeException(e);
    }
  }

  private String generateToken(User user) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
         .subject(user.getUsername())
         .issuer("security.com")
         .issueTime(new Date())
         .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
         .claim("scope", buildScope(user))
         .build();
    Payload payload = new Payload(jwtClaimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(header, payload);
    try {
      jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  private String buildScope(User user) {
    StringJoiner stringJoiner = new StringJoiner(" ");
    if (!CollectionUtils.isEmpty(user.getRoles())) {
      user.getRoles().forEach(stringJoiner::add);
    }
    return stringJoiner.toString();
  }
}
