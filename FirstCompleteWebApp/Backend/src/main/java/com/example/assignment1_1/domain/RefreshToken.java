package com.example.assignment1_1.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private String token;

    @Column(nullable = false)
    @Getter
    @Setter
    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id")
    @Getter
    @Setter
    private AppUser user;
}
