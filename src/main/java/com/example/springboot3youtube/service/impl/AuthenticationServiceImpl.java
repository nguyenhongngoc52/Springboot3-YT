package com.example.springboot3youtube.service.impl;

import com.example.springboot3youtube.dto.request.AuthenticationRequest;
import com.example.springboot3youtube.dto.request.IntrospectRequest;
import com.example.springboot3youtube.dto.request.LogoutRequest;
import com.example.springboot3youtube.dto.request.RefreshRequest;
import com.example.springboot3youtube.dto.respone.AuthenticationRespone;
import com.example.springboot3youtube.dto.respone.IntrospectRespone;
import com.example.springboot3youtube.entity.InvalidatedToken;
import com.example.springboot3youtube.entity.User;
import com.example.springboot3youtube.exception.AppException;
import com.example.springboot3youtube.exception.ErrorCode;
import com.example.springboot3youtube.repository.InvalidatedTokenRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private InvalidatedTokenRepository invalidatedTokenRepository;

  @NonFinal
  protected static final String SIGNER_KEY = "4wrC2npX87qo9RF8e9DCo3XE6Sw6NPDcVjXAHw7Mc0zVJDofIMquOeKrWMy3bJus";
  @NonFinal
  @Value("${jwt.valid-duration}")
  protected long VALID_DURATION;

  @NonFinal
  @Value("${jwt.refreshable-duration}")
  protected long REFRESHABLE_DURATION;


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
  public IntrospectRespone introspect(IntrospectRequest request) throws ParseException, JOSEException {
    var token = request.getToken();
    boolean invalid = true;
    try {
      verifyToken(token, false);
    } catch (AppException e) {
      invalid = false;
    }
    return IntrospectRespone.builder().valid(invalid).build();

  }

  private String generateToken(User user) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
         .subject(user.getUsername())
         .issuer("security.com")
         .issueTime(new Date())
         .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
         .jwtID(UUID.randomUUID().toString())
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

  @Override
  public void logout(LogoutRequest request) throws ParseException, JOSEException {
    try {
      var signToken = verifyToken(request.getToken(), true);
      String jti = signToken.getJWTClaimsSet().getJWTID();
      Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
      InvalidatedToken invalidatedToken = InvalidatedToken.builder()
           .id(jti)
           .expiryTime(expiryTime)
           .build();

      invalidatedTokenRepository.save(invalidatedToken);
    } catch (AppException e) {
      log.info("Token already expired");
    }

  }

  private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
    JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
    SignedJWT signedJWT = SignedJWT.parse(token);
    var verified = signedJWT.verify(verifier);
    Date expityTime = (isRefresh) ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
         : signedJWT.getJWTClaimsSet().getExpirationTime();
    if (!(verified && expityTime.after(new Date())))
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    return signedJWT;
  }

  private String buildScope(User user) {
    StringJoiner stringJoiner = new StringJoiner(" ");
    if (!CollectionUtils.isEmpty(user.getRoles())) {
      user.getRoles().forEach(role -> {
             stringJoiner.add("ROLE_" + role.getName());
             if (!CollectionUtils.isEmpty(role.getPermissions()))
               role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
           }
      );
    }
    return stringJoiner.toString();
  }

  @Override
  public AuthenticationRespone refreshToken(RefreshRequest request) throws ParseException, JOSEException {
    var signJWT = verifyToken(request.getToken(), true);
    var jti = signJWT.getJWTClaimsSet().getJWTID();
    var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
    InvalidatedToken invalidatedToken = InvalidatedToken.builder()
         .id(jti)
         .expiryTime(expiryTime)
         .build();
    invalidatedTokenRepository.save(invalidatedToken);
    var userName = signJWT.getJWTClaimsSet().getSubject();
    var user = userRepository.findByUsername(userName).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

    var token = generateToken(user);
    return AuthenticationRespone.builder()
         .token(token)
         .authenticated(true)
         .build();
  }
}
