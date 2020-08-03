package com.codechallenge.citylink.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@ControllerAdvice
public class CityLinkControllerAdvice {
  @ExceptionHandler(value = InvalidCityException.class)
  public ResponseEntity<Object> exception(InvalidCityException exception) {
    return new ResponseEntity<>(exception.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
