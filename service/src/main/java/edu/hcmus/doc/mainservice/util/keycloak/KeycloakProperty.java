package edu.hcmus.doc.mainservice.util.keycloak;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperty {

  private String url;
  private String scope;
  private String clientId;
  private String clientSecret;
}
