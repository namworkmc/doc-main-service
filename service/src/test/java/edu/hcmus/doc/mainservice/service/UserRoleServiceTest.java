package edu.hcmus.doc.mainservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.hcmus.doc.mainservice.model.entity.UserRole;
import edu.hcmus.doc.mainservice.model.exception.UserRoleNotFoundException;
import edu.hcmus.doc.mainservice.service.impl.UserRoleServiceImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UserRoleServiceTest extends AbstractServiceTest {

  private UserRoleService userRoleService;

  @BeforeEach
  void setUp() {
    userRoleService = new UserRoleServiceImpl(userRoleRepository);
  }

  @Test
  void testGetUserRolesByUserId() {
    // Given
    long userId = anyLong();

    // When
    when(userRoleRepository.getUserRolesByUserId(userId))
        .thenReturn(List.of(
            new UserRole(),
            new UserRole(),
            new UserRole()
        ));

    // Then
    List<UserRole> actual = userRoleService.getUserRolesByUserId(userId);
    ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

    verify(userRoleRepository).getUserRolesByUserId(userIdCaptor.capture());

    assertThat(userIdCaptor.getValue()).isEqualTo(userId);
    assertThat(actual)
        .isNotEmpty()
        .hasSize(3);
  }

  @Test
  void testGetEmptyUserRolesByUserId() {
    // Given
    long userId = anyLong();

    // When
    when(userRoleRepository.getUserRolesByUserId(userId))
        .thenReturn(List.of());

    // Then
    assertThatThrownBy(() -> userRoleService.getUserRolesByUserId(userId))
        .isInstanceOf(UserRoleNotFoundException.class)
        .hasMessage(UserRoleNotFoundException.USER_ROLE_NOT_FOUND);

    ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

    verify(userRoleRepository).getUserRolesByUserId(userIdCaptor.capture());

    assertThat(userIdCaptor.getValue()).isEqualTo(userId);
  }
}
