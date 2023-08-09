package edu.hcmus.doc.mainservice.service;

public interface EmailService {
  void sendPasswordEmail(String toEmail, String toUsername,  String toFullname, String generatedPassword, boolean isCreateAccount);

  void sendTransferDocumentEmail(String from, String toEmail, String to, String docIds);

  void sendReturnDocumentEmail(String from, String toEmail, String to, String docIds);

  void sendSendBackDocumentEmail(String from, String toEmail, String to, String docIds);
}
