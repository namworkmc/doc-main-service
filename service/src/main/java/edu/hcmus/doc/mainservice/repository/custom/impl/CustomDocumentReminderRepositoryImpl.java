package edu.hcmus.doc.mainservice.repository.custom.impl;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.map;
import static com.querydsl.core.group.GroupBy.set;
import static edu.hcmus.doc.mainservice.model.entity.QDocumentReminder.documentReminder;
import static edu.hcmus.doc.mainservice.model.entity.QIncomingDocument.incomingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingDocument.processingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingUser.processingUser;

import edu.hcmus.doc.mainservice.model.entity.DocumentReminder;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomDocumentReminderRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomDocumentReminderRepositoryImpl
    extends DocAbstractCustomRepository<DocumentReminder>
    implements CustomDocumentReminderRepository {

  @Override
  public Map<LocalDate, Set<DocumentReminderStatusEnum>> getDocumentReminderStatusByUserIdAndTime(Long userId, YearMonth yearMonth) {
    return selectFrom(documentReminder)
        .innerJoin(processingDocument)
        .on(documentReminder.processingDoc.id.eq(processingDocument.id))
        .fetchJoin()
        .innerJoin(processingUser)
        .on(processingDocument.id.eq(processingUser.processingDocument.id))
        .where(processingUser.user.id.eq(userId)
            .and(documentReminder.expirationDate.month().eq(yearMonth.getMonthValue()))
            .and(documentReminder.expirationDate.year().eq(yearMonth.getYear())))
        .transform(groupBy(documentReminder.expirationDate).as(set(documentReminder.status)));
  }

  @Override
  public Map<DocumentReminderStatusEnum, Set<ProcessingDocument>> getDocumentReminderDetailByUserIdAndTime(Long userId, LocalDate date) {
    return selectFrom(documentReminder)
        .innerJoin(processingDocument)
        .on(documentReminder.processingDoc.id.eq(processingDocument.id))
        .fetchJoin()
        .innerJoin(processingUser)
        .on(processingDocument.id.eq(processingUser.processingDocument.id))
        .fetchJoin()
        .innerJoin(incomingDocument)
        .on(processingDocument.incomingDoc.id.eq(incomingDocument.id))
        .fetchJoin()
        .where(processingUser.user.id.eq(userId)
            .and(documentReminder.expirationDate.eq(date)))
        .transform(groupBy(documentReminder.status).as(set(processingDocument)));
  }

  @Override
  public Map<LocalDate, Set<DocumentReminderStatusEnum>> getDocumentReminderStatusByUserIdAndTime(Long currentUserId, int year) {
    return selectFrom(documentReminder)
        .innerJoin(processingDocument)
        .on(documentReminder.processingDoc.id.eq(processingDocument.id))
        .fetchJoin()
        .innerJoin(processingUser)
        .on(processingDocument.id.eq(processingUser.processingDocument.id))
        .where(processingUser.user.id.eq(currentUserId)
            .and(documentReminder.expirationDate.year().eq(year)))
        .transform(groupBy(documentReminder.expirationDate).as(set(documentReminder.status)));
  }

  @Override
  public Map<DocumentReminderStatusEnum, Map<LocalDate, Set<ProcessingDocument>>> getDocumentReminderDetailByUserIdAndTime(Long currentUserId, YearMonth yearMonth) {
    return selectFrom(documentReminder)
        .innerJoin(processingDocument)
        .on(documentReminder.processingDoc.id.eq(processingDocument.id))
        .fetchJoin()
        .innerJoin(processingUser)
        .on(processingDocument.id.eq(processingUser.processingDocument.id))
        .fetchJoin()
        .innerJoin(incomingDocument)
        .on(processingDocument.incomingDoc.id.eq(incomingDocument.id))
        .fetchJoin()
        .where(processingUser.user.id.eq(currentUserId)
            .and(documentReminder.expirationDate.month().eq(yearMonth.getMonthValue()))
            .and(documentReminder.expirationDate.year().eq(yearMonth.getYear())))
        .transform(groupBy(documentReminder.status)
            .as(map(documentReminder.expirationDate, set(processingDocument))));
  }

  @Override
  public long countByUserId(Long currentUserId) {
    return selectFrom(documentReminder)
        .select(documentReminder.id)
        .innerJoin(processingDocument)
        .on(documentReminder.processingDoc.id.eq(processingDocument.id))
        .innerJoin(processingUser)
        .on(processingDocument.id.eq(processingUser.processingDocument.id).and(processingUser.user.id.eq(currentUserId)))
        .where(documentReminder.isOpened.isFalse())
        .stream()
        .count();
  }

  @Override
  public List<DocumentReminder> getDocumentRemindersInAndIsNotOpen(List<Long> ids, Long currentUserId) {
    return selectFrom(documentReminder)
        .innerJoin(processingDocument)
        .on(documentReminder.processingDoc.id.eq(processingDocument.id))
        .innerJoin(processingUser)
        .on(processingDocument.id.eq(processingUser.processingDocument.id).and(processingUser.user.id.eq(currentUserId)))
        .where(documentReminder.id.in(ids).and(documentReminder.isOpened.isFalse()))
        .fetch();
  }
}
