package edu.hcmus.doc.mainservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DocMainServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(DocMainServiceApplication.class, args);
    log.info("doc-main-service application is running");
  }
}
