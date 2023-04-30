package edu.hcmus.doc.mainservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.entity.UserRole;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "test", password = "test")
class UserRepositoryTest extends DocAbstractRepositoryTest {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  void testGetUsersWithBlankQuery() {
    // Given
    User user = new User();
    user.setUsername("test");
    user.setEmail("test");
    user.setPassword(passwordEncoder.encode("test"));
    user.setRole(DocSystemRoleEnum.GIAM_DOC);
    userRepository.save(user);

    DocSystemRoleEnum docSystemRole = DocSystemRoleEnum.GIAM_DOC;

    UserRole userRole = new UserRole();
    userRole.setUser(user);
    userRole.setRole(docSystemRole);
    userRoleRepository.save(userRole);

    // When
    List<User> users = userRepository.getUsers("", 0, 10);

    // Then
    assertThat(users).isNotEmpty();
  }

  @Test
  void testGetUsersWithQuery() {
    // Given
    String query = "test";

    // Given
    User user = new User();
    user.setUsername("test");
    user.setEmail("test");
    user.setPassword(passwordEncoder.encode("test"));
    user.setRole(DocSystemRoleEnum.GIAM_DOC);
    userRepository.save(user);

    DocSystemRoleEnum docSystemRole = DocSystemRoleEnum.GIAM_DOC;

    UserRole userRole = new UserRole();
    userRole.setUser(user);
    userRole.setRole(docSystemRole);
    userRoleRepository.save(userRole);

    // When
    List<User> users = userRepository.getUsers(query, 0, 10);

    // Then
    assertThat(users).isNotEmpty();
  }

  @Test
  void testGetUserById() {
    // Given
    Long id = 1L;

    // When
    User user = userRepository.getUserById(id).orElse(null);

    // Then
    assertThat(user).isNotNull();
  }

  @Test
  void testFindByUsername() {
    // Given
    String username = "user1";

    // When
    User actual = userRepository.findByUsername(username).orElse(null);

    // Then
    assertThat(actual).isNotNull();
  }

  @Test
  void testFindByEmail() {
    // Given
    String email = "email";

    // Given
    User user = new User();
    user.setUsername(email);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(email));
    user.setRole(DocSystemRoleEnum.GIAM_DOC);
    userRepository.save(user);

    DocSystemRoleEnum docSystemRole = DocSystemRoleEnum.GIAM_DOC;

    UserRole userRole = new UserRole();
    userRole.setUser(user);
    userRole.setRole(docSystemRole);
    userRoleRepository.save(userRole);

    // When
    User actual = userRepository.findByEmail(email).orElse(null);

    // Then
    assertThat(actual).isNotNull();
  }
}
