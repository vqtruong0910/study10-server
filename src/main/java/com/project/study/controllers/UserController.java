package com.project.study.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.study.dtos.MessageResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {

  @GetMapping("/user")
  ResponseEntity<MessageResponse> getUserByEmail() {
    log.info("Da vao route user");
    return null;
  }
}
