package edu.hcmus.doc.mainservice.repository.custom.impl;

import edu.hcmus.doc.mainservice.model.entity.PasswordExpiration;
import edu.hcmus.doc.mainservice.model.entity.QPasswordExpiration;
import edu.hcmus.doc.mainservice.repository.custom.CustomPasswordExpirationRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;
import java.util.Optional;

public class CustomPasswordExpirationRepositoryImpl extends DocAbstractCustomRepository<PasswordExpiration>
    implements CustomPasswordExpirationRepository {

  private static final QPasswordExpiration qPasswordExpiration = QPasswordExpiration.passwordExpiration;

  @Override
  public Optional<PasswordExpiration> findLastPasswordExpirationByUserId(Long userId) {
    return Optional.ofNullable(selectFrom(qPasswordExpiration)
        .where(qPasswordExpiration.user.id.eq(userId))
        .orderBy(qPasswordExpiration.creationTime.desc())
        .fetchFirst());
  }

  @Override
  public List<PasswordExpiration> findPasswordExpirationByUserId(Long userId) {
    return selectFrom(qPasswordExpiration)
        .where(qPasswordExpiration.user.id.eq(userId))
        .fetch();
  }
}
