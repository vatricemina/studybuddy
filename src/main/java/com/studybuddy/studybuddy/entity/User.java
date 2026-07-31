package com.studybuddy.studybuddy.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="app_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique=true, nullable=false)
    private String email;

    private String password;
}
