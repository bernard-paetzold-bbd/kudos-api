package com.bbdgrads.kudos_api.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acting_user_id", nullable = false)
    private User actingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id", nullable = true)
    private User targetUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kudo_id", nullable = true)
    private Kudo kudo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = true)
    private Team team;

    @Column(nullable = true)
    private int eventId;

    @Column(nullable = true)
    private String verboseLog;

    @Column(nullable = false, updatable = false)
    private LocalDateTime logTime;

    @PrePersist
    protected void onCreate() {
        this.logTime = LocalDateTime.now();
    }
}
