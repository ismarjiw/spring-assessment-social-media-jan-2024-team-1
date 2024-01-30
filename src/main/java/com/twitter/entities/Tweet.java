package com.twitter.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name="tweet")
public class Tweet {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User author;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp posted;

    private boolean deleted;

    @Column
    private String content;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="in_reply_to_id")
    private Tweet inReplyTo;

    @OneToMany(mappedBy="inReplyTo",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tweet> replies=new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="repost_Of_id")
    private Tweet repostOf;

    @OneToMany(mappedBy = "repostOf", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tweet> reposts = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "tweet_hashtags",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"tweet_id", "hashtag_id"})
    )
    private Set<Hashtag> hashtags;

}
