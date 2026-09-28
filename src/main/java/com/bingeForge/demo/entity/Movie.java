package com.bingeForge.demo.entity;

import com.bingeForge.demo.enums.VideoStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 1000)
    private String videoUrl;

    @Column(length = 1000)
    private String rawS3Key;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoStatus status = VideoStatus.PENDING;

    @Column(nullable = false, length = 200 )
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 1000 )
    private String thumbnailUrl;

    @Column(length = 1000 )
    private String trailerUrl;

    @Column(nullable = false)
    private Integer durationInSeconds;

    private Integer releaseYear;

    @Column(length = 1000)
    private String castMembers;

    @Column(nullable = false)
    private Boolean isPremium;

    @Column(precision = 10, scale = 2)
    private BigDecimal rentAmount = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}