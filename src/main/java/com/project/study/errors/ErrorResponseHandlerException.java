package com.project.study.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorResponseHandlerException {

  @ExceptionHandler(value = CustomException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse ErrorResponseHandler(CustomException ex) {
    return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
  }
}
