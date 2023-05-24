package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.DocumentReminderDetailsDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DocumentReminderService {

  Map<LocalDate, Set<DocumentReminderStatusEnum>> getCurrentUserDocumentReminders(int year, int month);

  Map<LocalDate, Set<DocumentReminderStatusEnum>> getCurrentUserDocumentReminders(int year);

  Map<DocumentReminderStatusEnum, Set<DocumentReminderDetailsDto>> getCurrentUserDocumentRemindersDetailsByTime(LocalDate date);

  Map<DocumentReminderStatusEnum, Set<DocumentReminderDetailsDto>> getCurrentUserDocumentRemindersDetailsByTime(int year, int month);

  long countUnreadDocumentReminders();

  long updateCurrentUserIsNotOpenedReminders(List<Long> ids);

  Long createdDocumentReminder(ProcessingUser processingUser);
}
