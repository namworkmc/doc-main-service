package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.SendingLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendingLevelRepository extends JpaRepository<SendingLevel, Long> {

}