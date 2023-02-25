package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.entity.UserRole;
import edu.hcmus.doc.mainservice.model.exception.UserRoleNotFoundException;
import edu.hcmus.doc.mainservice.repository.UserRoleRepository;
import edu.hcmus.doc.mainservice.service.UserRoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class UserRoleServiceImpl implements UserRoleService {

  private final UserRoleRepository userRoleRepository;

  @Override
  public List<UserRole> getUserRolesByUserId(Long userId) {
    List<UserRole> userRoles = userRoleRepository.getUserRolesByUserId(userId);

    if (CollectionUtils.isEmpty(userRoles)) {
      throw new UserRoleNotFoundException(UserRoleNotFoundException.USER_ROLE_NOT_FOUND);
    }

    return userRoles;
  }
}
