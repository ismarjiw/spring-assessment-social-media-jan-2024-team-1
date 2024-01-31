package com.twitter.repositories;

import com.twitter.dtos.CredentialsDto;
import com.twitter.embeddables.Credentials;
import com.twitter.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByCredentials(Credentials credentials);
}
