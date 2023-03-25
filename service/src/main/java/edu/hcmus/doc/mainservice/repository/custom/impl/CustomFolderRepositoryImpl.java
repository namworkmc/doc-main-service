package edu.hcmus.doc.mainservice.repository.custom.impl;

import edu.hcmus.doc.mainservice.model.entity.Folder;
import edu.hcmus.doc.mainservice.model.entity.QFolder;
import edu.hcmus.doc.mainservice.repository.custom.CustomFolderRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;

import java.util.List;

public class CustomFolderRepositoryImpl
        extends DocAbstractCustomRepository<Folder>
        implements CustomFolderRepository {
    @Override
    public List<Folder> findAllOrderByIdAsc() {
        return selectFrom(QFolder.folder)
                .orderBy(QFolder.folder.id.asc())
                .fetch();
    }
}
