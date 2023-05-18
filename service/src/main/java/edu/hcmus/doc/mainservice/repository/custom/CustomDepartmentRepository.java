package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.dto.DepartmentSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.Department;
import edu.hcmus.doc.mainservice.repository.DocAbstractSearchRepository;
import java.util.List;

public interface CustomDepartmentRepository
    extends DocAbstractSearchRepository<Department, DepartmentSearchCriteria> {

  boolean isUserIsTruongPhongOfAnotherDepartment(Long userId, Long departmentId);

  Department getDepartmentByUserId(Long userId);

  List<Department> getAllDepartments();
}
