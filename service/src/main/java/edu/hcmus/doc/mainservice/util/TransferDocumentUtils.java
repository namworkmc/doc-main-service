package edu.hcmus.doc.mainservice.util;


import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingMethod;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import edu.hcmus.doc.mainservice.model.entity.TransferHistory;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentType;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransferDocumentUtils {

  public static int getStep(User reporter, User assignee, Boolean isCreate) {
    int step = 1;
    switch (reporter.getRole()) {
      case VAN_THU:
//        if (assignee.getRole() == GIAM_DOC) {
        step = 1;
//        }
        break;
      case HIEU_TRUONG:
        if (isCreate) {
          step = 2;
        } else {
          step = 1;
        }
        break;
      case TRUONG_PHONG:
        if (isCreate) {
          step = 3;
        } else {
          step = 2;
        }
        break;
      case CHUYEN_VIEN:
        if (isCreate) {
          step = 4;
        } else {
          step = 3;
        }
        break;
      default:
        step = 1;
        break;
    }
    return step;
  }

  public static int getStepOutgoingDocument(User reporter, Boolean isCreate) {
    int step;
    switch (reporter.getRole()) {
      case TRUONG_PHONG -> {
        if (isCreate) {
          step = 2;
        } else {
          step = 1;
        }
      }
      case HIEU_TRUONG -> {
        if (isCreate) {
          step = 3;
        } else {
          step = 2;
        }
      }
      case VAN_THU -> step = 3;

      default -> step = 1;
    }
    return step;
  }

  /**
   * Create transfer history base on transfer type
   *
   * @param reporter       User
   * @param assignee       User
   * @param transferDocDto TransferDocDto
   * @return TransferHistory
   */
  public static TransferHistory createTransferHistory(User reporter, User assignee,
      TransferDocDto transferDocDto, ProcessingMethod processingMethod,
      ProcessingDocumentType transferType) {
    TransferHistory transferHistory = new TransferHistory();
    transferHistory.setSender(reporter);
    transferHistory.setReceiver(assignee);
    transferHistory.setIsTransferToSameLevel(transferDocDto.getIsTransferToSameLevel());
    transferHistory.setIsRead(false);

    if (transferType == ProcessingDocumentType.INCOMING_DOCUMENT) {
      transferHistory.setIncomingDocumentIds(transferDocDto.getDocumentIds());

      if (transferDocDto.getIsTransferToSameLevel()) {
        transferHistory.setIsInfiniteProcessingTime(true);
        return transferHistory;
      }
    } else {
      transferHistory.setOutgoingDocumentIds(transferDocDto.getDocumentIds());
    }

    // set transfer history info
    transferHistory.setIsInfiniteProcessingTime(transferDocDto.getIsInfiniteProcessingTime());
    transferHistory.setProcessingMethod(processingMethod);
    if (Boolean.FALSE.equals(transferDocDto.getIsInfiniteProcessingTime())) {
      transferHistory.setProcessingDuration(LocalDate.parse(
          Objects.requireNonNull(transferDocDto.getProcessingTime()),
          DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    return transferHistory;
  }

  public static ProcessingDocument createProcessingDocument(IncomingDocument incomingDocument,
      OutgoingDocument outgoingDocument, ProcessingStatus processingStatus) {
    ProcessingDocument processingDocument = new ProcessingDocument();
    if (Objects.nonNull(incomingDocument)) {
      processingDocument.setIncomingDoc(incomingDocument);
    } else {
      processingDocument.setOutgoingDocument(outgoingDocument);
    }
    processingDocument.setStatus(processingStatus);
    processingDocument.setOpened(true);
    processingDocument.setProcessingRequest("processing_request");
    return processingDocument;
  }

  public static ProcessingUser createProcessingUser(ProcessingDocument processingDocument,
      User user,
      Integer step, TransferDocDto transferDocDto, ProcessingMethod processingMethod) {
    ProcessingUser processingUser = new ProcessingUser();
    processingUser.setProcessingDocument(processingDocument);
    processingUser.setUser(user);
    processingUser.setStep(step);
    processingUser.setProcessingMethod(processingMethod);

    if (Boolean.FALSE.equals(transferDocDto.getIsInfiniteProcessingTime())) {
      processingUser.setProcessingDuration(LocalDate.parse(
          Objects.requireNonNull(transferDocDto.getProcessingTime()),
          DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
    return processingUser;
  }

  public static ProcessingUserRole createProcessingUserRole(ProcessingUser processingUser,
      ProcessingDocumentRoleEnum role) {
    ProcessingUserRole processingUserRole = new ProcessingUserRole();
    processingUserRole.setProcessingUser(processingUser);
    processingUserRole.setRole(role);

    return processingUserRole;
  }

  public static String formatDocIds(List<Long> docIds) {
    return "[" + docIds.stream()
        .map(Object::toString)
        .collect(Collectors.joining(", ")) + "]";
  }

}
