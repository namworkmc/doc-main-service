package edu.hcmus.doc.mainservice.repository.custom.impl;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;
import static edu.hcmus.doc.mainservice.model.entity.QDocumentType.documentType;
import static edu.hcmus.doc.mainservice.model.entity.QIncomingDocument.incomingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingDocument.processingDocument;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.QDistributionOrganization;
import edu.hcmus.doc.mainservice.model.entity.QFolder;
import edu.hcmus.doc.mainservice.model.entity.QIncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.QSendingLevel;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import edu.hcmus.doc.mainservice.repository.custom.CustomIncomingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomIncomingDocumentRepositoryImpl
    extends DocAbstractCustomRepository<IncomingDocument>
    implements CustomIncomingDocumentRepository {

  private final StringExpression processingStatusCases = Expressions
      .cases()
      .when(processingDocument.status.eq(ProcessingStatus.IN_PROGRESS))
      .then(ProcessingStatus.IN_PROGRESS.value)
      .when(processingDocument.status.eq(ProcessingStatus.CLOSED))
      .then(ProcessingStatus.CLOSED.value)
      .otherwise(ProcessingStatus.UNPROCESSED.value)
      .as("status");


  @Override
  public Long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
    return selectFrom(incomingDocument)
        .select(incomingDocument.id.count())
        .fetchOne();
  }

  @Override
  public List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit) {
    return select(
        incomingDocument.id,
        incomingDocument.version,
        incomingDocument.sendingLevel,
        incomingDocument.documentType,
        incomingDocument.incomingNumber,
        incomingDocument.originalSymbolNumber,
        incomingDocument.arrivingDate,
        incomingDocument.distributionOrg,
        incomingDocument.summary
    ).from(incomingDocument)
        .innerJoin(incomingDocument.sendingLevel, QSendingLevel.sendingLevel)
        .innerJoin(incomingDocument.documentType, documentType)
        .innerJoin(incomingDocument.distributionOrg, QDistributionOrganization.distributionOrganization)
        .orderBy(incomingDocument.id.asc())
        .offset(offset * limit)
        .limit(limit)
        .fetch()
        .stream()
        .map(tuple -> {
          IncomingDocument incomingDocument = new IncomingDocument();
          incomingDocument.setId(tuple.get(QIncomingDocument.incomingDocument.id));
          incomingDocument.setVersion(tuple.get(QIncomingDocument.incomingDocument.version));
          incomingDocument.setSendingLevel(tuple.get(QIncomingDocument.incomingDocument.sendingLevel));
          incomingDocument.setDocumentType(tuple.get(QIncomingDocument.incomingDocument.documentType));
          incomingDocument.setIncomingNumber(tuple.get(QIncomingDocument.incomingDocument.incomingNumber));
          incomingDocument.setOriginalSymbolNumber(tuple.get(QIncomingDocument.incomingDocument.originalSymbolNumber));
          incomingDocument.setArrivingDate(tuple.get(QIncomingDocument.incomingDocument.arrivingDate));
          incomingDocument.setDistributionOrg(tuple.get(QIncomingDocument.incomingDocument.distributionOrg));
          incomingDocument.setSummary(tuple.get(QIncomingDocument.incomingDocument.summary));
          return incomingDocument;
        })
        .toList();
  }

    @Override
    public IncomingDocument getIncomingDocumentById(Long id) {
        return selectFrom(incomingDocument)
                .join(incomingDocument.folder, QFolder.folder)
                .fetchJoin()
                .join(incomingDocument.documentType, documentType)
                .fetchJoin()
                .join(incomingDocument.sendingLevel, QSendingLevel.sendingLevel)
                .fetchJoin()
                .join(incomingDocument.distributionOrg, QDistributionOrganization.distributionOrganization)
                .fetchJoin()
                .where(incomingDocument.id.eq(id))
                .fetchFirst();
    }

  @Override
  public List<IncomingDocument> getIncomingDocumentsByIds(List<Long> ids) {
    return selectFrom(incomingDocument)
        .where(incomingDocument.id.in(ids))
        .fetch();
  }

  @Override
  public Map<String, Set<Long>> getQuarterProcessingStatisticsByUserId(Long userId) {
    QProcessingDocument qProcessingDocument = processingDocument;
    QIncomingDocument qIncomingDocument = QIncomingDocument.incomingDocument;
    QProcessingUser qProcessingUser = QProcessingUser.processingUser;

    return selectFrom(qIncomingDocument)
        .select(processingStatusCases)
        .leftJoin(qProcessingDocument)
        .on(qIncomingDocument.id.eq(qProcessingDocument.incomingDoc.id))
        .leftJoin(qProcessingUser)
        .on(qProcessingDocument.id.eq(qProcessingUser.processingDocument.id))
        .where(qProcessingUser.user.id.eq(userId)
            .and(incomingDocument.createdDate.month()
                .divide(3)
                .ceil()
                .eq(2)))
        .transform(groupBy(processingStatusCases).as(set(qIncomingDocument.id)));
  }

  @Override
  public Map<String, Set<Long>> getQuarterProcessingDocumentTypeStatisticsByUserId(Long userId) {
    return selectFrom(incomingDocument)
        .innerJoin(incomingDocument.documentType, documentType)
        .orderBy(documentType.id.asc())
        .where(incomingDocument.createdDate.month()
            .divide(3)
            .ceil()
            .eq(2))
        .transform(groupBy(documentType.type).as(set(incomingDocument.id)));
  }
}
