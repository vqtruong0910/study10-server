package com.project.study.errors;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UnauthorizedException extends RuntimeException {
  private String message;

  public UnauthorizedException(String message) {
    super(message);
    this.message = message;
  }
}
