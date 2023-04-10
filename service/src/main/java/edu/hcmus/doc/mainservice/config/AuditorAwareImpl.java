package edu.hcmus.doc.mainservice.config;

import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

@Profile("dev-security")
@Service
public class AuditorAwareImpl implements AuditorAware<User> {

  @Autowired
  private UserRepository userRepository;

  @Override
  public Optional<User> getCurrentAuditor() {
    return userRepository.getUserById(SecurityUtils.getCurrentUser().getId());
  }
}
