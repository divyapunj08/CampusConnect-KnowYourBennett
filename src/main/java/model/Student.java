package com.campusconnect.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// @Entity maps this class to the 'students' table in MySQL
@Entity
@Table(name = "students")
public class Student {

    // Auto-incremented primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // unique=true ensures no two students have same email
    // email is used as the username for login
    @Column(nullable = false, unique = true)
    private String email;

    // Password stored as BCrypt hash — never plain text
    @Column(nullable = false)
    private String password;

    @Column
    private String phone;

    // Automatically set to current date/time when account created
    @Column
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}