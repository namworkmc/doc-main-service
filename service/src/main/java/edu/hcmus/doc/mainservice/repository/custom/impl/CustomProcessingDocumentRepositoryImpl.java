package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QIncomingDocument.incomingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QOutgoingDocument.outgoingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingDocument.processingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingMethod.processingMethod;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingUser.processingUser;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingUserRole.processingUserRole;
import static edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum.ASSIGNEE;
import static edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum.COLLABORATOR;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.dto.DocDetailStatisticsDto;
import edu.hcmus.doc.mainservice.model.dto.DocListStatisticsDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailResponse;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.QDistributionOrganization;
import edu.hcmus.doc.mainservice.model.entity.QDocumentType;
import edu.hcmus.doc.mainservice.model.entity.QProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUserRole;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.MESSAGE;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentType;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.util.DocMessageUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class CustomProcessingDocumentRepositoryImpl
    extends DocAbstractCustomRepository<ProcessingDocument>
    implements CustomProcessingDocumentRepository {

  private static final String STATUS_ALIAS = "status";

  private final StringExpression processingStatusCases = Expressions
      .cases()
      .when(processingDocument.status.eq(ProcessingStatus.IN_PROGRESS))
      .then(ProcessingStatus.IN_PROGRESS.value)
      .when(processingDocument.status.eq(ProcessingStatus.CLOSED))
      .then(ProcessingStatus.CLOSED.value)
      .otherwise(ProcessingStatus.UNPROCESSED.value)
      .as(STATUS_ALIAS);

  private final StringExpression documentCase = Expressions
      .cases()
      .when(processingUserRole.role.eq(ProcessingDocumentRoleEnum.ASSIGNEE))
      .then("received_document")
      .when(processingUserRole.role.eq(ProcessingDocumentRoleEnum.REPORTER))
      .then("transferred_document")
      .otherwise("irrelevant_document");

  @Override
  public long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
    if (searchCriteriaDto.getStatus() != null) {
      return Optional.ofNullable(searchQueryByCriteria(searchCriteriaDto)
          .select(processingStatusCases)
          .where(Expressions.stringPath(STATUS_ALIAS).eq(searchCriteriaDto.getStatus().value))
          .select(processingDocument.id.count())
          .fetchFirst())
          .orElse(0L);
    }

    return Optional.ofNullable(searchQueryByCriteria(searchCriteriaDto)
        .select(incomingDocument.id.count())
        .fetchFirst())
        .orElse(0L);
  }

  @Override
  public long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit) {
    long totalElements = getTotalElements(searchCriteriaDto);
    return (totalElements / limit) + (totalElements % limit == 0 ? 0 : 1);
  }

  @Override
  public List<ProcessingDocument> searchByCriteria(SearchCriteriaDto searchCriteriaDto, long offset,
      long limit) {
    BooleanBuilder where = new BooleanBuilder();
    if (searchCriteriaDto.getStatus() != null) {
      where.and(Expressions.stringPath(STATUS_ALIAS).eq(searchCriteriaDto.getStatus().value));
    }

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
                incomingDocument.documentType.id,
                incomingDocument.documentType.type,
                incomingDocument.distributionOrg.id,
                incomingDocument.distributionOrg.name)
            .where(where)
            .orderBy(incomingDocument.id.desc())
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
              processingDocument.getIncomingDoc().setSendingLevel(null);
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
  public List<ProcessingDocument> searchAllByCriteria(SearchCriteriaDto searchCriteriaDto) {
    BooleanBuilder where = new BooleanBuilder();
    if (searchCriteriaDto.getStatus() != null) {
      where.and(Expressions.stringPath(STATUS_ALIAS).eq(searchCriteriaDto.getStatus().value));
    }

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
                incomingDocument.documentType.id,
                incomingDocument.documentType.type,
                incomingDocument.distributionOrg.id,
                incomingDocument.distributionOrg.name)
            .where(where)
            .orderBy(incomingDocument.id.desc())
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
              processingDocument.getIncomingDoc().setSendingLevel(null);
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

    if (StringUtils.isNotBlank(
        searchCriteriaDto.getIncomingNumber())) {
      where.and(incomingDocument.incomingNumber.containsIgnoreCase(searchCriteriaDto.getIncomingNumber().trim()));
    }
    if (StringUtils.isNotBlank(
        searchCriteriaDto.getOriginalSymbolNumber())) {
      where.and(
          incomingDocument.originalSymbolNumber.containsIgnoreCase(searchCriteriaDto.getOriginalSymbolNumber().trim()));
    }
    if (searchCriteriaDto.getDocumentTypeId() != null) {
      where.and(incomingDocument.documentType.id.eq(searchCriteriaDto.getDocumentTypeId()));
    }
    if (searchCriteriaDto.getDistributionOrgId() != null) {
      where.and(incomingDocument.distributionOrg.id.eq(searchCriteriaDto.getDistributionOrgId()));
    }
    if (searchCriteriaDto.getArrivingDateFrom() != null
        && searchCriteriaDto.getArrivingDateTo() != null) {
      where.and(incomingDocument.arrivingDate.between(
          searchCriteriaDto.getArrivingDateFrom().atStartOfDay().toLocalDate(),
          searchCriteriaDto.getArrivingDateTo().plusDays(1).atStartOfDay().toLocalDate()
      ));
    }
    if (searchCriteriaDto.getProcessingDurationFrom() != null
        && searchCriteriaDto.getProcessingDurationTo() != null) {
      where.and(incomingDocument.arrivingDate.between(
          searchCriteriaDto.getProcessingDurationFrom().atStartOfDay().toLocalDate(),
          searchCriteriaDto.getProcessingDurationTo().plusDays(1).atStartOfDay().toLocalDate()
      ));
    }
    if (StringUtils.isNotBlank(searchCriteriaDto.getSummary())) {
      where.and(incomingDocument.summary.containsIgnoreCase(searchCriteriaDto.getSummary().trim()));
    }
    if (StringUtils.isNotBlank(searchCriteriaDto.getDocumentName())) {
      where.and(incomingDocument.name.containsIgnoreCase(searchCriteriaDto.getDocumentName().trim()));
    }

    return selectFrom(incomingDocument)
        .leftJoin(processingDocument)
        .on(incomingDocument.id.eq(processingDocument.incomingDoc.id))
        .innerJoin(incomingDocument.documentType, QDocumentType.documentType)
        .innerJoin(incomingDocument.distributionOrg,
            QDistributionOrganization.distributionOrganization)
        .distinct()
        .where(where);
  }

  @Override
  public List<Long> checkIncomingDocumentSearchByCriteria(long userId, int step, ProcessingDocumentRoleEnum role) {

    return selectFrom(incomingDocument)
        .select(incomingDocument.id)
        .leftJoin(processingDocument).on(incomingDocument.id.eq(processingDocument.incomingDoc.id)).fetchJoin()
        .innerJoin(processingUser).on(processingUser.processingDocument.id.eq(processingDocument.id).and(processingUser.user.id.eq(userId)).and(processingUser.step.eq(step))).fetchJoin()
        .innerJoin(processingUserRole).on(processingUser.id.eq(processingUserRole.processingUser.id).and(processingUserRole.role.eq(role))).fetchJoin()
        .distinct()
        .fetch();
  }

  @Override
  public Map<Long, String> getProcessingTimeOfIncomingDocumentList(long userId) {
    return selectFrom(incomingDocument)
        .select(incomingDocument.id, processingUser.processingDuration)
        .leftJoin(processingDocument)
        .on(incomingDocument.id.eq(processingDocument.incomingDoc.id)).fetchJoin()
        .innerJoin(processingUser).on(processingUser.processingDocument.id.eq(processingDocument.id)
            .and(processingUser.user.id.eq(userId))).fetchJoin()
        .innerJoin(processingUserRole).on(processingUser.id.eq(processingUserRole.processingUser.id)
            .and(processingUserRole.role.in(ASSIGNEE, COLLABORATOR))).fetchJoin()
        .distinct()
        .fetch()
        .stream()
        .collect(Collectors.toMap(
            tuple -> tuple.get(incomingDocument.id),
            tuple -> {
              LocalDate processingDuration = tuple.get(processingUser.processingDuration
              );
              if (processingDuration != null) {
                return processingDuration.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
              } else {
                return DocMessageUtils.getContent(MESSAGE.infinite_processing_duration);
              }
            }
        ));
  }

  @Override
  public List<Long> getIncomingDocumentsWithTransferPermission() {
    BooleanBuilder where = new BooleanBuilder();

    User currUser = SecurityUtils.getCurrentUser();
    where.and(incomingDocument.createdBy.eq(currUser.getUsername())
        .or(processingUser.user.id.eq(currUser.getId())
            .and(processingDocument.status.eq(ProcessingStatus.CLOSED).not())
            .and(processingUserRole.role.eq(ASSIGNEE))));

    return selectFrom(incomingDocument)
        .select(incomingDocument.id)
        .leftJoin(processingDocument)
        .on(incomingDocument.id.eq(processingDocument.incomingDoc.id))
        .leftJoin(processingUser)
        .on(processingUser.processingDocument.id.eq(processingDocument.id))
        .fetchJoin()
        .leftJoin(processingUserRole)
        .on(processingUser.id.eq(processingUserRole.processingUser.id))
        .distinct()
        .where(where)
        .fetch();
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
        processingMethod.name,
        processingUser.user.id,
        processingUserRole.role)
        .from(incomingDocument)
        .join(processingDocument).on(incomingDocument.id.eq(processingDocument.incomingDoc.id))
        .join(processingUser).on(processingDocument.id.eq(processingUser.processingDocument.id))
        .leftJoin(processingMethod).on(processingUser.processingMethod.id.eq(processingMethod.id))
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
          instance.setProcessingMethod(tuple.get(processingMethod.name));
          instance.setUserId(tuple.get(processingUser.user.id));
          instance.setRole(tuple.get(processingUserRole.role));
          return instance;
        })
        .orElse(null);
  }

  @Override
  public GetTransferDocumentDetailResponse getTransferOutgoingDocumentDetail(
      GetTransferDocumentDetailRequest request) {
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
        processingMethod.name,
        processingUser.user.id,
        processingUserRole.role)
        .from(outgoingDocument)
        .join(processingDocument).on(outgoingDocument.id.eq(processingDocument.outgoingDocument.id))
        .join(processingUser).on(processingDocument.id.eq(processingUser.processingDocument.id))
        .leftJoin(processingMethod).on(processingUser.processingMethod.id.eq(processingMethod.id))
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
          instance.setProcessingMethod(tuple.get(processingMethod.name));
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
  public Optional<ProcessingDocument> findByOutgoingDocumentId(Long outgoingDocumentId) {
    return Optional.ofNullable(
        selectFrom(processingDocument)
            .where(processingDocument.outgoingDocument.id.eq(outgoingDocumentId))
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
  public Tuple getCurrentStep(Long processingDocumentId) {
    return select(processingUser.step)
        .from(processingUser)
        .where(processingUser.processingDocument.id.eq(processingDocumentId))
        .orderBy(processingUser.step.desc())
        .fetchFirst();
  }

  @Override
  public Optional<ProcessingDocument> findProcessingDocumentById(Long id) {
    return Optional.ofNullable(
        selectFrom(processingDocument)
            .where(processingDocument.id.eq(id))
            .fetchOne()
    );

  }

  @Override
  public DocListStatisticsDto getDocListStatistics(List<Long> userIdList, LocalDate fromDate,
      LocalDate toDate, ProcessingDocumentType processingDocumentType) {
    QProcessingDocument qProcessingDocument = processingDocument;
    QProcessingUser qProcessingUser = processingUser;
    QProcessingUserRole qProcessingUserRole = processingUserRole;

    BooleanBuilder where = new BooleanBuilder();

    if (ProcessingDocumentType.INCOMING_DOCUMENT.equals(processingDocumentType)) {
      where.and(qProcessingDocument.incomingDoc.isNotNull());
    } else {
      where.and(qProcessingDocument.outgoingDocument.isNotNull());
    }

    if (fromDate != null && toDate != null) {
      where.and(qProcessingUserRole.createdDate.between(
          fromDate.atStartOfDay(),
          toDate.plusDays(1).atStartOfDay()
      ));
    }

    Map<String, Map<Long, List<DocDetailStatisticsDto>>> receivedDocuments = new HashMap<>();

    List<Tuple> queryResults = selectFrom(qProcessingDocument)
        .distinct()
        .select(
            documentCase,
            qProcessingDocument.id,
            qProcessingDocument.incomingDoc.id,
            qProcessingDocument.outgoingDocument.id,
            qProcessingDocument.status,
            qProcessingUser.user.id,
            qProcessingUser.processingDuration,
            qProcessingUserRole.createdDate
        )
        .leftJoin(qProcessingUser)
        .on(qProcessingDocument.id.eq(qProcessingUser.processingDocument.id))
        .leftJoin(qProcessingUserRole)
        .on(qProcessingUser.id.eq(qProcessingUserRole.processingUser.id))
        .where(
            qProcessingUser.user.id.in(userIdList)  // Assuming userIdList is a List<Long>
                .and(where)
        )
        .fetch();

    for (Tuple tuple : queryResults) {
      String caseType = tuple.get(documentCase);
      Long documentId = tuple.get(qProcessingDocument.id);
      Long incomingDocId = tuple.get(qProcessingDocument.incomingDoc.id);
      Long outgoingDocId = tuple.get(qProcessingDocument.outgoingDocument.id);
      Long userId = tuple.get(qProcessingUser.user.id);
      ProcessingStatus status = tuple.get(qProcessingDocument.status);
      LocalDate processingDuration = tuple.get(qProcessingUser.processingDuration);
      LocalDate createdDate = Objects.isNull(qProcessingUserRole.createdDate) ? null : Objects.requireNonNull(
          tuple.get(qProcessingUserRole.createdDate)).toLocalDate();

      DocDetailStatisticsDto docDetailStatisticsDto = new DocDetailStatisticsDto();
      docDetailStatisticsDto.setDocId(documentId);
      docDetailStatisticsDto.setIncomingDocId(incomingDocId);
      docDetailStatisticsDto.setOutgoingDocId(outgoingDocId);
      docDetailStatisticsDto.setStatus(status);
      docDetailStatisticsDto.setProcessingDuration(processingDuration);
      docDetailStatisticsDto.setCreatedDate(createdDate);

      receivedDocuments
          .computeIfAbsent(caseType, k -> new HashMap<>())
          .computeIfAbsent(userId, k -> new ArrayList<>())
          .add(docDetailStatisticsDto);
    }

    Map<Long, List<DocDetailStatisticsDto>> receivedDocs = receivedDocuments.getOrDefault("received_document", new HashMap<>());
    Map<Long, List<DocDetailStatisticsDto>> transferredDocs = receivedDocuments.getOrDefault("transferred_document", new HashMap<>());
    DocListStatisticsDto docListStatisticsDto = new DocListStatisticsDto();

    docListStatisticsDto.setReceivedDocuments(receivedDocs);
    docListStatisticsDto.setTransferredDocuments(transferredDocs);

    return docListStatisticsDto;
  }

  /**
   * Check if there is a user working on this document at specific step, if step is null that means every step is acceptable
   * @param documentId Long
   * @param step Integer
   * @return Boolean
   */
  @Override
  public Boolean isExistUserWorkingOnThisDocumentAtSpecificStep(Long documentId, Integer step) {
    // check in processing user table, if there is a record with the same document id then return true, else return false
    BooleanBuilder whereBuilder = new BooleanBuilder();
    whereBuilder.and(processingUser.processingDocument.id.eq(documentId));
    if (step != null) {
      whereBuilder.and(processingUser.step.eq(step));
    }

    ProcessingUser result = selectFrom(processingUser)
        .where(whereBuilder)
        .fetchFirst();
    return Objects.nonNull(result);
  }

  @Override
  public boolean isDocumentClosed(Long documentId, ProcessingDocumentTypeEnum processingDocumentType) {
    BooleanBuilder whereBuilder = new BooleanBuilder();
    if (ProcessingDocumentTypeEnum.INCOMING_DOCUMENT.equals(processingDocumentType)) {
      whereBuilder.and(processingDocument.incomingDoc.id.eq(documentId));
    } else {
      whereBuilder.and(processingDocument.outgoingDocument.id.eq(documentId));
    }

    ProcessingDocument result = selectFrom(processingDocument)
        .where(whereBuilder)
        .fetchOne();

    if (Objects.nonNull(result)) {
      return result.getStatus() == ProcessingStatus.CLOSED;
    }
    return false;
  }

  @Override
  public Optional<ProcessingDocument> findProcessingDocumentByProcessingUserId(
      Long processingUserId) {
    return Optional.ofNullable(selectFrom(processingDocument)
        .join(processingUser)
        .on(processingUser.processingDocument.id.eq(processingDocument.id))
        .where(processingUser.id.eq(processingUserId))
        .fetchOne());
  }
}
