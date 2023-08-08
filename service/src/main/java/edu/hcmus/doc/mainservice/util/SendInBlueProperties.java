package edu.hcmus.doc.mainservice.util;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "sendinblue")
public class SendInBlueProperties {
  @NotBlank
  private String apiKey;

  @NotBlank
  private String senderEmail;

  @NotBlank
  private String senderName;
}
