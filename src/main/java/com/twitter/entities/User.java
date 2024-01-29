package com.twitter.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="user_table")
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp joined;

    private boolean deleted;

    @Embedded
    private Credentials credentials;

    @Embedded
    private Profile profile;

    @OneToMany(mappedBy = "author")
    private Set<Tweet> createdTweets = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "followers_following",
            joinColumns = @JoinColumn(name = "following_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "followers")
    private Set<User> following = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private Set<Tweet> likedTweets = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_mentions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private Set<Tweet> mentionedTweets = new HashSet<>();

    @Embeddable
    public class Credentials {
        private String username;

        private String password;
    }

    @Embeddable
    public class Profile {
        private String firstName;

        private String lastName;

        private String email;

        private String phone;

    }

}
