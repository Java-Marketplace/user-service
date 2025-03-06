package com.jmp.userservice.mapper;

import com.jmp.userservice.dto.request.UserCreateRequest;
import com.jmp.userservice.dto.request.UserUpdateRequest;
import com.jmp.userservice.dto.response.UserResponse;
import com.jmp.userservice.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "firstName", source = "firstName")
    User toEntity(UserCreateRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    void updateModel(UserUpdateRequest dto, @MappingTarget User user);

    UserResponse toDto(User user);
}
