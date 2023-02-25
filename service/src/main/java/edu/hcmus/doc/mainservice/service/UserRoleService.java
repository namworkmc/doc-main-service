package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.UserRole;
import java.util.List;

public interface UserRoleService {

  List<UserRole> getUserRolesByUserId(Long userId);
}
