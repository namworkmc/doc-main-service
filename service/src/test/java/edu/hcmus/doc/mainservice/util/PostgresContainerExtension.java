package edu.hcmus.doc.mainservice.util;

import static edu.hcmus.doc.mainservice.common.PostgresContainerProps.DATABASE_NAME;
import static edu.hcmus.doc.mainservice.common.PostgresContainerProps.DB_SCRIPTS_PATH;
import static edu.hcmus.doc.mainservice.common.PostgresContainerProps.ENTRYPOINT_INIT_DB_PATH;
import static edu.hcmus.doc.mainservice.common.PostgresContainerProps.IMAGE;
import static edu.hcmus.doc.mainservice.common.PostgresContainerProps.PASSWORD;
import static edu.hcmus.doc.mainservice.common.PostgresContainerProps.PORT;
import static edu.hcmus.doc.mainservice.common.PostgresContainerProps.USERNAME;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Component
@Testcontainers
public class PostgresContainerExtension implements BeforeAllCallback {

  @Container
  private static final PostgreSQLContainer<?> postgreSQLContainer =
      (PostgreSQLContainer<?>) new PostgreSQLContainer(IMAGE)
          .withDatabaseName(DATABASE_NAME)
          .withUsername(USERNAME)
          .withPassword(PASSWORD)
          .withExposedPorts(PORT)
          .withFileSystemBind(DB_SCRIPTS_PATH, ENTRYPOINT_INIT_DB_PATH, BindMode.READ_ONLY)
          .withEnv("TZ", "Asia/Ho_Chi_Minh");

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    postgreSQLContainer.start();
    overrideProperties();
  }

  private void overrideProperties() {
    System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl() + "&stringtype=unspecified");
    System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
    System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
  }
}
