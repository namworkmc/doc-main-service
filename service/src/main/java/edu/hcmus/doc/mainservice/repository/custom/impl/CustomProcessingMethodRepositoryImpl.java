package edu.hcmus.doc.mainservice.repository.custom.impl;

import edu.hcmus.doc.mainservice.model.entity.ProcessingMethod;
import edu.hcmus.doc.mainservice.model.entity.QProcessingMethod;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingMethodRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.Optional;

public class CustomProcessingMethodRepositoryImpl
    extends DocAbstractCustomRepository<ProcessingMethod>
    implements CustomProcessingMethodRepository {

  @Override
  public Optional<ProcessingMethod> findProcessingMethodByName(String name) {
    return Optional.ofNullable(
        selectFrom(QProcessingMethod.processingMethod)
            .where(QProcessingMethod.processingMethod.name.eq(name))
            .fetchOne());
  }


}
