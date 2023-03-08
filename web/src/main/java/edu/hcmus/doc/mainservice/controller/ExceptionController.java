package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.model.dto.ExceptionDto;
import edu.hcmus.doc.mainservice.model.dto.KeycloakErrorDto;
import edu.hcmus.doc.mainservice.model.exception.DocAuthorizedException;
import edu.hcmus.doc.mainservice.model.exception.DocNotFoundException;
import javax.ws.rs.ClientErrorException;
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

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ExceptionDto> handleInternalErrorException(Throwable throwable) {
    return ResponseEntity
        .internalServerError()
        .body(new ExceptionDto("INTERNAL_SERVER_ERROR"));
  }
}
