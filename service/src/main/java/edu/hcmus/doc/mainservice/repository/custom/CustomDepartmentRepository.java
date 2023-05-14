package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.dto.DepartmentSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.Department;
import edu.hcmus.doc.mainservice.repository.DocAbstractSearchRepository;

public interface CustomDepartmentRepository
    extends DocAbstractSearchRepository<Department, DepartmentSearchCriteria> {

  boolean isUserIsTruongPhongOfAnotherDepartment(Long userId, Long departmentId);

  Department getDepartmentByUserId(Long userId);
}
