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

    @Id // indicates the below property is the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // No argument => uses default Id gen of postgres
    private Long user_id;
    private String first_name;
    private String last_name;

    @Column(nullable = false)
    private boolean is_admin;

    @Column(unique = true)
    private String username;

}
