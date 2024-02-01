package com.twitter.mappers;

import com.twitter.dtos.CredentialsDto;
import com.twitter.embeddables.Credentials;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface CredentialsMapper {
    Credentials dtoToEntity(CredentialsDto credentialsDto);

    Credentials DtoToEntity(CredentialsDto credentialsDto);

}
