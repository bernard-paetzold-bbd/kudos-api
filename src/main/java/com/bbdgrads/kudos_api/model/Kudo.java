package com.bbdgrads.kudos_api.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="kudos")
public class Kudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kudo_id;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "sending_user_id", nullable = false)
    private User sending_user_id;

    @ManyToOne
    @JoinColumn(name = "receiving_user_id", nullable = false)
    private User target_user_id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    private Boolean flagged;

    @PrePersist
    protected void onCreate(){
        this.created_at = LocalDateTime.now();
    }

}
