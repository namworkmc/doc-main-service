package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.ProcessingMethod;
import java.util.Optional;

public interface CustomProcessingMethodRepository {
  Optional<ProcessingMethod> findProcessingMethodByName(String name);

}
