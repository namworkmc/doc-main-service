package edu.hcmus.doc.mainservice.config;

import edu.hcmus.doc.mainservice.util.keycloak.KeycloakProperty;
import javax.ws.rs.client.ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing
@EnableConfigurationProperties(KeycloakProperty.class)
public class DocMainServiceConfig {

  private final KeycloakProperty keycloakProperty;

  @Bean
  public ResteasyClient resteasyClient() {
    return (ResteasyClient) ClientBuilder.newClient();
  }

  @Bean
  public ResteasyWebTarget resteasyWebTarget() {
    ResteasyClient resteasyClient = resteasyClient();
    return resteasyClient.target(keycloakProperty.getUrl());
  }
}
