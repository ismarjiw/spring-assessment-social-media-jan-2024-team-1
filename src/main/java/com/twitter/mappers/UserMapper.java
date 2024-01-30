package com.twitter.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.twitter.dtos.UserResponseDto;
import com.twitter.entities.User;

@Mapper(componentModel="spring", uses = {ProfileMapper.class, CredentialsMapper.class})
public interface UserMapper {
	@Mapping(target = "username", source = "credentials.username")
    UserResponseDto entityToDto(User user);
}
