package com.project.study.errors;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  private String message;

  public CustomException(String msg) {
    super(msg);
    this.message = msg;
  }
}
