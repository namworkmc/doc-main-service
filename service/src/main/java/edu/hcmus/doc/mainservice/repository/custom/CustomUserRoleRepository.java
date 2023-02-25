package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.UserRole;
import java.util.List;

public interface CustomUserRoleRepository {

  List<UserRole> getUserRolesByUserId(Long userId);
}
