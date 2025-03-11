package com.bbdgrads.kudos_api.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long log_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="acting_user_id", nullable=false)
    private User acting_user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="target_user_id", nullable=false)
    private User target_user;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="kudo_id", nullable=true)
    private Kudo kudo;

    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LogEvent> log_events = new ArrayList<>();
}
