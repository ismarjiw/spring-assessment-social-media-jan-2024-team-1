package com.twitter.mappers;

import com.twitter.dtos.CredentialsDto;
import com.twitter.embeddables.Credentials;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface CredentialsMapper {
    Credentials DtoToEntity(CredentialsDto credentialsDto);
}
