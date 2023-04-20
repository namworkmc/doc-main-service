package edu.hcmus.doc.mainservice.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.service.impl.ProcessingDocumentServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;

class ProcessingDocumentServiceTest extends AbstractServiceTest {

  private ProcessingDocumentService processingDocumentService;

  @Mock
  private AsyncRabbitTemplate asyncRabbitTemplate;

  @BeforeEach
  void setUp() {
    this.processingDocumentService = new ProcessingDocumentServiceImpl(
        processingDocumentRepository,
        asyncRabbitTemplate
    );
  }

  @Test
  void testGetTotalElements() {
    // Given
    SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto();

    // When
    when(processingDocumentRepository.getTotalElements(searchCriteriaDto)).thenReturn(12L);
    long actual = processingDocumentService.getTotalElements(searchCriteriaDto);

    // Then
    ArgumentCaptor<SearchCriteriaDto> searchCriteriaDtoArgumentCaptor = ArgumentCaptor.forClass(
        SearchCriteriaDto.class);
    verify(processingDocumentRepository).getTotalElements(
        searchCriteriaDtoArgumentCaptor.capture());

    assertThat(searchCriteriaDtoArgumentCaptor.getValue()).isEqualTo(searchCriteriaDto);
    assertThat(actual).isEqualTo(12L);
  }

  @Test
  void testGetTotalPages() {
    // Given
    SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto();
    int pageSize = 3;

    // When
    when(processingDocumentRepository.getTotalPages(searchCriteriaDto, pageSize)).thenReturn(4L);
    long actual = processingDocumentService.getTotalPages(searchCriteriaDto, pageSize);

    // Then
    ArgumentCaptor<SearchCriteriaDto> searchCriteriaDtoArgumentCaptor = ArgumentCaptor.forClass(
        SearchCriteriaDto.class);
    ArgumentCaptor<Integer> pageSizeArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(processingDocumentRepository).getTotalPages(searchCriteriaDtoArgumentCaptor.capture(),
        pageSizeArgumentCaptor.capture());

    assertThat(searchCriteriaDtoArgumentCaptor.getValue()).isEqualTo(searchCriteriaDto);
    assertThat(actual).isEqualTo(4L);
  }

  @Test
  void testSearchProcessingDocuments() {
    // Given
    SearchCriteriaDto searchCriteriaDto = new SearchCriteriaDto();
    long offset = 0;
    long limit = 3;

    List<ProcessingDocument> processingDocuments = new ArrayList<>();
    ProcessingDocument document1 = new ProcessingDocument();
    document1.setId(1L);
    processingDocuments.add(document1);
    ProcessingDocument document2 = new ProcessingDocument();
    document2.setId(2L);
    processingDocuments.add(document2);

    // When
    when(processingDocumentRepository
        .searchByCriteria(searchCriteriaDto, offset, limit))
        .thenReturn(processingDocuments);

    List<ProcessingDocument> actual = processingDocumentService
        .searchProcessingDocuments(searchCriteriaDto, offset, limit);

    // Then
    ArgumentCaptor<SearchCriteriaDto> searchCriteriaDtoArgumentCaptor
        = ArgumentCaptor.forClass(SearchCriteriaDto.class);
    ArgumentCaptor<Long> offsetArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Long> limitArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    verify(processingDocumentRepository)
        .searchByCriteria(
            searchCriteriaDtoArgumentCaptor.capture(),
            offsetArgumentCaptor.capture(),
            limitArgumentCaptor.capture()
        );

    assertThat(searchCriteriaDtoArgumentCaptor.getValue()).isEqualTo(searchCriteriaDto);
    assertThat(offsetArgumentCaptor.getValue()).isEqualTo(offset);
    assertThat(limitArgumentCaptor.getValue()).isEqualTo(limit);
    assertThat(actual).isEqualTo(processingDocuments);
    assertThat(2).isEqualTo(actual.size());
    assertThat(document1).isEqualTo(actual.get(0));
    assertThat(document2).isEqualTo(actual.get(1));
  }
}
