package com.jmp.userservice.mapper;

import com.jmp.userservice.dto.request.UserCreateAccountRequest;
import com.jmp.userservice.dto.request.UserUpdateAccountRequest;
import com.jmp.userservice.dto.response.UserAccountResponse;
import com.jmp.userservice.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "firstName", source = "firstName")
    User fromUserCreateAccountRequestDtoToUserModel(UserCreateAccountRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    void updateUserModelFromUpdateAccountRequestDto(UserUpdateAccountRequest dto, @MappingTarget User user);

    UserAccountResponse fromUserModelToUserAccountResponseDto(User user);
}
