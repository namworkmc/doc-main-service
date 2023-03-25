package edu.hcmus.doc.mainservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DocMainServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(DocMainServiceApplication.class, args);
    System.out.println("Application's running");
  }
}
