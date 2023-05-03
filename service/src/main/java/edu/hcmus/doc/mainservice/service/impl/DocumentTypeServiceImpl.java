package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import edu.hcmus.doc.mainservice.repository.DocumentTypeRepository;
import edu.hcmus.doc.mainservice.service.DocumentTypeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class DocumentTypeServiceImpl implements DocumentTypeService {
    private final DocumentTypeRepository documentTypeRepository;

    @Override
    public List<DocumentType> findAll() {
        return documentTypeRepository.findAll();
    }

    @Override
    public DocumentType findById(Long id) {
        return documentTypeRepository.findById(id).orElse(null);
    }

  @Override
  public DocumentType createDocumentType(DocumentType documentType) {
    return documentTypeRepository.save(documentType);
  }
}
