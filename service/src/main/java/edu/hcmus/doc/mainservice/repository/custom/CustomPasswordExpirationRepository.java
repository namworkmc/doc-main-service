package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.PasswordExpiration;
import java.util.List;
import java.util.Optional;

public interface CustomPasswordExpirationRepository {

  Optional<PasswordExpiration> findLastPasswordExpirationByUserId(Long userId);
  List<PasswordExpiration> findPasswordExpirationByUserId(Long userId);
}
