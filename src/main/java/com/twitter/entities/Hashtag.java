package com.twitter.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name="hashtags")
public class Hashtag {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String label;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp firstUsed;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp lastUsed;

    @ManyToMany(mappedBy = "hashtags")
    private Set<Tweet> tweets;
}
