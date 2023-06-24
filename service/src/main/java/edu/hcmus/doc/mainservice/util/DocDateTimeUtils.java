package edu.hcmus.doc.mainservice.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DocDateTimeUtils {

  public static final String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";

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

  public static boolean isBetween(LocalDateTime time, LocalDateTime start, LocalDateTime end) {
    return time.isAfter(start) && time.isBefore(end);
  }
}
