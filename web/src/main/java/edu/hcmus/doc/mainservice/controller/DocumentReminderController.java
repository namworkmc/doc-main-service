package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DocumentReminderDetailsDto;
import edu.hcmus.doc.mainservice.model.dto.DocumentReminderWrapperDto;
import edu.hcmus.doc.mainservice.model.dto.MobileNotificationMessageDto;
import edu.hcmus.doc.mainservice.model.enums.DocumentReminderStatusEnum;
import edu.hcmus.doc.mainservice.service.DocumentReminderService;
import edu.hcmus.doc.mainservice.util.DocDateTimeUtils;
import edu.hcmus.doc.mainservice.util.mapper.DocumentReminderMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/document-reminders")
public class DocumentReminderController {

  private final DocumentReminderService documentReminderService;
  private final DocumentReminderMapper documentReminderMapper;

  @GetMapping("/current-user/{month}/{year}")
  public Map<LocalDate, Set<DocumentReminderStatusEnum>> getCurrentUserReminderStatusByYearMonth
      (@PathVariable("month") int month, @PathVariable("year") int year) {
    return documentReminderService.getCurrentUserDocumentReminders(year, month);
  }

  @GetMapping("/current-user/{year}")
  public Map<LocalDate, Set<DocumentReminderStatusEnum>> getCurrentUserReminderStatusByYear
      (@PathVariable("year") int year) {
    return documentReminderService.getCurrentUserDocumentReminders(year);
  }

  @GetMapping("/current-user/details/{date}")
  public DocumentReminderWrapperDto getCurrentUserDocumentReminderDetailsByDate
      (@PathVariable("date") @DateTimeFormat(pattern = DocDateTimeUtils.YYYY_MM_DD_FORMAT) LocalDate date) {
    return documentReminderMapper.toDto(documentReminderService.getCurrentUserDocumentRemindersDetailsByTime(date), date);
  }

  @GetMapping("/current-user/details/{month}/{year}")
  public Map<DocumentReminderStatusEnum, Set<DocumentReminderDetailsDto>> getCurrentUserDocumentReminderDetailsByYearMonth
      (@PathVariable("month") int month, @PathVariable("year") int year) {
    return documentReminderService.getCurrentUserDocumentRemindersDetailsByTime(year, month);
  }

  @GetMapping("/current-user/unread/count")
  public long getCurrentUserNotificationCount() {
    return documentReminderService.countUnreadDocumentReminders();
  }

  @SneakyThrows
  @PostMapping("/mobile-notification")
  public void pushMobileNotification(@RequestBody MobileNotificationMessageDto messageDto) {
    documentReminderService.pushMobileNotification(messageDto);
  }

  @PutMapping("/current-user/read")
  public long updateCurrentUserIsOpenedReminders(@RequestBody List<Long> ids) {
    return documentReminderService.updateCurrentUserIsNotOpenedReminders(ids);
  }
}
