package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ProcessingFlow;
import edu.hcmus.doc.mainservice.model.entity.pk.ProcessingFlowPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessingFlowRepository extends JpaRepository<ProcessingFlow, ProcessingFlowPK> {

}