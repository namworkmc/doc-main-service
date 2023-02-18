package edu.hcmus.doc.mainservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import edu.hcmus.doc.mainservice.model.entity.DocRole;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.entity.UserRole;
import edu.hcmus.doc.mainservice.model.enums.DocRoleEnum;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserRepositoryTest extends AbstractRepositoryTest {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  void testGetUsersWithBlankQuery() {
    // Given
    User user = new User();
    user.setFirstName("test");
    user.setLastName("test");
    user.setUsername("test");
    user.setEmail("test");
    user.setPassword(passwordEncoder.encode("test"));

    DocRole role = new DocRole();
    role.setName(DocRoleEnum.REVIEWER);
    UserRole userRole = new UserRole();
    userRole.setRole(role);
    user.setRoles(Set.of(userRole));

    userRepository.save(user);

    // When
    List<User> users = userRepository.getUsers("", 0, 10);

    // Then
    assertThat(users).isNotEmpty();
  }

  @Test
  void testGetUsersWithQuery() {
    // Given
    String query = "test";

    User user = new User();
    user.setFirstName(query);
    user.setLastName(query);
    user.setUsername(query);
    user.setEmail(query);
    user.setPassword(passwordEncoder.encode(query));

    DocRole role = new DocRole();
    role.setName(DocRoleEnum.REVIEWER);
    UserRole userRole = new UserRole();
    userRole.setRole(role);
    user.setRoles(Set.of(userRole));

    userRepository.save(user);

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
    String username = "username";

    User user = new User();
    user.setFirstName(username);
    user.setLastName(username);
    user.setUsername(username);
    user.setEmail(username);
    user.setPassword(passwordEncoder.encode(username));

    DocRole role = new DocRole();
    role.setName(DocRoleEnum.REVIEWER);
    UserRole userRole = new UserRole();
    userRole.setRole(role);
    user.setRoles(Set.of(userRole));

    userRepository.save(user);

    // When
    User actual = userRepository.findByUsername(username).orElse(null);

    // Then
    assertThat(actual).isNotNull();
  }

  @Test
  void testFindByEmail() {
    // Given
    String email = "email";

    User user = new User();
    user.setFirstName(email);
    user.setLastName(email);
    user.setUsername(email);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(email));

    DocRole role = new DocRole();
    role.setName(DocRoleEnum.REVIEWER);
    UserRole userRole = new UserRole();
    userRole.setRole(role);
    user.setRoles(Set.of(userRole));

    userRepository.save(user);

    // When
    User actual = userRepository.findByEmail(email).orElse(null);

    // Then
    assertThat(actual).isNotNull();
  }
}