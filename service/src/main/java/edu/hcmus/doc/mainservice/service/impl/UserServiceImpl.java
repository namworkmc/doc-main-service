package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.service.UserService;
import edu.hcmus.doc.mainservice.model.entity.User;
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
}
