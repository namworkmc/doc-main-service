package edu.hcmus.doc.mainservice.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.entity.QDocRole;
import edu.hcmus.doc.mainservice.model.entity.QUser;
import edu.hcmus.doc.mainservice.model.entity.QUserRole;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.repository.custom.CustomUserRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class CustomUserRepositoryImpl extends DocAbstractCustomRepository<User> implements
    CustomUserRepository {

  @Override
  public List<User> getUsers(String query, long first, long max) {
    JPAQuery<User> userJPAQuery = baseQuery();
    if (StringUtils.isNotBlank(query)) {
      userJPAQuery.where(QUser.user.username.startsWithIgnoreCase(query)
          .or(QUser.user.email.startsWithIgnoreCase(query)));
    }

    return userJPAQuery
        .offset(first)
        .limit(max)
        .distinct()
        .fetch();
  }

  @Override
  public Optional<User> getUserById(Long id) {
    return Optional.ofNullable(
        baseQuery()
            .where(QUser.user.id.eq(id))
            .fetchOne()
    );
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return Optional.ofNullable(
        baseQuery()
            .where(QUser.user.username.eq(username))
            .fetchOne()
    );
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(
        baseQuery()
            .where(QUser.user.email.eq(email))
            .fetchOne()
    );
  }

  private JPAQuery<User> baseQuery() {
    return selectFrom(QUser.user)
        .leftJoin(QUser.user.roles, QUserRole.userRole)
        .fetchJoin()
        .leftJoin(QUserRole.userRole.role, QDocRole.docRole)
        .fetchJoin();
  }
}
