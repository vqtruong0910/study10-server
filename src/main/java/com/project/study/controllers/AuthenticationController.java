package com.project.study.controllers;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.study.dtos.Gender;
import com.project.study.dtos.MessageAlert;
import com.project.study.dtos.MessageResponse;
import com.project.study.dtos.Role;
import com.project.study.dtos.UserDto;
import com.project.study.dtos.UserLoginDto;
import com.project.study.entity.UserEntity;
import com.project.study.errors.ConfligException;
import com.project.study.errors.NotFoundException;
import com.project.study.repositories.UserRepository;
import com.project.study.services.AuthentiacationService;
import com.project.study.services.UserService;
import com.project.study.utils.JwtUltils;
import com.project.study.utils.Ultils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/auth")
@Slf4j
public class AuthenticationController {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private UserService userService;
  private AuthentiacationService authentiacationService;

  @Autowired
  public AuthenticationController(UserRepository userRepository, PasswordEncoder passwordEncoder,
      UserService userService, AuthentiacationService authentiacationService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
    this.authentiacationService = authentiacationService;
  }

  @PostMapping("/login")
  ResponseEntity<MessageResponse> Login(@Valid @RequestBody UserLoginDto userDto, HttpServletResponse response) {
    try {
      UserEntity userDetails = authentiacationService.login(userDto.getEmail(), userDto.getPassword());

      String accessToken = JwtUltils.createAccessJwt(userDetails.getUsername(), userDetails.getRole().name());
      String refreshToken = JwtUltils.createRefreshJwt(userDetails.getUsername());

      Ultils.addRefreshTokenCookie(response, refreshToken, (int) TimeUnit.DAYS.toSeconds(30));

      log.info("Email " + userDto.getEmail() + " login success");
      MessageResponse messageResponse = new MessageResponse(HttpStatus.OK.value(), "Valid",
          new MessageUserDto(userDetails.getEmail(), userDetails.getFullName(), accessToken));
      return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    } catch (Exception e) {
      throw new NotFoundException("Email or password does not exist");
    }
  }

  @PostMapping("/register")
  ResponseEntity<MessageAlert> Register(@Valid @RequestBody UserDto userDto) {
    Optional<UserEntity> user = userRepository.findByEmail(userDto.getEmail());

    if (!user.isPresent()) {
      UserEntity userEntity = UserEntity.builder()
          .email(userDto.getEmail())
          .role(Role.USER)
          .password(passwordEncoder.encode(userDto.getPassword())).build();
      userRepository.save(userEntity);
      MessageAlert messageResponse = new MessageAlert(HttpStatus.OK.value(), "Register successfull");
      return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }
    throw new ConfligException("Email already exists");
  }

  @PostMapping("/register-v2")
  ResponseEntity<MessageResponse> RegisterV2(@Valid @RequestBody UserDto userDto, HttpServletResponse response) {
    Optional<UserEntity> user = userRepository.findByEmail(userDto.getEmail());

    if (!user.isPresent()) {
      UserEntity userEntity = UserEntity.builder()
          .email(userDto.getEmail())
          .fullName(userDto.getFullName())
          .phoneNumber(userDto.getPhoneNumber())
          .birthDate(userDto.getBirthDate())
          .gender(userDto.getGender() == 1 ? Gender.MALE : Gender.FEMALE)
          .role(Role.USER)
          .password(passwordEncoder.encode(userDto.getPassword())).build();
      UserEntity userRegister = userRepository.save(userEntity);

      String accessToken = JwtUltils.createAccessJwt(userRegister.getUsername(), userRegister.getRole().name());
      String refreshToken = JwtUltils.createRefreshJwt(userRegister.getUsername());

      Ultils.addRefreshTokenCookie(response, refreshToken, (int) TimeUnit.DAYS.toSeconds(30));

      MessageResponse messageResponse = new MessageResponse(HttpStatus.OK.value(), "Register and login successfull",
          new MessageUserDto(userRegister.getEmail(), userRegister.getFullName(), accessToken));
      return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }
    throw new ConfligException("Register failed! Email already exists");
  }

  @GetMapping("/logout")
  ResponseEntity<MessageAlert> Logout(@CookieValue(name = "refresh_token", required = false) String refreshToken,
      HttpServletResponse response) {
    if (refreshToken != null) {
      Cookie cookie = new Cookie("refresh_token", "");
      cookie.setHttpOnly(true);
      cookie.setPath("/");
      response.addCookie(cookie);
    }
    SecurityContextHolder.clearContext();
    MessageAlert messageResponse = new MessageAlert(HttpStatus.OK.value(), "Logout successfull");
    return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
  }

  @GetMapping("/refresh_token")
  ResponseEntity<MessageResponse> RefreshToken(@CookieValue("refresh_token") String refreshToken,
      HttpServletResponse response) {
    DecodedJWT decodedJWT = JwtUltils.decodeJwt(refreshToken);
    if (decodedJWT != null) {
      String email = decodedJWT.getClaim("email").asString();
      UserEntity user = (UserEntity) this.userService.loadUserByUsername(email);
      String newRefreshToken = JwtUltils.createRefreshJwt(user.getUsername());
      String newAccessToken = JwtUltils.createAccessJwt(user.getUsername(), user.getRole().name());

      Ultils.addRefreshTokenCookie(response, newRefreshToken, (int) TimeUnit.DAYS.toSeconds(30));

      MessageResponse messageResponse = new MessageResponse(HttpStatus.OK.value(), "Refresh token successfull",
          new TokenDto(newAccessToken));
      return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }
    throw new NotFoundException("Refresh token does not exist");
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

  @AllArgsConstructor
  @Getter
  @Setter
  public class MessageUserDto {
    private String email;
    private String fullName;
    private String accessToken;
  }
}
