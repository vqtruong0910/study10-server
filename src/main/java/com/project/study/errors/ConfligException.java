package com.project.study.errors;

import lombok.Getter;

@Getter
public class ConfligException extends RuntimeException {
  private String message;

  public ConfligException(String msg) {
    super(msg);
    this.message = msg;
  }
}
