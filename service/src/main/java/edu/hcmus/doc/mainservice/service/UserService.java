package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.User;
import java.util.List;

public interface UserService {

  List<User> getUsers(String query, long first, long max);

  User getUserById(Long id);

  User getUserByUsername(String username);

  User getUserByEmail(String email);

  long getTotalUsers();

  boolean validateUserCredentialsByUserId(Long id, String password);

  User getCurrentUserFromDB();

  List<User> getDirectors();

  List<User> getSecretaries();
}
