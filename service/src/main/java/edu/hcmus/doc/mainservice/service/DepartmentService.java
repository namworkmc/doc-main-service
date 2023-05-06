package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.Department;
import java.util.List;

public interface DepartmentService {

  List<Department> findAll();

  Department getDepartmentById(Long id);
}
