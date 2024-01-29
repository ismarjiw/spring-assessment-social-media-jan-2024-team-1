package com.twitter.dtos;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProfileDto {

    @Nullable
    private String firstName;

    @Nullable
    private String lastName;

    @Nonnull
    private String email;

    @Nullable
    private String phone;
}
