package edu.hcmus.doc.mainservice.config;

import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

@Service
public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    return SecurityUtils.getCurrentName().describeConstable();
  }
}
