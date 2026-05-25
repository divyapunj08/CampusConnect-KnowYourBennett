package com.campusconnect.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

// @Entity tells JPA this class maps to a database table
// @Table specifies the exact table name in MySQL
@Entity
@Table(name = "events")
public class Event {

    // @Id marks the primary key field
    // @GeneratedValue AUTO_INCREMENT in MySQL
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable=false) means this field is required in DB
    @Column(nullable = false)
    private String title;

    // length=1000 allows longer descriptions
    @Column(length = 1000)
    private String description;

    // LocalDate stores date only (no time)
    @Column(nullable = false)
    private LocalDate date;

    // LocalTime stores time only (no date)
    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String venue;

    // Category: Fest, Workshop, Seminar, Sports, Cultural,
    // Competition, Club-Tech, Club-Cultural
    @Column
    private String category;

    // 0 = unlimited seats
    @Column
    private int maxParticipants;

    // Optional external registration link
    @Column
    private String registrationUrl;

    // Which club owns this event (for club events)
    @Column
    private String clubName;
    // featured=true shows event as special banner on dashboard
    @Column
    private boolean featured = false;

    // Standard getters and setters — required by JPA and Thymeleaf
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int max) {
        this.maxParticipants = max;
    }

    public String getRegistrationUrl() {
        return registrationUrl;
    }

    public void setRegistrationUrl(String url) {
        this.registrationUrl = url;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public Boolean isFeatured() {
        return Boolean.TRUE.equals(featured);
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }
}