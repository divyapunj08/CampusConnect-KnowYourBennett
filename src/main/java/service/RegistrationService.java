package com.campusconnect.service;

import com.campusconnect.model.Event;
import com.campusconnect.model.Registration;
import com.campusconnect.model.Student;
import com.campusconnect.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

// Handles all business logic for event registrations
@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    // Register a student for an event with all form details
    public Registration registerStudentForEvent(
            Student student, Event event,
            Registration formData) {

        // Check 1: Already registered?
        if (registrationRepository
                .existsByStudentAndEvent(student, event)) {
            throw new RuntimeException("ALREADY_REGISTERED");
        }

        // Check 2: Seats full?
        if (event.getMaxParticipants() > 0) {
            long count = registrationRepository
                    .findByEvent(event).size();
            if (count >= event.getMaxParticipants()) {
                throw new RuntimeException("SEATS_FULL");
            }
        }

        // Fill in the details from form
        formData.setStudent(student);
        formData.setEvent(event);
        formData.setStatus("CONFIRMED");
        formData.setRegisteredAt(LocalDateTime.now());

        return registrationRepository.save(formData);
    }
    // Get all registrations for a specific student
    public List<Registration> getRegistrationsByStudent(
            Student student) {
        return registrationRepository.findByStudent(student);
    }

    // Get all registrations for a specific event
    // Used by admin to see who registered
    public List<Registration> getRegistrationsByEvent(
            Event event) {
        return registrationRepository.findByEvent(event);
    }

    // Cancel a registration — verifies it belongs to the student
    public void cancelRegistration(Long registrationId,
                                   String studentEmail) {
        Registration reg = registrationRepository
                .findById(registrationId).orElse(null);
        if (reg != null &&
                reg.getStudent().getEmail().equals(studentEmail)) {
            registrationRepository.delete(reg);
        }
    }
}