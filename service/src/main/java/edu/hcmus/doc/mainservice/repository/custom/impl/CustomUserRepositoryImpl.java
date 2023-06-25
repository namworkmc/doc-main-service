package edu.hcmus.doc.mainservice.repository.custom.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.dto.UserDepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.UserSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.QDepartment;
import edu.hcmus.doc.mainservice.model.entity.QUser;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomUserRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class CustomUserRepositoryImpl
    extends DocAbstractCustomRepository<User>
    implements CustomUserRepository {

  @Override
  public List<User> getUsers(String query, long first, long max) {
    JPAQuery<User> userJPAQuery = selectFrom(QUser.user)
        .innerJoin(QUser.user.department, QDepartment.department)
        .fetchJoin();
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
  public List<User> getAllUsers() {
    return selectFrom(QUser.user)
        .innerJoin(QUser.user.department, QDepartment.department)
        .where(QUser.user.role.ne(DocSystemRoleEnum.DOC_ADMIN))
        .fetchJoin().fetch();
  }

  @Override
  public Optional<User> getUserById(Long id) {
    return Optional.ofNullable(
        selectFrom(QUser.user)
            .innerJoin(QUser.user.department, QDepartment.department)
            .fetchJoin()
            .where(QUser.user.id.eq(id))
            .fetchOne()
    );
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return Optional.ofNullable(
        selectFrom(QUser.user)
            .innerJoin(QUser.user.department, QDepartment.department)
            .fetchJoin()
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
  public List<User> getUsersByRole(DocSystemRoleEnum role) {
    return selectFrom(QUser.user)
        .innerJoin(QUser.user.department, QDepartment.department)
        .fetchJoin()
        .where(QUser.user.role.eq(role))
        .fetch();
  }
  @Override
  public List<UserDepartmentDto> getUsersByRoleWithDepartment(DocSystemRoleEnum role) {
    User currentUser = SecurityUtils.getCurrentUser();

    return selectFrom(QUser.user)
        .leftJoin(QUser.user.department, QDepartment.department)
        .where(QUser.user.role.eq(role))
//            .and(QUser.user.id.ne(currentUser.getId()))) // exclude current user
        .select(QUser.user.id, QUser.user.version, QUser.user.username, QUser.user.email, QUser.user.fullName, QUser.user.role, QDepartment.department.departmentName)
        .fetch()
        .stream()
        .map(tuple -> {
          UserDepartmentDto userDepartmentDto = new UserDepartmentDto();
          userDepartmentDto.setId(tuple.get(QUser.user.id));
          userDepartmentDto.setVersion(tuple.get(QUser.user.version));
          userDepartmentDto.setUsername(tuple.get(QUser.user.username));
          userDepartmentDto.setEmail(tuple.get(QUser.user.email));
          userDepartmentDto.setFullName(tuple.get(QUser.user.fullName));
          userDepartmentDto.setRole(tuple.get(QUser.user.role));
          userDepartmentDto.setDepartmentName(tuple.get(QDepartment.department.departmentName));
          return userDepartmentDto;
        })
        .toList();
  }

  @Override
  public List<User> getUsersIn(List<Long> userIds) {
    return selectFrom(QUser.user)
        .where(QUser.user.id.in(userIds))
        .fetch();
  }

  @Override
  public long getTotalElements(UserSearchCriteria criteria) {
    return buildSearchQuery(criteria)
        .fetch()
        .size();
  }

  @Override
  public long getTotalPages(UserSearchCriteria criteria, long limit) {
    return 0;
  }

  @Override
  public List<User> searchByCriteria(UserSearchCriteria criteria, long offset, long limit) {
    return buildSearchQuery(criteria)
        .orderBy(QUser.user.id.asc())
        .offset(offset * limit)
        .limit(limit)
        .fetch();
  }

  @Override
  public JPAQuery<User> buildSearchQuery(UserSearchCriteria criteria) {
    BooleanBuilder whereBuilder = new BooleanBuilder();
    if (StringUtils.isNotBlank(criteria.getUsername())) {
      whereBuilder.and(QUser.user.username.startsWithIgnoreCase(criteria.getUsername()));
    }
    if (StringUtils.isNotBlank(criteria.getEmail())) {
      whereBuilder.and(QUser.user.email.startsWithIgnoreCase(criteria.getEmail()));
    }
    if (StringUtils.isNotBlank(criteria.getFullName())) {
      whereBuilder.and(QUser.user.fullName.startsWithIgnoreCase(criteria.getFullName()));
    }
    if (criteria.getRole() != null) {
      whereBuilder.and(QUser.user.role.eq(criteria.getRole()));
    }
    if (criteria.getDepartmentId() != null) {
      whereBuilder.and(QUser.user.department.id.eq(criteria.getDepartmentId()));
    }

    return selectFrom(QUser.user)
        .innerJoin(QUser.user.department, QDepartment.department)
        .where(whereBuilder)
        .fetchJoin();
  }
}
