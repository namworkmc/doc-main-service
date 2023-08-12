package edu.hcmus.doc.mainservice.util.validator;

import edu.hcmus.doc.mainservice.util.validator.annotation.StringDateFutureOrPresent;
import edu.hcmus.doc.mainservice.util.DocDateTimeUtils;
import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class StringDateValidator implements ConstraintValidator<StringDateFutureOrPresent, String> {

  private static final String DATE_REGEX_PATTERN = "^(\\d{2})/(\\d{2})/(\\d{4})$";

  @Override
  public boolean isValid(String dateAsString, ConstraintValidatorContext context) {
    if (StringUtils.isBlank(dateAsString) || !dateAsString.matches(DATE_REGEX_PATTERN)) {
      return false;
    }

    LocalDate dateToBeChecked = LocalDate.parse(dateAsString, DocDateTimeUtils.DD_MM_YYYY_FORMATTER);
    LocalDate now = LocalDate.now();
    return dateToBeChecked.isAfter(now) || dateToBeChecked.isEqual(now);
  }
}
