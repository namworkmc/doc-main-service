package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.FolderDto;
import edu.hcmus.doc.mainservice.model.entity.Folder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FolderMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "folderName", target = "folderName")
    @Mapping(source = "nextNumber", target = "nextNumber")
    @Mapping(source = "year", target = "year")
    FolderDto toDto(Folder folder);
}
