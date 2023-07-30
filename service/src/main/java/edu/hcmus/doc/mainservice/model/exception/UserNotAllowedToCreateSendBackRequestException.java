package edu.hcmus.doc.mainservice.model.exception;
public class UserNotAllowedToCreateSendBackRequestException extends DocNotFoundException {

  private static final String USER_NOT_ALLOWED_TO_CREATE_SEND_BACK_REQUEST = "transfer_modal.error.you_are_not_authorized_to_create_send_back_request";

  public UserNotAllowedToCreateSendBackRequestException() {
    super(USER_NOT_ALLOWED_TO_CREATE_SEND_BACK_REQUEST);
  }
}
