package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.UserDto;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.util.mapper.decorator.UserMapperDecorator;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = ComponentModel.SPRING, uses = {DepartmentMapper.class})
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {

  UserDto toDto(User user);

  List<UserDto> toDto(List<User> users);

  User partialUpdate(UserDto userDto, @MappingTarget User user);
}
