package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.service.EmailService;
import edu.hcmus.doc.mainservice.util.SendInBlueProperties;
import java.util.List;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.SendSmtpEmail;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class EmailServiceImpl implements EmailService {

  private final ApiKeyAuth apiKeyAuth;

  private final SendInBlueProperties sendInBlueProperties;

  @Override
  public void sendPasswordEmail(String toEmail, String toUsername, String generatedPassword) {
    Properties params = new Properties();
    params.setProperty("parameter", "My param value");
    params.setProperty("subject", "New Subject");
    params.setProperty("username", toUsername);
    params.setProperty("password", generatedPassword);
    log.info("Send email to {}", toUsername);
    sendEmail(2L, toEmail, params);
  }

  @Override
  public void sendTransferEmail(String from, String toEmail, String to,
      String type, String docIds, String action) {
    Properties params = new Properties();
    params.setProperty("parameter", "My param value");
    params.setProperty("subject", "New Subject");
    params.setProperty("to_username", to);
    params.setProperty("from_username", from);
    params.setProperty("type", type);
    params.setProperty("docIds", docIds);
    params.setProperty("action", action);
    log.info("Send transfer email from {} to {}", from, to);
    sendEmail(3L, toEmail, params);
  }

  private void sendEmail(Long templateId, String toEmail, Properties params) {
    TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();
    SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
    Properties headers = new Properties();
    headers.setProperty("Some-Custom-Name", "unique-id-1234");
    sendSmtpEmail.setTemplateId(templateId);
    sendSmtpEmail.setSender(
        new sibModel.SendSmtpEmailSender().name(sendInBlueProperties.getSenderName())
            .email(sendInBlueProperties.getSenderEmail()));
    sendSmtpEmail.setTo(List.of(new sibModel.SendSmtpEmailTo().email(toEmail)));
    sendSmtpEmail.setReplyTo(
        new sibModel.SendSmtpEmailReplyTo().email(sendInBlueProperties.getSenderEmail()));
    sendSmtpEmail.setHeaders(headers);
    sendSmtpEmail.setParams(params);
    try {
      apiInstance.sendTransacEmail(sendSmtpEmail);
    } catch (Exception e) {
      log.error("Exception when calling sendEmail");
      e.printStackTrace();
    }
  }
}
