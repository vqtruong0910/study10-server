package com.project.study.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.study.entity.UserEntity;

@Service
public class AuthentiacationService {

  private AuthenticationManager authenticationManager;

  public AuthentiacationService(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  public UserEntity login(String email, String password) {
    Authentication auth = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(email, password));
    SecurityContextHolder.getContext().setAuthentication(auth);

    return (UserEntity) auth.getPrincipal();
  }
}
