package edu.hcmus.doc.mainservice.util;


import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TransferDocumentUtils {

  public static int getStep(User reporter, User assignee, Boolean isCreate) {
    int step = 1;
    switch (reporter.getRole()) {
      case VAN_THU:
//        if (assignee.getRole() == GIAM_DOC) {
        step = 1;
//        }
        break;
      case GIAM_DOC:
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
      case GIAM_DOC -> {
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
      Integer step, ReturnRequest returnRequest, TransferDocDto transferDocDto) {
    ProcessingUser processingUser = new ProcessingUser();
    processingUser.setProcessingDocument(processingDocument);
    processingUser.setUser(user);
    processingUser.setStep(step);
    processingUser.setReturnRequest(returnRequest);
    processingUser.setProcessMethod(transferDocDto.getProcessMethod());

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

}
