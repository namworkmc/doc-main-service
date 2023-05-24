package edu.hcmus.doc.mainservice.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DocDateTimeUtils {

  public static LocalDateTime getAtStartOfDay(LocalDate date) {
    return date.atStartOfDay();
  }

  public static LocalDateTime getAtEndOfDay(LocalDate date) {
    return LocalTime.MAX.atDate(date);
  }

  public static LocalDate get7DaysBefore(LocalDate date) {
    return date.minusDays(7);
  }

  public static LocalDateTime getAtStartOf7DaysBefore(LocalDate date) {
    return getAtStartOfDay(get7DaysBefore(date));
  }
}
