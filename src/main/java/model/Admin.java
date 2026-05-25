package com.campusconnect.model;

import jakarta.persistence.*;

// @Entity maps to the 'admins' table in MySQL
// Admins can create/edit/delete events and manage registrations
@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // unique=true — each admin has a unique email for login
    @Column(nullable = false, unique = true)
    private String email;

    // BCrypt hashed password stored in database
    @Column(nullable = false)
    private String password;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}