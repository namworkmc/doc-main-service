package edu.hcmus.doc.repository.custom;

import edu.hcmus.doc.model.entity.User;
import java.util.List;
import java.util.Optional;

public interface CustomUserRepository {

  List<User> getUsers(String query, long first, long max);

  Optional<User> getUserById(Long id);

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
