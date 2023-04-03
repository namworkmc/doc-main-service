package edu.hcmus.doc.mainservice.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import edu.hcmus.doc.mainservice.service.impl.DocumentTypeServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentTypeServiceTest extends AbstractServiceTest {

  private DocumentTypeService documentTypeService;

  @BeforeEach
  void setUp() {
    this.documentTypeService = new DocumentTypeServiceImpl(documentTypeRepository);
  }

  @Test
  void findDocumentTypes() {
    // Given
    DocumentType documentType1 = new DocumentType();
    documentType1.setId(1L);

    DocumentType documentType2 = new DocumentType();
    documentType2.setId(2L);

    List<DocumentType> documentTypes = new ArrayList<>();
    documentTypes.add(documentType1);
    documentTypes.add(documentType2);

    // When
    when(documentTypeRepository.findAll()).thenReturn(documentTypes);
    List<DocumentType> actual = documentTypeService.findAll();

    // Then
    verify(documentTypeRepository).findAll();
    assertThat(actual).isEqualTo(documentTypes);
  }
}
