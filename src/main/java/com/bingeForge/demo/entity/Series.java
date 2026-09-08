package com.bingeForge.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "seasons")
@EqualsAndHashCode(of = "id")
@Table(name = "web_series")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200 )
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 1000 )
    private String thumbnailUrl;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "web_series_id", nullable = false)
    @OrderBy("seasonNo ASC")
    private List<Season> seasons = new ArrayList<>();

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
