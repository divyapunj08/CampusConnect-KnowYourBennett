package com.campusconnect.service;

import com.campusconnect.model.Announcement;
import com.campusconnect.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// Handles CRUD operations for announcements
// Admin creates announcements that appear on student dashboard
@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    // Returns only active announcements sorted newest first
    // Used on student dashboard news section
    public List<Announcement> getActiveAnnouncements() {
        return announcementRepository
                .findByActiveTrueOrderByAnnouncementDateDesc();
    }

    // Save new or updated announcement
    public Announcement save(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    // Delete announcement by ID
    public void delete(Long id) {
        announcementRepository.deleteById(id);
    }

    // Find single announcement by ID for editing
    public Announcement findById(Long id) {
        return announcementRepository.findById(id).orElse(null);
    }

    // Get ALL announcements including inactive ones
    // Used in admin panel to show all
    public List<Announcement> getAll() {
        return announcementRepository.findAll();
    }
}