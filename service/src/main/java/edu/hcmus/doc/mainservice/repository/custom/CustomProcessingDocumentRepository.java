package edu.hcmus.doc.mainservice.repository.custom;

import com.querydsl.core.Tuple;
import edu.hcmus.doc.mainservice.model.dto.DocListStatisticsDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailResponse;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentType;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import edu.hcmus.doc.mainservice.repository.DocAbstractSearchRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomProcessingDocumentRepository
    extends DocAbstractSearchRepository<ProcessingDocument, SearchCriteriaDto> {

  List<ProcessingDocument> findAllByIds(List<Long> ids);

  List<ProcessingDocument> findAllOutgoingByIds(List<Long> ids);

  GetTransferDocumentDetailResponse getTransferDocumentDetail(GetTransferDocumentDetailRequest request);

  List<Long> getListOfUserIdRelatedToTransferredDocument(Long processingDocumentId, Integer step, ProcessingDocumentRoleEnum role);

  GetTransferDocumentDetailResponse getTransferOutgoingDocumentDetail(GetTransferDocumentDetailRequest request);

  Tuple getCurrentStep(Long processingDocumentId);

  Optional<ProcessingDocument> findByIncomingDocumentId(Long incomingDocumentId);

  Optional<ProcessingDocument> findByOutgoingDocumentId(Long outgoingDocumentId);

  Optional<ProcessingStatus> getProcessingStatus(Long documentId);

  DocListStatisticsDto getDocListStatistics(List<Long> userId, LocalDate fromDate, LocalDate toDate, ProcessingDocumentType processingDocumentType);

  Optional<ProcessingDocument> findProcessingDocumentById(Long id);

  List<Long> getIncomingDocumentsWithTransferPermission();

  Boolean isExistUserWorkingOnThisDocumentAtSpecificStep(Long documentId, Integer step);

  boolean isDocumentClosed(Long documentId, ProcessingDocumentTypeEnum processingDocumentType);

  Optional<ProcessingDocument> findProcessingDocumentByProcessingUserId(Long processingUserId);

  List<Long>  checkIncomingDocumentSearchByCriteria(long userId, int step, ProcessingDocumentRoleEnum role);

  Map<Long, String> getProcessingTimeOfIncomingDocumentList(long userId);

  List<ProcessingDocument> searchAllByCriteria(SearchCriteriaDto searchCriteriaDto);
}
