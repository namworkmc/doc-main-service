package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.UserDepartmentDto;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

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
}
