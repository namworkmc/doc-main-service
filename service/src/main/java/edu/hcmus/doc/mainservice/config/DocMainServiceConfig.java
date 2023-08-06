package edu.hcmus.doc.mainservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import edu.hcmus.doc.mainservice.util.FirebaseProperties;
import edu.hcmus.doc.mainservice.util.keycloak.KeycloakProperties;
import javax.annotation.PostConstruct;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.client.ClientBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableConfigurationProperties({KeycloakProperties.class, FirebaseProperties.class})
@EnableScheduling
public class DocMainServiceConfig {

  private final KeycloakProperties keycloakProperties;

  private final FirebaseProperties firebaseProperties;

  private final ObjectMapper objectMapper;

  @Bean
  public ResteasyClient resteasyClient() {
    return (ResteasyClient) ClientBuilder.newClient();
  }

  @Bean
  public ResteasyWebTarget resteasyTokenTarget() {
    ResteasyClient resteasyClient = resteasyClient();
    return resteasyClient.target(keycloakProperties.getUrl() + "/token");
  }

  @Bean
  public ResteasyWebTarget resteasyRevokeTarget() {
    ResteasyClient resteasyClient = resteasyClient();
    return resteasyClient.target(keycloakProperties.getUrl() + "/revoke");
  }

  @PostConstruct
  public void setUp() {
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Bean
  public AuditorAware<String> auditorProvider() {
    return new AuditorAwareImpl();
  }

  @Bean
  public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
    return FirebaseMessaging.getInstance(firebaseApp);
  }

  @Bean
  public FirebaseApp firebaseApp(GoogleCredentials googleCredentials) {
    if (CollectionUtils.isNotEmpty(FirebaseApp.getApps())) {
      return FirebaseApp.getInstance();
    }

    FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(googleCredentials)
        .build();

    return FirebaseApp.initializeApp(options);
  }

  @Bean
  @SneakyThrows
  public GoogleCredentials googleCredentials() {
    if (firebaseProperties.getGoogleCredentials() != null) {
      return GoogleCredentials.fromStream(new ClassPathResource(firebaseProperties.getGoogleCredentials()).getInputStream());
    }
    else {
      // Use standard credentials chain. Useful when running inside GKE
      return GoogleCredentials.getApplicationDefault();
    }
  }

  @Bean
  public Validator validator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    return factory.getValidator();
  }
}
