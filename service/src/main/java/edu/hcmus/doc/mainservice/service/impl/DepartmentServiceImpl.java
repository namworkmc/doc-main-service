package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.exception.DepartmentEditException.USERS_ARE_AVAILABLE_IN_DEPARTMENT;

import edu.hcmus.doc.mainservice.model.dto.DepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.DepartmentSearchCriteria;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.entity.Department;
import edu.hcmus.doc.mainservice.model.exception.DepartmentEditException;
import edu.hcmus.doc.mainservice.repository.DepartmentRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.service.DepartmentService;
import edu.hcmus.doc.mainservice.util.mapper.DepartmentMapper;
import edu.hcmus.doc.mainservice.util.mapper.PaginationMapper;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final DepartmentMapper departmentMapper;
  private final PaginationMapper paginationMapper;
  private final UserRepository userRepository;

  @Override
  public List<Department> getAllDepartments() {
    return departmentRepository.getAllDepartments();
  }

  @Override
  public Department getDepartmentById(Long id) {
    return departmentRepository.findById(id).orElseThrow();
  }

  @Override
  public Long saveDepartment(Department department, Long newTruongPhongId) {
    if (isUserTruongPhongOfAnotherDepartment(department.getTruongPhong().getId(), department.getId())) {
      Department userPrevDepartment = departmentRepository.getDepartmentByUserId(department.getTruongPhong().getId());
      userPrevDepartment.setTruongPhong(null);
      departmentRepository.save(userPrevDepartment);
    }

    return departmentRepository.save(department).getId();
  }

  @Override
  public void deleteDepartments(Set<Long> departmentIds) {
    if (userRepository.existsByDepartmentIdIn(departmentIds)) {
      throw new DepartmentEditException(USERS_ARE_AVAILABLE_IN_DEPARTMENT);
    }

    departmentRepository.deleteAllById(departmentIds);
  }

  @Override
  public boolean isUserTruongPhongOfAnotherDepartment(Long userId, Long departmentId) {
    if (departmentId == null) {
      return false;
    }

    return departmentRepository.isUserIsTruongPhongOfAnotherDepartment(userId, departmentId);
  }

  @Override
  public DocPaginationDto<DepartmentDto> search(DepartmentSearchCriteria criteria, int page, int pageSize) {
    long totalElements = departmentRepository.getTotalElements(criteria);
    long totalPages = (totalElements / pageSize) + (totalElements % pageSize == 0 ? 0 : 1);
    List<DepartmentDto> departmentDtos = departmentRepository
        .searchByCriteria(criteria, page, pageSize)
        .stream()
        .map(departmentMapper::toDto)
        .toList();

    return paginationMapper.toDto(departmentDtos, totalElements, totalPages);
  }
}
