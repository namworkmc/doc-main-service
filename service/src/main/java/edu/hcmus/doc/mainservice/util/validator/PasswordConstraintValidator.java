package edu.hcmus.doc.mainservice.util.validator;

import edu.hcmus.doc.mainservice.model.enums.MESSAGE;
import edu.hcmus.doc.mainservice.model.exception.DocMandatoryFields;
import edu.hcmus.doc.mainservice.util.DocMessageUtils;
import org.apache.commons.lang3.StringUtils;

public class PasswordConstraintValidator {

  public static void validatePasswords(String... passwords) {
    for (String password : passwords) {
      String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
      if (StringUtils.isBlank(password) || !password.matches(PASSWORD_REGEX)) {
        throw new DocMandatoryFields(DocMessageUtils.getContent(MESSAGE.invalid_login_information));
      }
    }
  }
}
