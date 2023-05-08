package edu.hcmus.doc.mainservice.repository.custom.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import edu.hcmus.doc.mainservice.model.dto.ProcessingDetailsDto;
import edu.hcmus.doc.mainservice.model.dto.ProcessingUserDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import edu.hcmus.doc.mainservice.model.entity.QProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUserRole;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingUserRoleRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomProcessingUserRoleRepositoryImpl
    extends DocAbstractCustomRepository<ProcessingUserRole>
    implements CustomProcessingUserRoleRepository {

  @Override
  public List<ProcessingDetailsDto> getProcessingUserRolesByIncomingDocumentId(Long incomingDocumentId, boolean onlyAssignee) {
    QProcessingUser qProcessingUser = new QProcessingUser(QProcessingUserRole.processingUserRole.processingUser.getMetadata().getName());
    QProcessingDocument qProcessingDocument = new QProcessingDocument(qProcessingUser.processingDocument.getMetadata().getName());

    BooleanBuilder onlyAssigneeBuilder = new BooleanBuilder();
    if (onlyAssignee) {
      onlyAssigneeBuilder.and(QProcessingUserRole.processingUserRole.role.eq(ProcessingDocumentRoleEnum.ASSIGNEE));
    }

    return selectFrom(QProcessingUserRole.processingUserRole)
        .select(
            qProcessingDocument.incomingDoc.incomingNumber,
            qProcessingUser.step,
            qProcessingUser.user.id,
            qProcessingUser.user.fullName,
            QProcessingUserRole.processingUserRole.role,
            qProcessingUser.user.department.departmentName)
        .innerJoin(QProcessingUserRole.processingUserRole.processingUser, qProcessingUser)
        .innerJoin(qProcessingUser.processingDocument, qProcessingDocument)
        .where(qProcessingDocument.id.eq(incomingDocumentId).and(onlyAssigneeBuilder))
        .orderBy(qProcessingUser.step.asc())
        .orderBy(roleOrder())
        .stream()
        .map(tuple -> {
          ProcessingDetailsDto processingDetailsDto = new ProcessingDetailsDto();
          processingDetailsDto.setIncomingNumber(tuple.get(qProcessingDocument.incomingDoc.incomingNumber));
          processingDetailsDto.setStep(tuple.get(qProcessingUser.step));

          ProcessingUserDto processingUserDto = new ProcessingUserDto();
          processingUserDto.setId(tuple.get(qProcessingUser.user.id));
          processingUserDto.setFullName(tuple.get(qProcessingUser.user.fullName));
          processingUserDto.setRole(tuple.get(QProcessingUserRole.processingUserRole.role));
          processingUserDto.setDepartment(tuple.get(qProcessingUser.user.department.departmentName));

          processingDetailsDto.setProcessingUser(processingUserDto);
          return processingDetailsDto;
        })
        .toList();
  }

  private OrderSpecifier<Integer> roleOrder() {
    NumberExpression<Integer> target = new CaseBuilder()
        .when(QProcessingUserRole.processingUserRole.role.eq(ProcessingDocumentRoleEnum.REPORTER))
        .then(1)
        .when(QProcessingUserRole.processingUserRole.role.eq(ProcessingDocumentRoleEnum.ASSIGNEE))
        .then(2)
        .when(QProcessingUserRole.processingUserRole.role.eq(ProcessingDocumentRoleEnum.COLLABORATOR))
        .then(3)
        .otherwise(4);

    return new OrderSpecifier<>(Order.ASC, target);
  }
}
