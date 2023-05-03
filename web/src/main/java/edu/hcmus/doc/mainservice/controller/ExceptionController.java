package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.model.dto.ExceptionDto;
import edu.hcmus.doc.mainservice.model.dto.KeycloakErrorDto;
import edu.hcmus.doc.mainservice.model.exception.DocAuthorizedException;
import edu.hcmus.doc.mainservice.model.exception.DocNotFoundException;
import java.util.Objects;
import javax.ws.rs.ClientErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  @ExceptionHandler(ClientErrorException.class)
  public ResponseEntity<ExceptionDto> handleNotAuthorizedException(ClientErrorException exception) {
    KeycloakErrorDto errorDto = exception.getResponse().readEntity(KeycloakErrorDto.class);
    ResponseEntity<ExceptionDto> res;

    if ("Invalid refresh token".equals(errorDto.getErrorDescription())) {
      res = ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(new ExceptionDto(DocAuthorizedException.REFRESH_TOKEN_INVALID));
    } else {
      res = ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(new ExceptionDto(DocAuthorizedException.USER_INVALID));
    }

    return res;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ExceptionDto> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    FieldError errorField = exception.getFieldError();
    return ResponseEntity
        .badRequest()
        .body(new ExceptionDto(Objects.requireNonNull(errorField).getField() + ": " + errorField.getDefaultMessage()));
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ExceptionDto> handleInternalErrorException(Throwable throwable) {
    return ResponseEntity
        .internalServerError()
        .body(new ExceptionDto(throwable.getMessage()));
  }
}
