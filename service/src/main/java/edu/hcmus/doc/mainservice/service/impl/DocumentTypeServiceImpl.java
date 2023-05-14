package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.DocumentTypeDto;
import edu.hcmus.doc.mainservice.model.dto.DocumentTypeSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import edu.hcmus.doc.mainservice.repository.DocumentTypeRepository;
import edu.hcmus.doc.mainservice.service.DocumentTypeService;
import edu.hcmus.doc.mainservice.util.mapper.DocumentTypeMapper;
import edu.hcmus.doc.mainservice.util.mapper.PaginationMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class DocumentTypeServiceImpl implements DocumentTypeService {

  private final DocumentTypeRepository documentTypeRepository;
  private final DocumentTypeMapper documentTypeMapper;
  private final PaginationMapper paginationMapper;

  @Override
  public List<DocumentType> findAll() {
    return documentTypeRepository.findAll();
  }

  @Override
  public DocumentType findById(Long id) {
    return documentTypeRepository.findById(id).orElse(null);
  }

  @Override
  public DocumentType saveDocumentType(DocumentType documentType) {
    return documentTypeRepository.save(documentType);
  }

  @Override
  public void deleteDocumentTypes(List<Long> documentTypeIds) {
    List<DocumentType> documentTypes = documentTypeRepository.findAllById(documentTypeIds);
    documentTypes.parallelStream().forEach(documentType -> documentType.setDeleted(true));
    documentTypeRepository.saveAll(documentTypes);
  }

  @Override
  public DocPaginationDto<DocumentTypeDto> search(DocumentTypeSearchCriteria criteria, int page, int pageSize) {
    long totalElements = documentTypeRepository.getTotalElements(criteria);
    long totalPages = (totalElements / pageSize) + (totalElements % pageSize == 0 ? 0 : 1);
    List<DocumentTypeDto> documentTypeDtos = documentTypeRepository
        .searchByCriteria(criteria, page, pageSize)
        .stream()
        .map(documentTypeMapper::toDto)
        .toList();

    return paginationMapper.toDto(documentTypeDtos, totalElements, totalPages);
  }
}
