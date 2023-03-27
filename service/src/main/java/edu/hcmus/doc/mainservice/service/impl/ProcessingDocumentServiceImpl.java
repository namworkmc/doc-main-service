package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.ElasticSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocumentSearchResultDto;
import edu.hcmus.doc.mainservice.model.dto.ProcessingDocumentSearchResultDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate.RabbitConverterFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class ProcessingDocumentServiceImpl implements ProcessingDocumentService {

  private final ProcessingDocumentRepository processingDocumentRepository;

  private final AsyncRabbitTemplate asyncRabbitTemplate;

  @Value("${spring.rabbitmq.template.exchange}")
  private String exchange;

  @Value("${spring.rabbitmq.template.routing-key}")
  private String routingkey;

  @Override
  public long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
    return processingDocumentRepository.getTotalElements(searchCriteriaDto);
  }

  @Override
  public long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit) {
    return processingDocumentRepository.getTotalPages(searchCriteriaDto, limit);
  }

  @Override
  public List<ProcessingDocument> searchProcessingDocuments(
      SearchCriteriaDto searchCriteriaDto,
      long offset,
      long limit
  ) {
    return processingDocumentRepository.searchByCriteria(searchCriteriaDto, offset, limit);
  }

  @Override
  public ProcessingDocumentSearchResultDto searchProcessingDocumentsByElasticSearch(
      ElasticSearchCriteriaDto elasticSearchCriteriaDto, long offset, long limit)
      throws ExecutionException, InterruptedException {
    ProcessingDocumentSearchResultDto processingDocumentSearchResultDto = new ProcessingDocumentSearchResultDto();
    RabbitConverterFuture<List<IncomingDocumentSearchResultDto>> rabbitConverterFuture = asyncRabbitTemplate.convertSendAndReceiveAsType(
        exchange,
        routingkey,
        elasticSearchCriteriaDto,
        new ParameterizedTypeReference<>() {
        }
    );
    rabbitConverterFuture.addCallback(
        new ListenableFutureCallback<>() {
          @Override
          public void onFailure(Throwable ex) {
            throw new RuntimeException(ex);
          }

          @Override
          public void onSuccess(List<IncomingDocumentSearchResultDto> result) {
          }
        });

    List<IncomingDocumentSearchResultDto> result = rabbitConverterFuture.get();
    processingDocumentSearchResultDto.setProcessingDocuments(
        processingDocumentRepository.findProcessingDocumentsByElasticSearchResult(result, offset,
            limit));
    processingDocumentSearchResultDto.setTotalElements(Objects.requireNonNull(result).size());
    if (result.size() % limit == 0) {
      processingDocumentSearchResultDto.setTotalPages(result.size() / limit);
    } else {
      processingDocumentSearchResultDto.setTotalPages(result.size() / limit + 1);
    }
    return processingDocumentSearchResultDto;
  }

}
