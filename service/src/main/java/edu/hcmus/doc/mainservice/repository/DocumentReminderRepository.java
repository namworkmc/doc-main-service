package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.DocumentReminder;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomDocumentReminderRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentReminderRepository
    extends JpaRepository<DocumentReminder, Long>,
    QuerydslPredicateExecutor<DocumentReminder>,
    CustomDocumentReminderRepository {

  List<DocumentReminder> getDocumentRemindersByStatusIn(List<DocumentReminderStatusEnum> statuses);
}
