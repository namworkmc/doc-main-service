package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.ProcessingMethod;
import java.util.List;

public interface ProcessingMethodService {
  List<ProcessingMethod> findAll();

  ProcessingMethod findByName(String name);

  ProcessingMethod createProcessingMethod(String name);
}
