package edu.hcmus.doc.mainservice.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.entity.QUser;
import edu.hcmus.doc.mainservice.model.entity.QUserRole;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomUserRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class CustomUserRepositoryImpl
    extends DocAbstractCustomRepository<User>
    implements CustomUserRepository {

  @Override
  public List<User> getUsers(String query, long first, long max) {
    JPAQuery<User> userJPAQuery = selectFrom(QUser.user);
    if (StringUtils.isNotBlank(query)) {
      userJPAQuery.where(QUser.user.username.startsWithIgnoreCase(query)
          .or(QUser.user.email.startsWithIgnoreCase(query)));
    }

    return userJPAQuery
        .offset(first)
        .limit(max)
        .fetch();
  }

  @Override
  public Optional<User> getUserById(Long id) {
    return Optional.ofNullable(
        selectFrom(QUser.user)
            .where(QUser.user.id.eq(id))
            .fetchOne()
    );
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return Optional.ofNullable(
        selectFrom(QUser.user)
            .where(QUser.user.username.eq(username))
            .fetchOne()
    );
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(
        selectFrom(QUser.user)
            .where(QUser.user.email.eq(email))
            .fetchOne()
    );
  }

  @Override
  public List<User> getDirectors() {
    return selectFrom(QUserRole.userRole)
        .select(QUser.user)
        .innerJoin(QUserRole.userRole.user, QUser.user)
        .where(QUserRole.userRole.role.eq(DocSystemRoleEnum.DIRECTOR))
        .fetch()
        .stream()
        .toList();
  }
}
