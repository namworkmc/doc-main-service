package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.DepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.DepartmentSearchCriteria;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.entity.Department;
import edu.hcmus.doc.mainservice.repository.DepartmentRepository;
import edu.hcmus.doc.mainservice.service.DepartmentService;
import edu.hcmus.doc.mainservice.util.mapper.DepartmentMapper;
import edu.hcmus.doc.mainservice.util.mapper.PaginationMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final DepartmentMapper departmentMapper;
  private final PaginationMapper paginationMapper;

  @Override
  public List<Department> findAll() {
    return departmentRepository.findAll(Sort.by("departmentName").ascending()).stream().toList();
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
  public void deleteDepartments(List<Long> departmentIds) {
    List<Department> departments = departmentRepository.findAllById(departmentIds);
    departments.parallelStream()
        .filter(department -> !department.getDepartmentName().equals("RA"))
        .forEach(department -> department.setDeleted(true));
    departmentRepository.saveAll(departments);
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
