package edu.hcmus.doc.mainservice.model.exception;

public class DepartmentEditException extends DocBusinessException {

  public static final String USERS_ARE_AVAILABLE_IN_DEPARTMENT = "department_management.department.users_are_available_in_department";

  public DepartmentEditException(String message) {
    super(message);
  }
}
