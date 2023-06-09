package edu.hcmus.doc.mainservice.repository.custom.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import edu.hcmus.doc.mainservice.model.dto.ProcessingDetailsDto;
import edu.hcmus.doc.mainservice.model.dto.ProcessingUserDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import edu.hcmus.doc.mainservice.model.entity.QProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUserRole;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingUserRoleRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomProcessingUserRoleRepositoryImpl
    extends DocAbstractCustomRepository<ProcessingUserRole>
    implements CustomProcessingUserRoleRepository {

  private static final QProcessingUserRole qProcessingUserRole = QProcessingUserRole.processingUserRole;

  private static final OrderSpecifier<Integer> ROLE_ORDER = new OrderSpecifier<>(
      Order.ASC,
      new CaseBuilder()
          .when(qProcessingUserRole.role.eq(ProcessingDocumentRoleEnum.REPORTER))
          .then(1)
          .when(qProcessingUserRole.role.eq(ProcessingDocumentRoleEnum.ASSIGNEE))
          .then(2)
          .when(qProcessingUserRole.role.eq(ProcessingDocumentRoleEnum.COLLABORATOR))
          .then(3)
          .otherwise(4)
  );

  @Override
  public List<ProcessingDetailsDto> getProcessingUserRolesByTypeAndDocumentId(
      ProcessingDocumentTypeEnum processingDocumentType, Long documentId, boolean onlyAssignee) {
    QProcessingUser qProcessingUser = new QProcessingUser(
        qProcessingUserRole.processingUser.getMetadata().getName());
    QProcessingDocument qProcessingDocument = new QProcessingDocument(
        qProcessingUser.processingDocument.getMetadata().getName());

    BooleanBuilder criteria = new BooleanBuilder();
    Expression<?>[] selectExpressions;
    if (onlyAssignee) {
      criteria.and(
          QProcessingUserRole.processingUserRole.role.eq(ProcessingDocumentRoleEnum.ASSIGNEE));
    }

    if (processingDocumentType == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {
      criteria.and(qProcessingDocument.incomingDoc.id.eq(documentId));
      selectExpressions = new Expression[]{
          qProcessingDocument.incomingDoc.incomingNumber,
          qProcessingUser.step,
          qProcessingUser.user.id,
          qProcessingUser.user.fullName,
          qProcessingUser.user.role,
          qProcessingUserRole.role,
          qProcessingUser.user.department.departmentName
      };
    } else {
      criteria.and(qProcessingDocument.outgoingDocument.id.eq(documentId));
      selectExpressions = new Expression[]{
          qProcessingDocument.outgoingDocument.outgoingNumber,
          qProcessingUser.step,
          qProcessingUser.user.id,
          qProcessingUser.user.fullName,
          qProcessingUser.user.role,
          qProcessingUserRole.role,
          qProcessingUser.user.department.departmentName
      };
    }

    return selectFrom(qProcessingUserRole)
        .select(selectExpressions)
        .innerJoin(qProcessingUserRole.processingUser, qProcessingUser)
        .innerJoin(qProcessingUser.processingDocument, qProcessingDocument)
        .where(criteria)
        .orderBy(qProcessingUser.step.asc())
        .orderBy(ROLE_ORDER)
        .stream()
        .map(tuple -> {
          ProcessingDetailsDto processingDetailsDto = new ProcessingDetailsDto();
          if (processingDocumentType == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {

            processingDetailsDto.setIncomingNumber(
                tuple.get(qProcessingDocument.incomingDoc.incomingNumber));
          } else {
            processingDetailsDto.setOutgoingNumber(
                tuple.get(qProcessingDocument.outgoingDocument.outgoingNumber));
          }
          processingDetailsDto.setStep(tuple.get(qProcessingUser.step));

          ProcessingUserDto processingUserDto = new ProcessingUserDto();
          processingUserDto.setId(tuple.get(qProcessingUser.user.id));
          processingUserDto.setFullName(tuple.get(qProcessingUser.user.fullName));
          processingUserDto.setRole(tuple.get(qProcessingUserRole.role));
          processingUserDto.setDocSystemRole(tuple.get(qProcessingUser.user.role));
          processingUserDto.setDepartment(
              tuple.get(qProcessingUser.user.department.departmentName));

          processingDetailsDto.setProcessingUser(processingUserDto);
          return processingDetailsDto;
        })
        .toList();
  }
}
