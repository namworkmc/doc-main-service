package edu.hcmus.doc.mainservice.repository.custom.impl;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.map;
import static com.querydsl.core.group.GroupBy.set;
import static edu.hcmus.doc.mainservice.model.entity.QIncomingDocument.incomingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingDocument.processingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingUser.processingUser;

import edu.hcmus.doc.mainservice.model.entity.DocumentReminder;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QDocumentReminder;
import edu.hcmus.doc.mainservice.model.entity.QIncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.QOutgoingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUser;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomDocumentReminderRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CustomDocumentReminderRepositoryImpl
    extends DocAbstractCustomRepository<DocumentReminder>
    implements CustomDocumentReminderRepository {

  private static final QDocumentReminder qDocumentReminder = QDocumentReminder.documentReminder;

  @Override
  public Map<LocalDate, Set<DocumentReminderStatusEnum>> getDocumentReminderStatusByUserIdAndTime(Long userId, YearMonth yearMonth) {
    return selectFrom(qDocumentReminder)
        .innerJoin(processingUser)
        .on(qDocumentReminder.processingUser.id.eq(processingUser.id))
        .fetchJoin()
        .innerJoin(processingDocument)
        .on(processingDocument.id.eq(processingUser.processingDocument.id))
        .where(processingUser.user.id.eq(userId)
            .and(qDocumentReminder.expirationDate.month().eq(yearMonth.getMonthValue()))
            .and(qDocumentReminder.expirationDate.year().eq(yearMonth.getYear())))
        .transform(groupBy(qDocumentReminder.expirationDate).as(set(qDocumentReminder.status)));
  }

  @Override
  public Map<DocumentReminderStatusEnum, Set<ProcessingDocument>> getDocumentReminderDetailByUserIdAndTime(Long userId, LocalDate date) {
    QProcessingUser qProcessingUser = new QProcessingUser(qDocumentReminder.processingUser.getMetadata().getName());
    QProcessingDocument qProcessingDocument = new QProcessingDocument(qProcessingUser.processingDocument.getMetadata().getName());
    QIncomingDocument qIncomingDocument = new QIncomingDocument(qProcessingDocument.incomingDoc.getMetadata().getName());
    QOutgoingDocument qOutgoingDocument = new QOutgoingDocument(qProcessingDocument.outgoingDocument.getMetadata().getName());

    return selectFrom(qDocumentReminder, qIncomingDocument, qOutgoingDocument)
        .innerJoin(qProcessingUser).on(qDocumentReminder.processingUser.id.eq(qProcessingUser.id))
        .fetchJoin()
        .innerJoin(qProcessingDocument).on(qProcessingDocument.id.eq(qProcessingUser.processingDocument.id))
        .fetchJoin()
        .where(qProcessingUser.user.id.eq(userId)
            .and(qDocumentReminder.expirationDate.eq(date))
            .and(
                qProcessingDocument.incomingDoc.id.eq(qIncomingDocument.id)
                    .or(qProcessingDocument.outgoingDocument.id.eq(qOutgoingDocument.id))
            ))
        .transform(groupBy(qDocumentReminder.status).as(set(qProcessingDocument)));
  }

  @Override
  public Map<LocalDate, Set<DocumentReminderStatusEnum>> getDocumentReminderStatusByUserIdAndTime(Long currentUserId, int year) {
    return selectFrom(qDocumentReminder)
        .innerJoin(processingUser)
        .on(qDocumentReminder.processingUser.id.eq(processingUser.id))
        .fetchJoin()
        .innerJoin(processingDocument)
        .on(processingDocument.id.eq(processingUser.processingDocument.id))
        .where(processingUser.user.id.eq(currentUserId)
            .and(qDocumentReminder.expirationDate.year().eq(year)))
        .transform(groupBy(qDocumentReminder.expirationDate).as(set(qDocumentReminder.status)));
  }

  @Override
  public Map<DocumentReminderStatusEnum, Map<LocalDate, Set<ProcessingDocument>>> getDocumentReminderDetailByUserIdAndTime(Long currentUserId, YearMonth yearMonth) {
    return selectFrom(qDocumentReminder)
        .innerJoin(processingUser)
        .on(qDocumentReminder.processingUser.id.eq(processingUser.id))
        .fetchJoin()
        .innerJoin(processingDocument)
        .on(processingDocument.id.eq(processingUser.processingDocument.id))
        .fetchJoin()
        .innerJoin(incomingDocument)
        .on(processingDocument.incomingDoc.id.eq(incomingDocument.id))
        .fetchJoin()
        .where(processingUser.user.id.eq(currentUserId)
            .and(qDocumentReminder.expirationDate.month().eq(yearMonth.getMonthValue()))
            .and(qDocumentReminder.expirationDate.year().eq(yearMonth.getYear())))
        .transform(groupBy(qDocumentReminder.status)
            .as(map(qDocumentReminder.expirationDate, set(processingDocument))));
  }

  @Override
  public long countByUserId(Long currentUserId) {
    return selectFrom(qDocumentReminder)
        .select(qDocumentReminder.id)
        .innerJoin(processingUser)
        .on(qDocumentReminder.processingUser.id.eq(processingUser.id))
        .innerJoin(processingDocument)
        .on(processingDocument.id.eq(processingUser.processingDocument.id).and(processingUser.user.id.eq(currentUserId)))
        .where(qDocumentReminder.isOpened.isFalse())
        .stream()
        .count();
  }

  @Override
  public List<DocumentReminder> getDocumentRemindersInAndIsNotOpen(List<Long> ids, Long currentUserId) {
    return selectFrom(qDocumentReminder)
        .innerJoin(processingUser)
        .on(qDocumentReminder.processingUser.id.eq(processingUser.id))
        .innerJoin(processingDocument)
        .on(processingDocument.id.eq(processingUser.processingDocument.id).and(processingUser.user.id.eq(currentUserId)))
        .where(qDocumentReminder.id.in(ids).and(qDocumentReminder.isOpened.isFalse()))
        .fetch();
  }

  @Override
  public Optional<DocumentReminder> findByProcessingUserId(Long processingUserId) {
    return Optional.ofNullable(selectFrom(qDocumentReminder)
        .innerJoin(processingUser)
        .on(qDocumentReminder.processingUser.id.eq(processingUser.id))
        .fetchJoin()
        .where(processingUser.id.eq(processingUserId))
        .fetchFirst());
  }
}
