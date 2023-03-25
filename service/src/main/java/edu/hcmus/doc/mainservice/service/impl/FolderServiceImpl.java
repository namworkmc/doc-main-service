package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.entity.Folder;
import edu.hcmus.doc.mainservice.repository.FolderRepository;
import edu.hcmus.doc.mainservice.service.FolderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;

    public FolderServiceImpl(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

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
}
