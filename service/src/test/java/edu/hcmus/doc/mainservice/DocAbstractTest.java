package edu.hcmus.doc.mainservice;

import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DocAbstractTest {

  @Autowired
  private UserRepository userRepository;

  protected void setLoginUser(String username, DocSystemRoleEnum role, boolean getUserFromDB) {
    User user;
    if (getUserFromDB) {
      user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    } else {
      user = new User();
      user.setUsername(username);
      user.setRole(role);
    }

    SecurityUtils.setSecurityContextForTesting(user);
  }
}
