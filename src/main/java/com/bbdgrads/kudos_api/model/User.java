package com.bbdgrads.kudos_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    public User(String username, String googleId, boolean isAdmin) {
        this.username = username;
        this.googleId = googleId;
        this.isAdmin = isAdmin;
    }

    @Id // indicates the below property is the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // No argument => uses default Id gen of postgres
    private Long userId;
    private String username;

    @Column(nullable = false)
    private boolean isAdmin;

    @Column(nullable = false, unique = true)
    private String googleId;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    private Team team;

}
