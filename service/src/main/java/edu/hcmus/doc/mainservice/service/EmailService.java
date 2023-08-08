package edu.hcmus.doc.mainservice.service;

import java.util.List;

public interface EmailService {
  void sendPasswordEmail(String toEmail, String toUsername, String generatedPassword);
  void sendTransferEmail(String from, String toEmail, String to, String type, String docIds, String action);
}
