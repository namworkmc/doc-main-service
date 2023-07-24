package edu.hcmus.doc.mainservice.repository.custom.impl;

import edu.hcmus.doc.mainservice.model.entity.DocFirebaseTokenEntity;
import edu.hcmus.doc.mainservice.model.entity.QDocFirebaseTokenEntity;
import edu.hcmus.doc.mainservice.repository.custom.CustomDocFirebaseTokenEntityRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomDocFirebaseTokenEntityRepositoryImpl
    extends DocAbstractCustomRepository<DocFirebaseTokenEntity>
    implements CustomDocFirebaseTokenEntityRepository {

  private static final QDocFirebaseTokenEntity qDocFirebaseTokenEntity = QDocFirebaseTokenEntity.docFirebaseTokenEntity;

  @Override
  public List<DocFirebaseTokenEntity> getTokensByUserId(Long userId) {
    return selectFrom(qDocFirebaseTokenEntity)
        .where(qDocFirebaseTokenEntity.user.id.eq(userId))
        .fetch();
  }
}
