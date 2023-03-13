package edu.hcmus.doc.mainservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.entity.UserRole;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserRoleRepositoryTest extends DocAbstractRepositoryTest {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  void testGetUserRolesByUserId() {
    // Given
    User user = new User();
    user.setFirstName("test");
    user.setLastName("test");
    user.setUsername("test");
    user.setEmail("test");
    user.setPassword(passwordEncoder.encode("test"));
    userRepository.save(user);

    DocSystemRoleEnum docSystemRole = DocSystemRoleEnum.DIRECTOR;

    UserRole userRole = new UserRole();
    userRole.setUser(user);
    userRole.setRole(docSystemRole);
    userRoleRepository.save(userRole);

    // When
    List<UserRole> actual = userRoleRepository.getUserRolesByUserId(user.getId());

    // Then
    assertThat(actual).isNotEmpty();
    assertThat(actual.get(0).getUser()).isEqualTo(user);
    assertThat(actual.get(0).getRole()).isEqualTo(docSystemRole);
  }
}