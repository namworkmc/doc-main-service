package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.Folder;
import edu.hcmus.doc.mainservice.repository.custom.CustomFolderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends
    JpaRepository<Folder, Long>,
    QuerydslPredicateExecutor<Folder>,
    CustomFolderRepository {
}
