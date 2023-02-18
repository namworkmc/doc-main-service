package edu.hcmus.doc.mainservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.service.impl.UserServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest extends AbstractServiceTest {

  @Mock
  private UserRepository userRepository;

  @Spy
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserServiceImpl(userRepository, passwordEncoder);
  }

  @Test
  void testGetUsers() {
    // Given
    String query = anyString();
    long first = anyLong();
    long max = anyLong();

    // When
    List<User> users = userService.getUsers(query, first, max);

    // Then
    ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Long> firstCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Long> maxCaptor = ArgumentCaptor.forClass(Long.class);

    verify(userRepository).getUsers(queryCaptor.capture(), firstCaptor.capture(), maxCaptor.capture());

    assertThat(queryCaptor.getValue()).isEqualTo(query);
    assertThat(firstCaptor.getValue()).isEqualTo(first);
    assertThat(maxCaptor.getValue()).isEqualTo(max);

    assertThat(users).isNotNull();
  }

  @Test
  void testGetUserById() {
    // Given
    Long id = anyLong();
    User user = new User();
    user.setId(id);

    // When
    when(userRepository.getUserById(id)).thenReturn(Optional.of(user));

    // Then
    User result = userService.getUserById(id);
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(userRepository).getUserById(idCaptor.capture());

    assertThat(idCaptor.getValue()).isEqualTo(id);
    assertThat(result)
        .isNotNull()
        .is(new Condition<>(u -> id.equals(u.getId()), "User id is 1"));
  }

  @Test
  void testGetUserByIdWhenUserNotFound() {
    // Given
    Long id = anyLong();

    // When
    when(userRepository.getUserById(id)).thenReturn(Optional.empty());

    // Then
    assertThatThrownBy(() -> userService.getUserById(id))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessage(UserNotFoundException.USER_NOT_FOUND);

    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(userRepository).getUserById(idCaptor.capture());

    assertThat(idCaptor.getValue()).isEqualTo(id);
  }

  @Test
  void testGetUserByUsername() {
    // Given
    String username = anyString();
    User user = new User();
    user.setUsername(username);

    // When
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    // Then
    User result = userService.getUserByUsername(username);
    ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
    verify(userRepository).findByUsername(usernameCaptor.capture());

    assertThat(usernameCaptor.getValue()).isEqualTo(username);
    assertThat(result)
        .isNotNull()
        .is(new Condition<>(u -> username.equals(u.getUsername()), "User username is \"username\""));
  }

  @Test
  void testGetUserByUsernameWhenUserNotFound() {
    // Given
    String username = anyString();

    // When
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Then
    assertThatThrownBy(() -> userService.getUserByUsername(username))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessage(UserNotFoundException.USER_NOT_FOUND);

    ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
    verify(userRepository).findByUsername(usernameCaptor.capture());

    assertThat(usernameCaptor.getValue()).isEqualTo(username);
  }

  @Test
  void testGetUserByEmail() {
    // Given
    String email = anyString();
    User user = new User();
    user.setEmail(email);

    // When
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    // Then
    User result = userService.getUserByEmail(email);
    ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
    verify(userRepository).findByEmail(emailCaptor.capture());

    assertThat(emailCaptor.getValue()).isEqualTo(email);
    assertThat(result)
        .isNotNull()
        .is(new Condition<>(u -> email.equals(u.getEmail()), "User email is \"email\""));
  }

  @Test
  void testGetUserByEmailWhenUserNotFound() {
    // Given
    String email = anyString();

    // When
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    // Then
    assertThatThrownBy(() -> userService.getUserByEmail(email))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessage(UserNotFoundException.USER_NOT_FOUND);

    ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
    verify(userRepository).findByEmail(emailCaptor.capture());

    assertThat(emailCaptor.getValue()).isEqualTo(email);
  }

  @Test
  void testGetTotalUsers() {
    // Given
    // When
    when(userRepository.count()).thenReturn(10L);

    // Then
    userService.getTotalUsers();
    verify(userRepository).count();

    assertThat(userService.getTotalUsers()).isEqualTo(10L);
  }

  @Test
  void testValidUserCredentials() {
    // Given
    Long id = anyLong();
    String password = "password";
    String encodedPassword = passwordEncoder.encode(password);

    User user = new User();
    user.setId(id);
    user.setPassword(encodedPassword);

    // When
    when(userRepository.getUserById(id)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

    // Then
    userService.validateUserCredentialsByUserId(id, password);

    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
    verify(userRepository).getUserById(idCaptor.capture());
    verify(passwordEncoder).matches(passwordCaptor.capture(), passwordCaptor.capture());

    assertThat(idCaptor.getValue()).isEqualTo(id);
    assertThat(passwordCaptor.getAllValues()).containsExactly(password, encodedPassword);

    assertThat(userService.validateUserCredentialsByUserId(id, password)).isTrue();
  }

  @Test
  void testInvalidUserCredentials() {
    // Given
    Long id = anyLong();
    String password = "password";
    String encodedPassword = passwordEncoder.encode(password);

    User user = new User();
    user.setId(id);
    user.setPassword(encodedPassword);

    // When
    when(userRepository.getUserById(id)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

    // Then
    userService.validateUserCredentialsByUserId(id, password);

    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
    verify(userRepository).getUserById(idCaptor.capture());
    verify(passwordEncoder).matches(passwordCaptor.capture(), passwordCaptor.capture());

    assertThat(idCaptor.getValue()).isEqualTo(id);
    assertThat(passwordCaptor.getAllValues()).containsExactly(password, encodedPassword);

    assertThat(userService.validateUserCredentialsByUserId(id, password)).isFalse();
  }
}