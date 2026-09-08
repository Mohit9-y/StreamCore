package com.bingeForge.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
@Table(name = "episodes")
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 1000)
    private String videoUrl;

    @Column(nullable = false)
    private Integer episodeNo;

    @Column(nullable = false, length = 200 )
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 1000 )
    private String thumbnailUrl;

    @Column(nullable = false)
    private Integer durationInSeconds;

    private Integer releaseYear;


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
