package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DocStatisticsSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.DocStatisticsWrapperDto;
import edu.hcmus.doc.mainservice.model.dto.TransferHistory.TransferHistoryDto;
import edu.hcmus.doc.mainservice.model.dto.TransferHistory.TransferHistorySearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.UserDepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.UserDto;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.UserService;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/users")
public class UserController extends DocAbstractController {

  private final UserService userService;

  @GetMapping("/current")
  public UserDto getCurrentUser() {
    return userDecoratorMapper.toDto(userService.getCurrentUserFromDB());
  }

  @GetMapping("/role/{role}")
  public List<UserDto> getUsersByRole(@PathVariable DocSystemRoleEnum role) {
    return userMapper.toDto(userService.getUsersByRole(role));
  }

  @GetMapping("/role/department/{role}")
  public List<UserDepartmentDto> getUsersByRoleWithDepartment(
      @PathVariable DocSystemRoleEnum role) {
    return userService.getUsersByRoleWithDepartment(role);
  }

  @GetMapping("/current-principal")
  public UserDto getCurrentName() {
    return userMapper.toDto(SecurityUtils.getCurrentUser());
  }

  @PutMapping("/current/password")
  public Long updateCurrentUserPassword(@RequestParam String oldPassword,
      @RequestParam String newPassword) {
    return userService.updateCurrentUserPassword(oldPassword, newPassword);
  }

  @PutMapping("/current")
  public Long updateCurrentUser(@RequestBody UserDto userDto) {
    User user = userDecoratorMapper.partialUpdate(userDto, userService.getCurrentUserFromDB());
    return userService.updateUser(user);
  }

  @PostMapping("/get-transfer-history")
  public List<TransferHistoryDto> getTransferDocumentHistory(
      @RequestBody(required = false) TransferHistorySearchCriteriaDto searchCriteria,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "3") int pageSize) {
    return userService.getTransferHistoryByUser(
        searchCriteria, page, pageSize);
  }

  @GetMapping("/all")
  public List<UserDto> getUsers() {
    return userService
        .getAllUsers()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @PostMapping("/get-statistics")
  public DocStatisticsWrapperDto getStatistics(
      @RequestBody(required = false) DocStatisticsSearchCriteriaDto docStatisticsSearchCriteriaDto) {
    DocStatisticsWrapperDto docStatisticsWrapperDto = new DocStatisticsWrapperDto();
    docStatisticsWrapperDto.setDocStatisticsDtos(
        userService.getStatistics(docStatisticsSearchCriteriaDto));
    docStatisticsWrapperDto.setFromDate(
        Objects.isNull(docStatisticsSearchCriteriaDto.getFromDate()) ? ""
            : docStatisticsSearchCriteriaDto.getFromDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    docStatisticsWrapperDto.setToDate(
        Objects.isNull(docStatisticsSearchCriteriaDto.getToDate()) ? ""
            : docStatisticsSearchCriteriaDto.getToDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    return docStatisticsWrapperDto;
  }
}
