package com.bingeForge.demo.entity;

import com.bingeForge.demo.enums.ContentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Table(name = "watch_history")
public class WatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private UUID movieId;

    private UUID seriesId;

    private UUID episodeId;

    private Integer seasonNo;

    private Integer episodeNo;

    @Column(nullable = false)
    private Integer lastWatchedPositionSeconds = 0;

    @Column(nullable = false)
    private Integer totalDurationSeconds = 0;

    @Column( nullable = false)
    private Boolean isCompleted = false;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastWatchedAt;
}
