package edu.hcmus.doc.mainservice.repository.custom.impl;

import edu.hcmus.doc.mainservice.model.entity.QDocSystemRole;
import edu.hcmus.doc.mainservice.model.entity.QUser;
import edu.hcmus.doc.mainservice.model.entity.QUserRole;
import edu.hcmus.doc.mainservice.model.entity.UserRole;
import edu.hcmus.doc.mainservice.repository.custom.CustomUserRoleRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomUserRoleRepositoryImpl
    extends DocAbstractCustomRepository<UserRole>
    implements CustomUserRoleRepository {

  @Override
  public List<UserRole> getUserRolesByUserId(Long userId) {
    return selectFrom(QUserRole.userRole)
        .leftJoin(QUserRole.userRole.user, QUser.user)
        .fetchJoin()
        .leftJoin(QUserRole.userRole.role, QDocSystemRole.docSystemRole)
        .fetchJoin()
        .where(QUserRole.userRole.user.id.eq(userId))
        .fetch();
  }
}
