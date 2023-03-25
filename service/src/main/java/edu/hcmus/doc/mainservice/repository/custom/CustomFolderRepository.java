package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.Folder;

import java.util.List;

public interface CustomFolderRepository {
    List<Folder> findAllOrderByIdAsc();
}
