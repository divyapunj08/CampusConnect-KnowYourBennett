package com.campusconnect.controller;

import com.campusconnect.model.Event;
import com.campusconnect.service.EventService;
import com.campusconnect.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.campusconnect.model.Announcement;
import com.campusconnect.service.AnnouncementService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "admin-dashboard";
    }

    @GetMapping("/events/new")
    public String newEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "event-form";
    }

    @PostMapping("/events/save")
    public String saveEvent(@ModelAttribute Event event,
                            @RequestParam(value="featured", required=false) String featured) {
        // Handle featured checkbox — unchecked = null, checked = "true"
        event.setFeatured(featured != null && featured.equals("true"));
        eventService.saveEvent(event);
        return "redirect:/admin/dashboard?saved";
    }

    @GetMapping("/events/edit/{id}")
    public String editEvent(@PathVariable Long id, Model model) {
        eventService.getEventById(id).ifPresent(event ->
                model.addAttribute("event", event));
        return "event-form";
    }

    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
        } catch (Exception e) {
            return "redirect:/admin/dashboard?deleteError";
        }
        return "redirect:/admin/dashboard?deleted";
    }

    @GetMapping("/events/{id}/registrations")
    public String viewRegistrations(@PathVariable Long id, Model model) {
        eventService.getEventById(id).ifPresent(event -> {
            model.addAttribute("event", event);
            model.addAttribute("registrations",
                    registrationService.getRegistrationsByEvent(event));
        });
        return "event-registrations";
    }

    // Show all announcements in admin
    @GetMapping("/announcements")
    public String announcements(Model model) {
        model.addAttribute("announcements",
                announcementService.getAll());
        return "admin-announcements";
    }

    // New announcement form
    @GetMapping("/announcements/new")
    public String newAnnouncement(Model model) {
        model.addAttribute("announcement", new Announcement());
        return "announcement-form";
    }

    // Save announcement
    @PostMapping("/announcements/save")
    public String saveAnnouncement(
            @ModelAttribute Announcement announcement) {
        announcementService.save(announcement);
        return "redirect:/admin/announcements?saved";
    }

    // Edit announcement
    @GetMapping("/announcements/edit/{id}")
    public String editAnnouncement(@PathVariable Long id,
                                   Model model) {
        model.addAttribute("announcement",
                announcementService.findById(id));
        return "announcement-form";
    }

    // Delete announcement
    @GetMapping("/announcements/delete/{id}")
    public String deleteAnnouncement(@PathVariable Long id) {
        announcementService.delete(id);
        return "redirect:/admin/announcements?deleted";
    }
}