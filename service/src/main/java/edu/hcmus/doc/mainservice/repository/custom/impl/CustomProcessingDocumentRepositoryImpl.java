package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QIncomingDocument.incomingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingDocument.processingDocument;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocumentSearchResultDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QDistributionOrganization;
import edu.hcmus.doc.mainservice.model.entity.QDocumentType;
import edu.hcmus.doc.mainservice.model.entity.QProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QSendingLevel;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CustomProcessingDocumentRepositoryImpl
    extends DocAbstractCustomRepository<ProcessingDocument>
    implements CustomProcessingDocumentRepository {

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
    return searchQueryByCriteria(searchCriteriaDto)
        .select(incomingDocument.id.count())
        .fetchFirst();
  }

  @Override
  public long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit) {
    Long totalElements = getTotalElements(searchCriteriaDto);
    return (totalElements / limit) + (totalElements % limit == 0 ? 0 : 1);
  }

  @Override
  public List<ProcessingDocument> searchByCriteria(SearchCriteriaDto searchCriteriaDto, long offset, long limit) {
    return
        searchQueryByCriteria(searchCriteriaDto)
            .select(
                processingDocument.id,
                processingStatusCases,
                processingDocument.processingDuration,
                incomingDocument.id,
                incomingDocument.incomingNumber,
                incomingDocument.originalSymbolNumber,
                incomingDocument.arrivingDate,
                incomingDocument.summary,
                incomingDocument.sendingLevel.id,
                incomingDocument.sendingLevel.level,
                incomingDocument.documentType.id,
                incomingDocument.documentType.type,
                incomingDocument.distributionOrg.id,
                incomingDocument.distributionOrg.name)
            .orderBy(incomingDocument.id.asc())
            .offset(offset * limit)
            .limit(limit)
            .fetch()
            .stream()
            .map(tuple -> {
              ProcessingDocument processingDocument = new ProcessingDocument();
              processingDocument.setId(tuple.get(QProcessingDocument.processingDocument.id));
              processingDocument.setStatus(ProcessingStatus.valueOf(tuple.get(processingStatusCases)));
              processingDocument.setProcessingDuration(tuple.get(QProcessingDocument.processingDocument.processingDuration));
              processingDocument.getIncomingDoc().setId(tuple.get(incomingDocument.id));
              processingDocument.getIncomingDoc().setIncomingNumber(tuple.get(incomingDocument.incomingNumber));
              processingDocument.getIncomingDoc().setOriginalSymbolNumber(tuple.get(incomingDocument.originalSymbolNumber));
              processingDocument.getIncomingDoc().setArrivingDate(tuple.get(incomingDocument.arrivingDate));
              processingDocument.getIncomingDoc().setSummary(tuple.get(incomingDocument.summary));
              processingDocument.getIncomingDoc().getSendingLevel().setId(tuple.get(incomingDocument.sendingLevel.id));
              processingDocument.getIncomingDoc().getSendingLevel().setLevel(tuple.get(incomingDocument.sendingLevel.level));
              processingDocument.getIncomingDoc().getDocumentType().setId(tuple.get(incomingDocument.documentType.id));
              processingDocument.getIncomingDoc().getDocumentType().setType(tuple.get(incomingDocument.documentType.type));
              processingDocument.getIncomingDoc().getDistributionOrg().setId(tuple.get(incomingDocument.distributionOrg.id));
              processingDocument.getIncomingDoc().getDistributionOrg().setName(tuple.get(incomingDocument.distributionOrg.name));
              return processingDocument;
            })
            .toList();
  }

  private JPAQuery<ProcessingDocument> searchQueryByCriteria(SearchCriteriaDto searchCriteriaDto) {
    BooleanBuilder where = new BooleanBuilder();

    if (searchCriteriaDto != null && StringUtils.isNotBlank(searchCriteriaDto.getIncomingNumber())) {
      where.and(incomingDocument.incomingNumber.eq(searchCriteriaDto.getIncomingNumber()));
    }
    if (searchCriteriaDto != null && StringUtils.isNotBlank(searchCriteriaDto.getOriginalSymbolNumber())) {
      where.and(
          incomingDocument.originalSymbolNumber.eq(searchCriteriaDto.getOriginalSymbolNumber()));
    }
    if (searchCriteriaDto != null && searchCriteriaDto.getDocumentTypeId() != null) {
      where.and(incomingDocument.documentType.id.eq(searchCriteriaDto.getDocumentTypeId()));
    }
    if (searchCriteriaDto != null && searchCriteriaDto.getDistributionOrgId() != null) {
      where.and(incomingDocument.distributionOrg.id.eq(searchCriteriaDto.getDistributionOrgId()));
    }
    if (searchCriteriaDto != null
        && searchCriteriaDto.getArrivingDateFrom() != null
        && searchCriteriaDto.getArrivingDateTo() != null) {
      where.and(incomingDocument.arrivingDate.between(
          searchCriteriaDto.getArrivingDateFrom().atStartOfDay().toLocalDate(),
          searchCriteriaDto.getArrivingDateTo().plusDays(1).atStartOfDay().toLocalDate()
      ));
    }
    if (searchCriteriaDto != null
        && searchCriteriaDto.getProcessingDurationFrom() != null
        && searchCriteriaDto.getProcessingDurationTo() != null) {
      where.and(incomingDocument.arrivingDate.between(
          searchCriteriaDto.getProcessingDurationFrom().atStartOfDay().toLocalDate(),
          searchCriteriaDto.getProcessingDurationTo().plusDays(1).atStartOfDay().toLocalDate()
      ));
    }
    if (searchCriteriaDto != null && StringUtils.isNotBlank(searchCriteriaDto.getSummary())) {
      where.and(incomingDocument.summary.startsWithIgnoreCase(searchCriteriaDto.getSummary()));
    }

    return selectFrom(processingDocument)
        .rightJoin(processingDocument.incomingDoc, incomingDocument)
        .innerJoin(incomingDocument.sendingLevel, QSendingLevel.sendingLevel)
        .innerJoin(incomingDocument.documentType, QDocumentType.documentType)
        .innerJoin(incomingDocument.distributionOrg, QDistributionOrganization.distributionOrganization)
        .where(where);
  }

  @Override
  public List<ProcessingDocument> findProcessingDocumentsByElasticSearchResult(
      List<IncomingDocumentSearchResultDto> incomingDocumentSearchResultDtoList, long offset,
      long limit) {
    BooleanBuilder where = new BooleanBuilder();
    where.and(incomingDocument.id.in(
        incomingDocumentSearchResultDtoList.stream().map(IncomingDocumentSearchResultDto::getId)
            .toList()));
    return selectFrom(processingDocument)
        .rightJoin(processingDocument.incomingDoc, incomingDocument)
        .innerJoin(incomingDocument.sendingLevel, QSendingLevel.sendingLevel)
        .innerJoin(incomingDocument.documentType, QDocumentType.documentType)
        .innerJoin(incomingDocument.distributionOrg,
            QDistributionOrganization.distributionOrganization)
        .where(where)
        .select(
            processingDocument.id,
            processingStatusCases,
            processingDocument.processingDuration,
            incomingDocument.id,
            incomingDocument.incomingNumber,
            incomingDocument.originalSymbolNumber,
            incomingDocument.arrivingDate,
            incomingDocument.summary,
            incomingDocument.sendingLevel.id,
            incomingDocument.sendingLevel.level,
            incomingDocument.documentType.id,
            incomingDocument.documentType.type,
            incomingDocument.distributionOrg.id,
            incomingDocument.distributionOrg.name)
        .orderBy(incomingDocument.id.asc())
        .offset(offset * limit)
        .limit(limit)
        .fetch()
        .stream()
        .map(tuple -> {
          ProcessingDocument processingDocument = new ProcessingDocument();
          processingDocument.setId(tuple.get(QProcessingDocument.processingDocument.id));
          processingDocument.setStatus(ProcessingStatus.valueOf(tuple.get(processingStatusCases)));
          processingDocument.setProcessingDuration(
              tuple.get(QProcessingDocument.processingDocument.processingDuration));
          processingDocument.getIncomingDoc().setId(tuple.get(incomingDocument.id));
          processingDocument.getIncomingDoc()
              .setIncomingNumber(tuple.get(incomingDocument.incomingNumber));
          processingDocument.getIncomingDoc()
              .setOriginalSymbolNumber(tuple.get(incomingDocument.originalSymbolNumber));
          processingDocument.getIncomingDoc()
              .setArrivingDate(tuple.get(incomingDocument.arrivingDate));
          processingDocument.getIncomingDoc().setSummary(tuple.get(incomingDocument.summary));
          processingDocument.getIncomingDoc().getSendingLevel()
              .setId(tuple.get(incomingDocument.sendingLevel.id));
          processingDocument.getIncomingDoc().getSendingLevel()
              .setLevel(tuple.get(incomingDocument.sendingLevel.level));
          processingDocument.getIncomingDoc().getDocumentType()
              .setId(tuple.get(incomingDocument.documentType.id));
          processingDocument.getIncomingDoc().getDocumentType()
              .setType(tuple.get(incomingDocument.documentType.type));
          processingDocument.getIncomingDoc().getDistributionOrg()
              .setId(tuple.get(incomingDocument.distributionOrg.id));
          processingDocument.getIncomingDoc().getDistributionOrg()
              .setName(tuple.get(incomingDocument.distributionOrg.name));
          return processingDocument;
        })
        .toList();
  }

}
