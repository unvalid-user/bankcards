package com.example.bankcards.dto.mapper;

import com.example.bankcards.config.MapStructConfig;
import com.example.bankcards.dto.PagedResponse;
import com.example.bankcards.dto.user.UpdateUserRequest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.entity.User;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {
    UserResponse toResponse(User user);

    @Mapping(target = "page", source = "number")
    PagedResponse<UserResponse> toPagedResponse(Page<User> pageUsers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUserRequest dto, @MappingTarget User user);
}
