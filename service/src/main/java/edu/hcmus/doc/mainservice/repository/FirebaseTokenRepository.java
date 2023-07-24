package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.DocFirebaseTokenEntity;
import edu.hcmus.doc.mainservice.model.enums.DocFirebaseTokenType;
import edu.hcmus.doc.mainservice.repository.custom.CustomDocFirebaseTokenEntityRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FirebaseTokenRepository
    extends JpaRepository<DocFirebaseTokenEntity, Long>,
    QuerydslPredicateExecutor<DocFirebaseTokenEntity>,
    CustomDocFirebaseTokenEntityRepository {

  void deleteByTokenAndType(String token, DocFirebaseTokenType type);
}
