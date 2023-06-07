package edu.hcmus.doc.mainservice.model.exception;

public class DocMainServiceRuntimeException extends RuntimeException {

  public static final String CONCURRENT_UPDATE = "doc.exception.concurrent_update";

  public static final String INTERNAL_SERVER_ERROR = "doc.exception.internal_server_error";

  public DocMainServiceRuntimeException(String message) {
    super(message);
  }

  public DocMainServiceRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
