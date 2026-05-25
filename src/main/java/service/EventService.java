package com.campusconnect.service;

import com.campusconnect.model.Event;
import com.campusconnect.repository.EventRepository;
import com.campusconnect.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// @Service marks this as a business logic layer component
// Sits between Controller and Repository layers
@Service
public class EventService {

    // @Autowired injects the repository — Spring creates the instance
    @Autowired
    private EventRepository eventRepository;

    // Needed to delete registrations before deleting an event
    @Autowired
    private RegistrationRepository registrationRepository;

    // Returns all events sorted by date ascending
    public List<Event> getAllEvents() {
        return eventRepository.findAllByOrderByDateAsc();
    }

    // Returns a single event by ID wrapped in Optional
    // Optional prevents NullPointerException if event not found
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    // Saves new event or updates existing event
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    // Delete event — must delete registrations first due to foreign key
    public void deleteEvent(Long id) {
        eventRepository.findById(id).ifPresent(event -> {
            // Delete all student registrations for this event first
            registrationRepository.deleteAll(
                    registrationRepository.findByEvent(event));
        });
        // Now safe to delete the event itself
        eventRepository.deleteById(id);
    }

    // Filter events by category e.g. Fest, Workshop, Sports
    public List<Event> getEventsByCategory(String category) {
        return eventRepository.findByCategory(category);
    }

    // Get all featured events for the special banner section
    public List<Event> getFeaturedEvents() {
        return eventRepository.findAll().stream()
                .filter(e -> e.isFeatured())
                .collect(java.util.stream.Collectors.toList());
    }
}