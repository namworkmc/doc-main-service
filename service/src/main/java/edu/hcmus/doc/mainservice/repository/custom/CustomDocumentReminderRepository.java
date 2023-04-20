package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.DocumentReminder;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CustomDocumentReminderRepository {

  Map<LocalDate, Set<DocumentReminderStatusEnum>> getDocumentReminderStatusByUserIdAndTime(Long userId, YearMonth yearMonth);

  Map<DocumentReminderStatusEnum, Set<ProcessingDocument>> getDocumentReminderDetailByUserIdAndTime(Long userId, LocalDate date);

  Map<LocalDate, Set<DocumentReminderStatusEnum>> getDocumentReminderStatusByUserIdAndTime(Long currentUserId, int year);

  Map<DocumentReminderStatusEnum, Map<LocalDate, Set<ProcessingDocument>>> getDocumentReminderDetailByUserIdAndTime(Long currentUserId, YearMonth yearMonth);

  long countByUserId(Long currentUserId);

  List<DocumentReminder> getDocumentRemindersInAndIsNotOpen(List<Long> ids, Long currentUserId);
}
