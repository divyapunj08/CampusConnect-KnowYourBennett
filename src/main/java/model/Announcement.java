package com.campusconnect.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// @Entity maps to 'announcements' table
// Admin can create news and announcements shown on student dashboard
@Entity
@Table(name = "announcements")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    // Date the announcement was published
    @Column
    private LocalDate announcementDate;

    // Category: NEWS, ALERT, EVENT, GENERAL
    @Column
    private String category;

    // active=true shows on dashboard, false hides it
    // Allows admin to draft announcements before publishing
    @Column
    private boolean active = true;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public LocalDate getAnnouncementDate() { return announcementDate; }
    public void setAnnouncementDate(LocalDate d) { this.announcementDate = d; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}