package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.entity.Folder;
import edu.hcmus.doc.mainservice.repository.FolderRepository;
import edu.hcmus.doc.mainservice.service.FolderService;
import java.time.Year;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class FolderServiceImpl implements FolderService {

  private final FolderRepository folderRepository;

  @Override
  public List<Folder> findAll() {
    return folderRepository.findAllOrderByIdAsc();
  }

  @Override
  public Folder findById(Long id) {
    return folderRepository.findById(id).orElse(null);
  }

  @Override
  public Folder update(Folder folder) {
    return folderRepository.save(folder);
  }

  @Override
  public void resetFolderNextNumberAndUpdateYear() {
    List<Folder> folders = folderRepository.getAllFolders();
    folders.forEach(folder -> {
      folder.setNextNumber(1L);
      folder.setYear(Year.now().getValue());
    });
    folderRepository.saveAll(folders);
  }
}
