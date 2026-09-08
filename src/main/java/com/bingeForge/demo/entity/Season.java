package com.bingeForge.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "episodes")
@Table(name = "seasons")
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 1000 )
    private String thumbnailUrl;

    @Column(nullable = false)
    private Integer seasonNo;

    @Column(length = 1000 )
    private String trailerUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private Integer releaseYear;

    @Column(length = 1000)
    private String castMembers;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    @OrderBy("episodeNo ASC")
    private List<Episode>episodes = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
