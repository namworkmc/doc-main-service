package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.TransferHistory.TransferHistoryDto;
import edu.hcmus.doc.mainservice.model.dto.TransferHistory.TransferHistorySearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.UserDepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.UserDto;
import edu.hcmus.doc.mainservice.model.dto.UserSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.model.exception.EmailExistedException;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserPasswordIncorrectException;
import edu.hcmus.doc.mainservice.model.exception.UsernameExistedException;
import edu.hcmus.doc.mainservice.repository.TransferHistoryRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.UserService;
import edu.hcmus.doc.mainservice.util.mapper.PaginationMapper;
import edu.hcmus.doc.mainservice.util.mapper.TransferHistoryMapper;
import edu.hcmus.doc.mainservice.util.mapper.UserMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final PaginationMapper paginationMapper;
  private final TransferHistoryRepository transferHistoryRepository;
  private final TransferHistoryMapper transferHistoryMapper;

  @Override
  public List<User> getUsers(String query, long first, long max) {
    return userRepository.getUsers(query, first, max);
  }

  @Override
  public User getUserById(Long id) {
    return userRepository
        .getUserById(id)
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));
  }

  @Override
  public User getUserByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));
  }

  @Override
  public User getUserByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));
  }

  @Override
  public long getTotalUsers() {
    return userRepository.count();
  }

  @Override
  public boolean validateUserCredentialsByUserId(Long id, String password) {
    return passwordEncoder.matches(password, getUserById(id).getPassword());
  }

  @Override
  public User getCurrentUserFromDB() {
    return userRepository
        .findByUsername(SecurityUtils.getCurrentName())
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));
  }

  @Override
  public List<User> getUsersByRole(DocSystemRoleEnum role) {
    return userRepository.getUsersByRole(role);
  }

  @Override
  public List<UserDepartmentDto> getUsersByRoleWithDepartment(DocSystemRoleEnum role) {
    return userRepository.getUsersByRoleWithDepartment(role);
  }

  @Override
  public Long createUser(User user) {
    userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
      throw new UsernameExistedException();
    });
    userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
      throw new EmailExistedException();
    });

    return userRepository.save(user).getId();
  }

  @Override
  public Long updateUser(User user) {
    User persistedUser = getUserById(user.getId());
    if (!persistedUser.getUsername().equals(user.getUsername())) {
      userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
        throw new UsernameExistedException();
      });
    }
    if (!persistedUser.getEmail().equals(user.getEmail())) {
      userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
        throw new EmailExistedException();
      });
    }

    return userRepository.save(user).getId();
  }

  @Override
  public Long updateCurrentUserPassword(String oldPassword, String newPassword) {
    User user = getCurrentUserFromDB();
    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new UserPasswordIncorrectException();
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    return userRepository.save(user).getId();
  }

  @Override
  public DocPaginationDto<UserDto> search(UserSearchCriteria userSearchCriteria, int page,
      int pageSize) {
    long totalElements = userRepository.getTotalElements(userSearchCriteria);
    long totalPages = (totalElements / pageSize) + (totalElements % pageSize == 0 ? 0 : 1);
    List<UserDto> users = userRepository
        .searchByCriteria(userSearchCriteria, page, pageSize)
        .stream()
        .map(userMapper::toDto)
        .toList();

    return paginationMapper.toDto(users, totalElements, totalPages);
  }

  @Override
  public void deleteUsers(List<Long> userIds) {
    List<User> users = userRepository.getUsersIn(userIds);
    users.parallelStream()
        .filter(user -> user.getRole() != DocSystemRoleEnum.DOC_ADMIN)
        .forEach(user -> user.setDeleted(true));
    userRepository.saveAll(users);
  }

  @Override
  @Transactional
  public List<TransferHistoryDto> getTransferHistoryByUser(
      TransferHistorySearchCriteriaDto criteriaDto, int offset, int limit) {
    Pageable pageable = PageRequest.of(offset, limit);
    return transferHistoryRepository.findAllByUserId(criteriaDto.getUserId(), pageable)
        .map(transferHistoryMapper::toDto).toList();
  }
}
