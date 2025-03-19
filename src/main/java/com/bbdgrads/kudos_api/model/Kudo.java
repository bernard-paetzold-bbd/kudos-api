package com.bbdgrads.kudos_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kudos")
public class Kudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kudoId;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "sending_user_id", nullable = false)
    private User sendingUser;

    @ManyToOne
    @JoinColumn(name = "receiving_user_id", nullable = false)
    private User targetUser;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(nullable = true)
    private Boolean flagged;

    @Column(nullable = true)
    private Boolean read;

    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
    }

    public Kudo(String message, User sendingUser, User targetUser) {
        this.message = message;
        this.sendingUser = sendingUser;
        this.targetUser = targetUser;
        this.flagged = false;
        this.read = false;
    }
}
