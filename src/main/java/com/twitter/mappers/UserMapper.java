package com.twitter.mappers;

import com.twitter.dtos.UserResponseDto;
import com.twitter.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring", uses = {ProfileMapper.class, CredentialsMapper.class})
public interface UserMapper {

	@Mapping(target = "username", source = "credentials.username")
    UserResponseDto entityToDto(User user);

    @Mapping(target = "username", source = "credentials.username")
    List<UserResponseDto> entitiesToDtos(List<User> userLikes);

}
