package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.entity.ProcessingMethod;
import edu.hcmus.doc.mainservice.repository.ProcessingMethodRepository;
import edu.hcmus.doc.mainservice.service.ProcessingMethodService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class ProcessingMethodServiceImpl implements ProcessingMethodService {

  private final ProcessingMethodRepository processingMethodRepository;

  @Override
  public List<ProcessingMethod> findAll() {
    return processingMethodRepository.findAll();
  }

  @Override
  public ProcessingMethod findByName(String name){
    if(StringUtils.isNotBlank(name)) {
      Optional<ProcessingMethod> processingMethod = processingMethodRepository.findProcessingMethodByName(name);
      return processingMethod.orElseGet(() -> createProcessingMethod(name));
    } else return null;
  }

  @Override
  public ProcessingMethod createProcessingMethod(String name) {
    ProcessingMethod processingMethod = new ProcessingMethod();
    processingMethod.setName(name);
    return processingMethodRepository.saveAndFlush(processingMethod);
  }


}
