package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.enums.BatchJobEnum;
import edu.hcmus.doc.mainservice.service.FolderService;
import edu.hcmus.doc.mainservice.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

  private final FolderService folderService;

  @Scheduled(cron = "${doc.schedule.reset-folder-next-number-cron}")
  public void resetFolderNextNumber() {
    log.info(BatchJobEnum.RESET_FOLDER_NEXT_NUMBER_BATCH_JOB.info);
    BatchJobEnum.RESET_FOLDER_NEXT_NUMBER_BATCH_JOB.executor.accept(folderService);
  }
}
