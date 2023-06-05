package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QIncomingDocument.incomingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QOutgoingDocument.outgoingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingDocument.processingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingUser.processingUser;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingUserRole.processingUserRole;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocumentSearchResultDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailResponse;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QDistributionOrganization;
import edu.hcmus.doc.mainservice.model.entity.QDocumentType;
import edu.hcmus.doc.mainservice.model.entity.QProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QSendingLevel;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
  public long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
    return searchQueryByCriteria(searchCriteriaDto)
        .select(incomingDocument.id.count())
        .fetchFirst();
  }

  @Override
  public long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit) {
    long totalElements = getTotalElements(searchCriteriaDto);
    return (totalElements / limit) + (totalElements % limit == 0 ? 0 : 1);
  }

  @Override
  public List<ProcessingDocument> searchByCriteria(SearchCriteriaDto searchCriteriaDto, long offset,
      long limit) {
    return
        searchQueryByCriteria(searchCriteriaDto)
            .select(
                processingDocument.id,
                processingStatusCases,
                incomingDocument.id,
                incomingDocument.name,
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
              processingDocument.setStatus(
                  ProcessingStatus.valueOf(tuple.get(processingStatusCases)));
              processingDocument.initIncomingDocument();
              processingDocument.getIncomingDoc().setId(tuple.get(incomingDocument.id));
              processingDocument.getIncomingDoc().setName(tuple.get(incomingDocument.name));
              processingDocument.getIncomingDoc()
                  .setIncomingNumber(tuple.get(incomingDocument.incomingNumber));
              processingDocument.getIncomingDoc()
                  .setOriginalSymbolNumber(tuple.get(incomingDocument.originalSymbolNumber));
              processingDocument.getIncomingDoc()
                  .setArrivingDate(tuple.get(incomingDocument.arrivingDate));
              processingDocument.getIncomingDoc().setSummary(tuple.get(incomingDocument.summary));
              processingDocument.getIncomingDoc().initSendingLevel();
              processingDocument.getIncomingDoc().getSendingLevel()
                  .setId(tuple.get(incomingDocument.sendingLevel.id));
              processingDocument.getIncomingDoc().getSendingLevel()
                  .setLevel(tuple.get(incomingDocument.sendingLevel.level));
              processingDocument.getIncomingDoc().initDocumentType();
              processingDocument.getIncomingDoc().getDocumentType()
                  .setId(tuple.get(incomingDocument.documentType.id));
              processingDocument.getIncomingDoc().getDocumentType()
                  .setType(tuple.get(incomingDocument.documentType.type));
              processingDocument.getIncomingDoc().initDistributionOrg();
              processingDocument.getIncomingDoc().getDistributionOrg()
                  .setId(tuple.get(incomingDocument.distributionOrg.id));
              processingDocument.getIncomingDoc().getDistributionOrg()
                  .setName(tuple.get(incomingDocument.distributionOrg.name));
              return processingDocument;
            })
            .toList();
  }

  @Override
  public JPAQuery<ProcessingDocument> buildSearchQuery(SearchCriteriaDto criteria) {
    return null;
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
          processingDocument.initIncomingDocument();
          processingDocument.getIncomingDoc().setId(tuple.get(incomingDocument.id));
          processingDocument.getIncomingDoc()
              .setIncomingNumber(tuple.get(incomingDocument.incomingNumber));
          processingDocument.getIncomingDoc()
              .setOriginalSymbolNumber(tuple.get(incomingDocument.originalSymbolNumber));
          processingDocument.getIncomingDoc()
              .setArrivingDate(tuple.get(incomingDocument.arrivingDate));
          processingDocument.getIncomingDoc().setSummary(tuple.get(incomingDocument.summary));
          processingDocument.getIncomingDoc().initSendingLevel();
          processingDocument.getIncomingDoc().getSendingLevel()
              .setId(tuple.get(incomingDocument.sendingLevel.id));
          processingDocument.getIncomingDoc().getSendingLevel()
              .setLevel(tuple.get(incomingDocument.sendingLevel.level));
          processingDocument.getIncomingDoc().initDocumentType();
          processingDocument.getIncomingDoc().getDocumentType()
              .setId(tuple.get(incomingDocument.documentType.id));
          processingDocument.getIncomingDoc().getDocumentType()
              .setType(tuple.get(incomingDocument.documentType.type));
          processingDocument.getIncomingDoc().initDistributionOrg();
          processingDocument.getIncomingDoc().getDistributionOrg()
              .setId(tuple.get(incomingDocument.distributionOrg.id));
          processingDocument.getIncomingDoc().getDistributionOrg()
              .setName(tuple.get(incomingDocument.distributionOrg.name));
          return processingDocument;
        })
        .toList();
  }

  private JPAQuery<IncomingDocument> searchQueryByCriteria(SearchCriteriaDto searchCriteriaDto) {
    BooleanBuilder where = new BooleanBuilder();

    if (searchCriteriaDto != null && StringUtils.isNotBlank(
        searchCriteriaDto.getIncomingNumber())) {
      where.and(incomingDocument.incomingNumber.eq(searchCriteriaDto.getIncomingNumber()));
    }
    if (searchCriteriaDto != null && StringUtils.isNotBlank(
        searchCriteriaDto.getOriginalSymbolNumber())) {
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

    User currUser = SecurityUtils.getCurrentUser();

    JPAQuery<IncomingDocument> query = selectFrom(incomingDocument)
        .leftJoin(processingDocument)
        .on(incomingDocument.id.eq(processingDocument.incomingDoc.id))
        .innerJoin(incomingDocument.sendingLevel, QSendingLevel.sendingLevel)
        .innerJoin(incomingDocument.documentType, QDocumentType.documentType)
        .innerJoin(incomingDocument.distributionOrg,
            QDistributionOrganization.distributionOrganization)
        .distinct()
        .where(where);

    if (currUser.getRole() != DocSystemRoleEnum.VAN_THU) {
      query.innerJoin(processingUser)
          .on(processingUser.processingDocument.id.eq(processingDocument.id)
              .and(processingUser.user.id.eq(currUser.getId())));
    }

    return query;
  }

  @Override
  public List<ProcessingDocument> findAllByIds(List<Long> ids) {
    return selectFrom(processingDocument)
        .from(processingDocument)
        .where(processingDocument.incomingDoc.id.in(ids))
        .fetch();
  }

  @Override
  public List<ProcessingDocument> findAllOutgoingByIds(List<Long> ids) {
    return selectFrom(processingDocument)
        .from(processingDocument)
        .where(processingDocument.outgoingDocument.id.in(ids))
        .fetch();
  }

  @Override
  public GetTransferDocumentDetailResponse getTransferDocumentDetail(
      GetTransferDocumentDetailRequest request) {
    BooleanBuilder where = new BooleanBuilder();
    where.and(incomingDocument.id.eq(request.getDocumentId()))
        .and(processingUser.user.id.eq(request.getUserId())
            .and(processingUser.step.eq(request.getStep())));
    // if role is null, get all roles
    if (request.getRole() != null) {
      where.and(processingUserRole.role.eq(request.getRole()));
    }

    return select(
        incomingDocument.id,
        incomingDocument.incomingNumber,
        incomingDocument.summary,
        processingDocument.id,
        processingDocument.status,
        processingDocument.createdDate,
        processingUser.processingDuration,
        processingUser.step,
        processingUser.processMethod,
        processingUser.user.id,
        processingUserRole.role)
        .from(incomingDocument)
        .join(processingDocument).on(incomingDocument.id.eq(processingDocument.incomingDoc.id))
        .join(processingUser).on(processingDocument.id.eq(processingUser.processingDocument.id))
        .join(processingUserRole).on(processingUser.id.eq(processingUserRole.processingUser.id))
        .where(where)
        .fetch()
        .stream()
        .findFirst()
        .map(tuple -> {
          GetTransferDocumentDetailResponse instance = new GetTransferDocumentDetailResponse();
          instance.setDocumentId(tuple.get(incomingDocument.id));
          instance.setDocumentNumber(tuple.get(incomingDocument.incomingNumber));
          instance.setSummary(tuple.get(incomingDocument.summary));
          instance.setProcessingDocumentId(tuple.get(processingDocument.id));
          instance.setProcessingStatus(tuple.get(processingDocument.status));
          instance.setTransferDate(LocalDate.from(
              Objects.requireNonNull(tuple.get(processingDocument.createdDate))));
          instance.setProcessingDuration(tuple.get(processingUser.processingDuration));
          instance.setIsInfiniteProcessingTime(
              tuple.get(processingUser.processingDuration) == null);
          instance.setStep(tuple.get(processingUser.step));
          instance.setProcessMethod(tuple.get(processingUser.processMethod));
          instance.setUserId(tuple.get(processingUser.user.id));
          instance.setRole(tuple.get(processingUserRole.role));
          return instance;
        })
        .orElse(null);
  }

  @Override
  public GetTransferDocumentDetailResponse getTransferOutgoingDocumentDetail(GetTransferDocumentDetailRequest request) {
    BooleanBuilder where = new BooleanBuilder();
    where.and(outgoingDocument.id.eq(request.getDocumentId()))
        .and(processingUser.user.id.eq(request.getUserId())
            .and(processingUser.step.eq(request.getStep())));
    if (request.getRole() != null) {
      where.and(processingUserRole.role.eq(request.getRole()));
    }

    return select(
        outgoingDocument.id,
        outgoingDocument.outgoingNumber,
        outgoingDocument.summary,
        processingDocument.id,
        processingDocument.status,
        processingDocument.createdDate,
        processingUser.processingDuration,
        processingUser.step,
        processingUser.processMethod,
        processingUser.user.id,
        processingUserRole.role)
        .from(outgoingDocument)
        .join(processingDocument).on(outgoingDocument.id.eq(processingDocument.outgoingDocument.id))
        .join(processingUser).on(processingDocument.id.eq(processingUser.processingDocument.id))
        .join(processingUserRole).on(processingUser.id.eq(processingUserRole.processingUser.id))
        .where(where)
        .fetch()
        .stream()
        .findFirst()
        .map(tuple -> {
          GetTransferDocumentDetailResponse instance = new GetTransferDocumentDetailResponse();
          instance.setDocumentId(tuple.get(outgoingDocument.id));
          instance.setDocumentNumber(tuple.get(outgoingDocument.outgoingNumber));
          instance.setSummary(tuple.get(outgoingDocument.summary));
          instance.setProcessingDocumentId(tuple.get(processingDocument.id));
          instance.setProcessingStatus(tuple.get(processingDocument.status));
          instance.setTransferDate(LocalDate.from(
              Objects.requireNonNull(tuple.get(processingDocument.createdDate))));
          instance.setProcessingDuration(tuple.get(processingUser.processingDuration));
          instance.setIsInfiniteProcessingTime(
              tuple.get(processingUser.processingDuration) == null);
          instance.setStep(tuple.get(processingUser.step));
          instance.setProcessMethod(tuple.get(processingUser.processMethod));
          instance.setUserId(tuple.get(processingUser.user.id));
          instance.setRole(tuple.get(processingUserRole.role));
          return instance;
        })
        .orElse(null);
  }

  @Override
  public List<Long> getListOfUserIdRelatedToTransferredDocument(Long processingDocumentId,
      Integer step,
      ProcessingDocumentRoleEnum role) {
    return select(processingUser.user.id)
        .from(processingUser)
        .join(processingUserRole)
        .on(processingUser.id.eq(processingUserRole.processingUser.id))
        .where(processingUser.processingDocument.id.eq(processingDocumentId)
            .and(processingUser.step.eq(step))
            .and(processingUserRole.role.eq(role)))
        .fetch()
        .stream()
        .map(tuple -> tuple.get(processingUser.user.id))
        .toList();
  }

  @Override
  public Optional<ProcessingDocument> findByIncomingDocumentId(Long incomingDocumentId) {
    return Optional.ofNullable(
        selectFrom(processingDocument)
            .where(processingDocument.incomingDoc.id.eq(incomingDocumentId))
            .fetchOne()
    );
  }

  @Override
  public Optional<ProcessingStatus> getProcessingStatus(Long documentId) {
    String result = selectFrom(processingDocument)
        .rightJoin(processingDocument.incomingDoc, incomingDocument)
        .select(processingStatusCases)
        .where(incomingDocument.id.eq(documentId))
        .fetchOne();

      return Optional.ofNullable(result)
          .map(ProcessingStatus::valueOf);
  }

  @Override
  public Tuple getCurrentStep(Long processingDocumentId){
    return select(processingUser.step)
        .from(processingUser)
        .where(processingUser.processingDocument.id.eq(processingDocumentId))
        .orderBy(processingUser.step.desc())
        .fetchFirst();
  }
}
