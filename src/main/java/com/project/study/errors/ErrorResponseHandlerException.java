package com.project.study.errors;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorResponseHandlerException {

  @ExceptionHandler(value = ConfligException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse ConfligResponseHandler(ConfligException ex) {
    return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
  }

  @ExceptionHandler(value = NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse NotFoundResponseHandler(NotFoundException ex) {
    return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
  }

  @ExceptionHandler(value = UnauthorizedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorResponse UnauthorizedResposeHandler(UnauthorizedException ex) {
    return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
        ex.getAllErrors().stream().map(i -> i.getDefaultMessage()).collect(Collectors.toList())
            .toString());
  }

  @ExceptionHandler(value = MissingRequestCookieException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse NotFoundCookieResponseHandler(MissingRequestCookieException ex) {
    return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
  }
}
