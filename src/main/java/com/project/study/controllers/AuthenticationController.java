package com.project.study.controllers;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.study.dtos.MessageResponse;
import com.project.study.dtos.Role;
import com.project.study.entity.UserEntity;
import com.project.study.errors.ConfligException;
import com.project.study.errors.NotFoundException;
import com.project.study.repositories.UserRepository;
import com.project.study.utils.JwtUltils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/auth")
@Slf4j
public class AuthenticationController {
  private UserRepository userRepository;
  private AuthenticationManager authenticationManager;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public AuthenticationController(UserRepository userRepository, AuthenticationManager authenticationManager,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/login")
  ResponseEntity<MessageResponse> Login(@Valid @RequestBody UserEntity userDto, HttpServletResponse response,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new ConfligException(
          bindingResult.getAllErrors().stream().map(i -> i.getDefaultMessage()).collect(Collectors.toList())
              .toString());
    }
    try {
      Authentication auth = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(auth);

      UserEntity userDetails = (UserEntity) auth.getPrincipal();

      String accessToken = JwtUltils.createAccessJwt(userDetails.getUsername(), userDetails.getRole().name());
      String refreshToken = JwtUltils.createRefreshJwt(userDetails.getUsername());

      Cookie cookie = new Cookie("refesh_token", refreshToken);
      cookie.setHttpOnly(true);
      cookie.setPath("/");
      response.addCookie(cookie);

      log.info("Email " + userDto.getEmail() + " login success");
      MessageResponse messageResponse = new MessageResponse(HttpStatus.OK.value(), "Valid", new TokenDto(accessToken));
      return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    } catch (Exception e) {
      throw new NotFoundException("Email or password does not exist");
    }
  }

  @PostMapping("/register")
  ResponseEntity<MessageResponse> Register(@Valid @RequestBody UserEntity userDto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new ConfligException(
          bindingResult.getAllErrors().stream().map(i -> i.getDefaultMessage()).collect(Collectors.toList())
              .toString());
    }
    Optional<UserEntity> user = userRepository.findByEmail(userDto.getEmail());

    if (!user.isPresent()) {
      UserEntity userEntity = UserEntity.builder()
          .email(userDto.getEmail())
          .password(passwordEncoder.encode(userDto.getPassword())).build();
      userEntity.setRole(Role.USER);
      userRepository.save(userEntity);
      MessageResponse messageResponse = new MessageResponse(HttpStatus.OK.value(), "Register successfull", userEntity);
      return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }
    throw new ConfligException("Email already exists");
  }

  @GetMapping("/logout")
  ResponseEntity<MessageResponse> Logout() {
    SecurityContextHolder.clearContext();
    MessageResponse messageResponse = new MessageResponse(HttpStatus.OK.value(), "Logout successfull", null);
    return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
  }

  /**
   * UserLoginDto
   */
  @AllArgsConstructor
  @Getter
  @Setter
  public class TokenDto {
    private String accessToken;
  }
}
