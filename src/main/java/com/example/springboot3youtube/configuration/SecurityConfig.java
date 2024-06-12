package com.example.springboot3youtube.configuration;

import com.example.springboot3youtube.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  private final String[] PUBLIC_ENDPOINTS = {"/users", "/auth/token", "/auth/introspect"};

  private final String SECRET_KEY = "4wrC2npX87qo9RF8e9DCo3XE6Sw6NPDcVjXAHw7Mc0zVJDofIMquOeKrWMy3bJus";

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
         .authorizeHttpRequests(request ->
              request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                   .requestMatchers(HttpMethod.GET, "/users")
                   .hasRole(Role.ADMIN.name())//tự động tìm ROLE_ADMIN
//                   .hasAuthority("ROLE_ADMIN")
                   .anyRequest().authenticated()
         );
    //check token ở các request khác
    httpSecurity.oauth2ResourceServer(oauth2 ->
         oauth2.jwt(jwtConfigurer ->
              jwtConfigurer.decoder(jwtDecoder())
                   .jwtAuthenticationConverter(jwtAuthenticationConverter())
         )
    );

    httpSecurity.csrf(AbstractHttpConfigurer::disable);
    return httpSecurity.build();
  }

  @Bean
  JwtDecoder jwtDecoder() {
    SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HS512");

    return NimbusJwtDecoder.withSecretKey(secretKeySpec)
         .macAlgorithm(MacAlgorithm.HS512)
         .build();
  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter(){
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

}
