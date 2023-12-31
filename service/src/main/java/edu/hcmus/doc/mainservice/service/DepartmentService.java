package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.DepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.DepartmentSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.Department;
import java.util.List;
import java.util.Set;

public interface DepartmentService extends SearchService<DepartmentDto, DepartmentSearchCriteria> {

  List<Department> getAllDepartments();

  Department getDepartmentById(Long id);

  Long saveDepartment(Department department, Long newTruongPhongId);

  void deleteDepartments(Set<Long> departmentIds);

  boolean isUserTruongPhongOfAnotherDepartment(Long userId, Long departmentId);
}
