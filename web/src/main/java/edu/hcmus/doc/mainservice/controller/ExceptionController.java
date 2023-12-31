package edu.hcmus.doc.mainservice.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import edu.hcmus.doc.mainservice.model.dto.ExceptionDto;
import edu.hcmus.doc.mainservice.model.dto.KeycloakErrorDto;
import edu.hcmus.doc.mainservice.model.exception.DocAuthorizedException;
import edu.hcmus.doc.mainservice.model.exception.DocBusinessException;
import edu.hcmus.doc.mainservice.model.exception.DocExistedException;
import edu.hcmus.doc.mainservice.model.exception.DocMainServiceRuntimeException;
import edu.hcmus.doc.mainservice.model.exception.DocMandatoryFields;
import edu.hcmus.doc.mainservice.model.exception.DocNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.DocStatusViolatedException;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.ClientErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

  @ExceptionHandler(DocNotFoundException.class)
  public ResponseEntity<ExceptionDto> handleDocNotFoundException(DocNotFoundException exception) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ExceptionDto(exception.getMessage()));
  }

  @ExceptionHandler(DocExistedException.class)
  public ResponseEntity<ExceptionDto> handleDocExistedException(DocExistedException exception) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
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

  @ExceptionHandler(DocStatusViolatedException.class)
  public ResponseEntity<ExceptionDto> docStatusViolatedException(DocStatusViolatedException exception) {
    return ResponseEntity
            .badRequest()
            .body(new ExceptionDto(exception.getMessage()));
  }

  @ExceptionHandler(DocMainServiceRuntimeException.class)
  public ResponseEntity<ExceptionDto> handleDocException(DocMainServiceRuntimeException exception) {
    return ResponseEntity
        .badRequest()
        .body(new ExceptionDto(exception.getMessage()));
  }

  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
  public ResponseEntity<ExceptionDto> handleMandatoryFieldsException(
      BindException exception) {
    log.warn(exception.getMessage());
    return ResponseEntity
        .badRequest()
        .body(new ExceptionDto(
            Optional.ofNullable(
                    exception.getFieldError())
                .orElseThrow(DocMainServiceRuntimeException::new
                ).getDefaultMessage()));
  }

  @ExceptionHandler(DocMandatoryFields.class)
  public ResponseEntity<ExceptionDto> handleMandatoryFieldsException(DocMandatoryFields exception) {
    log.warn(exception.getMessage());
    return ResponseEntity
        .badRequest()
        .body(new ExceptionDto(exception.getBusinessMessage()));
  }

  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<ExceptionDto> handleObjectOptimisticLockingFailureException(
      ObjectOptimisticLockingFailureException exception) {
    log.error(exception.getMessage());
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(new ExceptionDto(DocMainServiceRuntimeException.CONCURRENT_UPDATE));
  }

  @ExceptionHandler(FirebaseMessagingException.class)
  public ResponseEntity<ExceptionDto> handleFirebaseMessagingException(FirebaseMessagingException exception) {
    log.error(exception.getMessage(), exception);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ExceptionDto("Firebase messaging error"));
  }

  @ExceptionHandler(DocBusinessException.class)
  public ResponseEntity<ExceptionDto> handleDocBusinessException(DocBusinessException exception) {
    log.warn(exception.getMessage(), exception);
    return ResponseEntity
        .badRequest()
        .body(new ExceptionDto(exception.getMessage()));
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ExceptionDto> handleInternalErrorException(Throwable throwable) {
    log.error(throwable.getMessage(), throwable);

    return ResponseEntity
        .internalServerError()
        .body(new ExceptionDto(DocMainServiceRuntimeException.INTERNAL_SERVER_ERROR));
  }
}
