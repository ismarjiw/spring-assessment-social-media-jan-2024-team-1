package com.twitter.mappers;

import com.twitter.dtos.CredentialsDto;
import com.twitter.dtos.ProfileDto;
import com.twitter.embeddables.Credentials;
import com.twitter.embeddables.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ProfileMapper {
    Profile DtoToEntity(ProfileDto profileDto);
}
