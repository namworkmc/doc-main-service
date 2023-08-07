package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QLinkedDocument.linkedDocument;
import static edu.hcmus.doc.mainservice.model.entity.QOutgoingDocument.outgoingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingDocument.processingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingUser.processingUser;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingUserRole.processingUserRole;
import static edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum.ASSIGNEE;
import static edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum.COLLABORATOR;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.entity.QDepartment;
import edu.hcmus.doc.mainservice.model.entity.QDocumentType;
import edu.hcmus.doc.mainservice.model.entity.QFolder;
import edu.hcmus.doc.mainservice.model.entity.QOutgoingDocument;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.MESSAGE;
import edu.hcmus.doc.mainservice.model.enums.OutgoingDocumentStatusEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomOutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.util.DocMessageUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class CustomOutgoingDocumentRepositoryImpl
    extends DocAbstractCustomRepository<OutgoingDocument>
    implements CustomOutgoingDocumentRepository {

  @Override
  public OutgoingDocument getOutgoingDocumentById(Long id) {
    return selectFrom(QOutgoingDocument.outgoingDocument)
        .join(QOutgoingDocument.outgoingDocument.folder, QFolder.folder)
        .fetchJoin()
        .join(QOutgoingDocument.outgoingDocument.documentType, QDocumentType.documentType)
        .fetchJoin()
        .join(QOutgoingDocument.outgoingDocument.publishingDepartment, QDepartment.department)
        .fetchJoin()
        .where(QOutgoingDocument.outgoingDocument.id.eq(id))
        .fetchFirst();
  }

  @Override
  public long getTotalElements(OutgoingDocSearchCriteriaDto searchCriteriaDto) {
    return buildSearchQuery(searchCriteriaDto)
        .fetch()
        .size();
  }

  @Override
  public long getTotalPages(OutgoingDocSearchCriteriaDto searchCriteriaDto, long limit) {
    long totalElements = getTotalElements(searchCriteriaDto);
    return (totalElements / limit) + (totalElements % limit == 0 ? 0 : 1);
  }

  @Override
  public List<OutgoingDocument> searchByCriteria(OutgoingDocSearchCriteriaDto searchCriteriaDto,
      long offset, long limit) {
    return
        buildSearchQuery(searchCriteriaDto)
            .orderBy(outgoingDocument.id.desc())
            .offset(offset * limit)
            .limit(limit)
            .fetch();
  }

  @Override
  public JPAQuery<OutgoingDocument> buildSearchQuery(OutgoingDocSearchCriteriaDto searchCriteriaDto) {
    BooleanBuilder where = new BooleanBuilder();

    if (StringUtils.isNotBlank(searchCriteriaDto.getOutgoingNumber())) {
      where.and(outgoingDocument.outgoingNumber.eq(searchCriteriaDto.getOutgoingNumber()));
    }
    if (StringUtils.isNotBlank(
        searchCriteriaDto.getOriginalSymbolNumber())) {
      where.and(
          outgoingDocument.originalSymbolNumber.eq(searchCriteriaDto.getOriginalSymbolNumber()));
    }
    if (searchCriteriaDto.getDocumentTypeId() != null) {
      where.and(outgoingDocument.documentType.id.eq(searchCriteriaDto.getDocumentTypeId()));
    }
    if (searchCriteriaDto.getReleaseDateFrom() != null && searchCriteriaDto.getReleaseDateTo() != null) {
      where.and(outgoingDocument.releaseDate.between(
          searchCriteriaDto.getReleaseDateFrom().atStartOfDay().toLocalDate(),
          searchCriteriaDto.getReleaseDateTo().plusDays(1).atStartOfDay().toLocalDate()
      ));
    }
    if (StringUtils.isNotBlank(searchCriteriaDto.getSummary())) {
      where.and(outgoingDocument.summary.containsIgnoreCase(searchCriteriaDto.getSummary().trim()));
    }
    if (searchCriteriaDto.getStatus() != null) {
      where.and(outgoingDocument.status.eq(searchCriteriaDto.getStatus()));
    }
    if (StringUtils.isNotBlank(searchCriteriaDto.getDocumentName())) {
      where.and(outgoingDocument.name.containsIgnoreCase(searchCriteriaDto.getDocumentName().trim()));
    }

    return selectFrom(outgoingDocument)
        .leftJoin(outgoingDocument.documentType)
        .fetchJoin()
        .leftJoin(outgoingDocument.publishingDepartment)
        .fetchJoin()
        .distinct()
        .where(where);
  }

  @Override
  public List<Long> checkOutgoingDocumentSearchByCriteria(long userId, int step, ProcessingDocumentRoleEnum role) {

    return selectFrom(outgoingDocument)
        .select(outgoingDocument.id)
        .leftJoin(processingDocument).on(outgoingDocument.id.eq(processingDocument.outgoingDocument.id)).fetchJoin()
        .innerJoin(processingUser).on(processingUser.processingDocument.id.eq(processingDocument.id).and(processingUser.user.id.eq(userId)).and(processingUser.step.eq(step))).fetchJoin()
        .innerJoin(processingUserRole).on(processingUser.id.eq(processingUserRole.processingUser.id).and(processingUserRole.role.eq(role))).fetchJoin()
        .distinct()
        .fetch();
  }

  @Override
  public Map<Long, String> getProcessingTimeOfOutgoingDocumentList(long userId) {
    return selectFrom(outgoingDocument)
        .select(outgoingDocument.id, processingUser.processingDuration)
        .leftJoin(processingDocument)
        .on(outgoingDocument.id.eq(processingDocument.outgoingDocument.id)).fetchJoin()
        .innerJoin(processingUser).on(processingUser.processingDocument.id.eq(processingDocument.id)
            .and(processingUser.user.id.eq(userId))).fetchJoin()
        .innerJoin(processingUserRole).on(processingUser.id.eq(processingUserRole.processingUser.id)
            .and(processingUserRole.role.in(ASSIGNEE, COLLABORATOR))).fetchJoin()
        .distinct()
        .fetch()
        .stream()
        .collect(Collectors.toMap(
            tuple -> tuple.get(outgoingDocument.id),
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
  public List<Long> getOutgoingDocumentsWithTransferPermission() {
    BooleanBuilder where = new BooleanBuilder();

    User currUser = SecurityUtils.getCurrentUser();
    where.and(outgoingDocument.createdBy.eq(currUser.getUsername()).or(processingUser.user.id.eq(currUser.getId()).and(processingUserRole.role.eq(ASSIGNEE))));

    return selectFrom(outgoingDocument)
        .select(outgoingDocument.id)
        .leftJoin(processingDocument)
        .on(outgoingDocument.id.eq(processingDocument.outgoingDocument.id))
        .fetchJoin()
        .innerJoin(processingUser)
        .on(processingUser.processingDocument.id.eq(processingDocument.id))
        .fetchJoin()
        .innerJoin(processingUserRole)
        .on(processingUser.id.eq(processingUserRole.processingUser.id))
        .distinct()
        .where(where.and(outgoingDocument.status.eq(OutgoingDocumentStatusEnum.RELEASED).not()))
        .fetch();
  }

  @Override
  public List<OutgoingDocument> getOutgoingDocumentsByIds(List<Long> ids) {
    return selectFrom(outgoingDocument)
        .where(outgoingDocument.id.in(ids))
        .fetch();
  }

  @Override
  public List<OutgoingDocument> getDocumentsLinkedToIncomingDocument(Long sourceDocumentId) {
    return selectFrom(linkedDocument)
            .where(linkedDocument.incomingDocument.id.eq(sourceDocumentId))
            .stream()
            .map(linkedDocument -> getOutgoingDocumentById(linkedDocument.getOutgoingDocument().getId()))
            .collect(Collectors.toList());
  }

  @Override
  public boolean isDocumentReleased(Long documentId) {
    OutgoingDocument result = selectFrom(outgoingDocument)
        .where(outgoingDocument.id.eq(documentId))
        .fetchOne();

    if (Objects.nonNull(result)) {
      return result.getStatus() == OutgoingDocumentStatusEnum.RELEASED;
    }
    return false;
  }
}
