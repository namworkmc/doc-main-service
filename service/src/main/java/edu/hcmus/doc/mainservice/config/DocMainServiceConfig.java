package edu.hcmus.doc.mainservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.hcmus.doc.mainservice.util.KeycloakProperty;
import javax.annotation.PostConstruct;
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

  private final ObjectMapper objectMapper;

  @Bean
  public ResteasyClient resteasyClient() {
    return (ResteasyClient) ClientBuilder.newClient();
  }

  @Bean
  public ResteasyWebTarget resteasyWebTarget() {
    ResteasyClient resteasyClient = resteasyClient();
    return resteasyClient.target(keycloakProperty.getUrl());
  }

  @PostConstruct
  public void setUp() {
    objectMapper.registerModule(new JavaTimeModule());
  }
}
