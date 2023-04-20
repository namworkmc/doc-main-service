package edu.hcmus.doc.mainservice.config;

import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  public @NonNull Optional<String> getCurrentAuditor() {
    return SecurityUtils.getCurrentName().describeConstable();
  }
}
