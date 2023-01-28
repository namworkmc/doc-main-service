package edu.hcmus.doc.service.impl;

import edu.hcmus.doc.model.entity.User;
import edu.hcmus.doc.repository.UserRepository;
import edu.hcmus.doc.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public List<User> getUsers() {
    return userRepository.findAll();
  }
}
