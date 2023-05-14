package edu.hcmus.doc.mainservice.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.dto.DepartmentSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.Department;
import edu.hcmus.doc.mainservice.model.entity.QDepartment;
import edu.hcmus.doc.mainservice.model.entity.QUser;
import edu.hcmus.doc.mainservice.repository.custom.CustomDepartmentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomDepartmentRepositoryImpl
    extends DocAbstractCustomRepository<Department>
    implements CustomDepartmentRepository {

  @Override
  public long getTotalElements(DepartmentSearchCriteria criteria) {
    return buildSearchQuery(criteria)
        .select(QDepartment.department.id.count())
        .fetchFirst();
  }

  @Override
  public long getTotalPages(DepartmentSearchCriteria criteria, long limit) {
    return 0;
  }

  @Override
  public List<Department> searchByCriteria(DepartmentSearchCriteria criteria, long offset, long limit) {
    QDepartment qDepartment = QDepartment.department;
    return buildSearchQuery(criteria)
        .fetchJoin()
        .orderBy(qDepartment.departmentName.asc())
        .orderBy(qDepartment.id.asc())
        .offset(offset * limit)
        .limit(limit)
        .fetch();
  }

  @Override
  public JPAQuery<Department> buildSearchQuery(DepartmentSearchCriteria criteria) {
    QDepartment qDepartment = QDepartment.department;
    QUser qUser = new QUser(qDepartment.truongPhong.getMetadata().getName());
    return selectFrom(qDepartment)
        .leftJoin(qDepartment.truongPhong, qUser);
  }

  @Override
  public boolean isUserIsTruongPhongOfAnotherDepartment(Long userId, Long departmentId) {
    return selectFrom(QDepartment.department)
        .where(QDepartment.department.truongPhong.id.eq(userId)
            .and(QDepartment.department.id.ne(departmentId)))
        .fetchFirst() != null;
  }

  @Override
  public Department getDepartmentByUserId(Long userId) {
    return selectFrom(QDepartment.department)
        .where(QDepartment.department.truongPhong.id.eq(userId))
        .fetchFirst();
  }
}
