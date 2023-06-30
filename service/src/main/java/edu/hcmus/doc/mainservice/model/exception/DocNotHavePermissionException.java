package edu.hcmus.doc.mainservice.model.exception;

public class DocNotHavePermissionException extends DocMainServiceRuntimeException {

  public static final String DOC_NOT_HAVE_PERMISSION = "doc.not_have_permission";

  public DocNotHavePermissionException(String message) {
    super(message);
  }
}
