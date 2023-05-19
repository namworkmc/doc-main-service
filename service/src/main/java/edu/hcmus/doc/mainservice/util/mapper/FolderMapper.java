package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.FolderDto;
import edu.hcmus.doc.mainservice.model.entity.Folder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface FolderMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(target = "folderName", expression = "java(String.format(folder.getFolderName(), folder.getYear()))")
    @Mapping(source = "nextNumber", target = "nextNumber")
    @Mapping(source = "year", target = "year")
    FolderDto toDto(Folder folder);
}
