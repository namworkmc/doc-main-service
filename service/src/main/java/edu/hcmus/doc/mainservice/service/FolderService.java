package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.Folder;
import java.util.List;

public interface FolderService {

    List<Folder> findAll();

    Folder findById(Long id);

    Folder update(Folder folder);

    void resetFolderNextNumberAndUpdateYear();
}
