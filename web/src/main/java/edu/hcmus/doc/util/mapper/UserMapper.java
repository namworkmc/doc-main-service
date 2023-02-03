package edu.hcmus.doc.util.mapper;

import edu.hcmus.doc.model.dto.UserDto;
import edu.hcmus.doc.model.entity.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {

  @Mapping(target = "roles", ignore = true)
  UserDto toDto(User user);
}
