package com.twitter.repositories;

import com.twitter.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findByDeleted(Boolean bool);
    User findByCredentialsUsername(String username);
    User findByCredentialsUsernameAndCredentialsPassword(String username, String password);
}
