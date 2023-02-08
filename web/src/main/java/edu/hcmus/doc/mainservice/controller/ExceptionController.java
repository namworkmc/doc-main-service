package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.model.dto.ExceptionDto;
import edu.hcmus.doc.mainservice.model.exception.DocNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

  @ExceptionHandler(DocNotFoundException.class)
  public ResponseEntity<ExceptionDto> handleDocNotFoundException(DocNotFoundException exception) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ExceptionDto(exception.getMessage()));
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ExceptionDto> handleInternalErrorException(Throwable throwable) {
    return ResponseEntity
        .internalServerError()
        .body(new ExceptionDto("INTERNAL_SERVER_ERROR"));
  }
}
