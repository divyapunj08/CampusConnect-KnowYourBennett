package com.campusconnect.controller;

import com.campusconnect.model.Event;
import com.campusconnect.model.Student;
import com.campusconnect.repository.RegistrationRepository;
import com.campusconnect.service.AnnouncementService;
import com.campusconnect.service.EventService;
import com.campusconnect.service.RegistrationService;
import com.campusconnect.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import com.campusconnect.model.Registration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import com.campusconnect.model.Registration;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private EventService eventService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        Student student = studentService
                .getStudentByEmail(auth.getName()).orElse(null);
        model.addAttribute("student", student);

        // Get all events
        List<Event> allEvents = eventService.getAllEvents();

        // Main events only (not club events) — sorted newest date first
        List<Event> mainEvents = allEvents.stream()
                .filter(e -> e.getCategory() != null &&
                        !e.getCategory().equals("Club-Tech") &&
                        !e.getCategory().equals("Club-Cultural"))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(java.util.stream.Collectors.toList());
        model.addAttribute("events", mainEvents);

        // Featured events for special banner
        List<Event> featuredEvents = allEvents.stream()
                .filter(e -> e.isFeatured())
                .collect(java.util.stream.Collectors.toList());
        model.addAttribute("featuredEvents", featuredEvents);

        // Count of recent events added (last 7 days)
        long recentCount = allEvents.stream()
                .filter(e -> e.getDate() != null &&
                        e.getDate().isAfter(
                                java.time.LocalDate.now().minusDays(7)))
                .count();
        model.addAttribute("recentCount", recentCount);

        // Announcements
        model.addAttribute("announcements",
                announcementService.getActiveAnnouncements());

        return "student-dashboard";
    }

    @GetMapping("/club-events")
    public String clubEvents(Model model, Authentication auth) {
        Student student = studentService
                .getStudentByEmail(auth.getName()).orElse(null);
        model.addAttribute("student", student);

        List<Event> allEvents = eventService.getAllEvents();
        List<Event> clubEvents = allEvents.stream()
                .filter(e -> e.getCategory() != null &&
                        (e.getCategory().equals("Club-Tech") ||
                                e.getCategory().equals("Club-Cultural")))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(java.util.stream.Collectors.toList());
        model.addAttribute("clubEvents", clubEvents);
        return "club-events";
    }

    @GetMapping("/events")
    public String viewEvents(@RequestParam(required = false) String category, Model model) {
        if (category != null && !category.isEmpty()) {
            model.addAttribute("events", eventService.getEventsByCategory(category));
            model.addAttribute("selectedCategory", category);
        } else {
            model.addAttribute("events", eventService.getAllEvents());
            model.addAttribute("selectedCategory", "all");
        }
        return "event-list";
    }

    @GetMapping("/events/{id}")
    public String viewEventDetails(@PathVariable Long id, Model model) {
        eventService.getEventById(id).ifPresent(event ->
                model.addAttribute("event", event));
        return "event-details";
    }

    // Show the registration form page
    @GetMapping("/events/{id}/register")
    public String showRegisterForm(@PathVariable Long id,
                                   Authentication auth, Model model,
                                   RedirectAttributes redirectAttributes) {

        Event event = eventService.getEventById(id).orElse(null);
        if (event == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Event not found!");
            return "redirect:/student/events";
        }

        // Check if already registered
        Student student = studentService
                .getStudentByEmail(auth.getName()).orElse(null);
        if (registrationRepository
                .existsByStudentAndEvent(student, event)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "You are already registered for " + event.getTitle() + "!");
            return "redirect:/student/events/" + id;
        }

        // Check seats
        if (event.getMaxParticipants() > 0) {
            long count = registrationRepository
                    .findByEvent(event).size();
            if (count >= event.getMaxParticipants()) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Sorry! No seats available for " + event.getTitle() + ".");
                return "redirect:/student/events/" + id;
            }
        }

        // Pre-fill form with student's existing data
        Registration reg = new Registration();
        reg.setFullName(student.getName());
        reg.setEmail(student.getEmail());
        reg.setPhoneNumber(student.getPhone());

        model.addAttribute("event", event);
        model.addAttribute("registration", reg);
        model.addAttribute("student", student);
        return "registration-form";
    }

    // Handle the registration form submission
    @PostMapping("/events/{id}/register")
    public String submitRegistration(
            @PathVariable Long id,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String enrollment,
            @RequestParam String department,
            @RequestParam String yearOfStudy,
            @RequestParam(required = false) String additionalInfo,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        try {
            Student student = studentService
                    .getStudentByEmail(auth.getName()).orElse(null);
            Event event = eventService
                    .getEventById(id).orElse(null);

            System.out.println("=== REGISTRATION: "
                    + fullName + " for " + (event != null
                    ? event.getTitle() : "NULL") + " ===");

            if (student == null || event == null) {
                redirectAttributes.addFlashAttribute(
                        "errorMessage", "Session expired or event not found!");
                return "redirect:/student/events";
            }

            // Check already registered
            if (registrationRepository
                    .existsByStudentAndEvent(student, event)) {
                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "You are already registered for "
                                + event.getTitle() + "!");
                return "redirect:/student/events/" + id;
            }

            // Check seats
            if (event.getMaxParticipants() > 0) {
                long count = registrationRepository
                        .findByEvent(event).size();
                if (count >= event.getMaxParticipants()) {
                    redirectAttributes.addFlashAttribute(
                            "errorMessage",
                            "Sorry! No seats available.");
                    return "redirect:/student/events/" + id;
                }
            }

            // Create FRESH Registration — no ID, no binding issues
            Registration reg = new Registration();
            reg.setStudent(student);
            reg.setEvent(event);
            reg.setFullName(fullName);
            reg.setEmail(email);
            reg.setPhoneNumber(phoneNumber);
            reg.setEnrollment(enrollment);
            reg.setDepartment(department);
            reg.setYearOfStudy(yearOfStudy);
            reg.setAdditionalInfo(additionalInfo);
            reg.setStatus("CONFIRMED");
            reg.setRegisteredAt(java.time.LocalDateTime.now());

            Registration saved = registrationRepository.save(reg);

            System.out.println("=== SAVED! ID: "
                    + saved.getId() + " ===");

            redirectAttributes.addFlashAttribute("successMessage",
                    "Successfully registered for "
                            + event.getTitle() + "!");

            return "redirect:/student/registrations/confirmation/"
                    + saved.getId();

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Registration failed. Please try again!");
            return "redirect:/student/events/" + id;
        }
    }
    // Confirmation page after successful registration
    @GetMapping("/registrations/confirmation/{id}")
    public String registrationConfirmation(
            @PathVariable Long id,
            Authentication auth,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            Registration reg = registrationRepository
                    .findById(id).orElse(null);

            System.out.println("=== CONFIRMATION PAGE ===");
            System.out.println("Registration ID: " + id);
            System.out.println("Found: " + (reg != null));

            if (reg == null) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Registration not found!");
                return "redirect:/student/my-registrations";
            }

            model.addAttribute("registration", reg);
            model.addAttribute("event", reg.getEvent());
            return "registration-confirmation";

        } catch (Exception e) {
            System.out.println("CONFIRMATION ERROR: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Could not load confirmation page.");
            return "redirect:/student/my-registrations";
        }
    }
    @GetMapping("/my-registrations")
    public String myRegistrations(Model model, Authentication auth) {
        Student student = studentService
                .getStudentByEmail(auth.getName()).orElse(null);
        model.addAttribute("student", student);

        List<Registration> registrations = registrationRepository
                .findByStudent(student);
        model.addAttribute("registrations", registrations);
        return "my-registrations";
    }
    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {
        Student student = studentService
                .getStudentByEmail(auth.getName()).orElse(null);
        model.addAttribute("student", student);

        List<Registration> registrations = registrationRepository
                .findByStudent(student);
        model.addAttribute("registrationCount", registrations.size());
        model.addAttribute("myRegistrations", registrations);
        return "student-profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute Student student,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        Student existing = studentService
                .getStudentByEmail(auth.getName()).orElse(null);
        if (existing != null) {
            existing.setName(student.getName());
            existing.setPhone(student.getPhone());
            studentService.saveStudent(existing);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Profile updated successfully!");
        }
        return "redirect:/student/profile";
    }

    @GetMapping("/registrations/cancel/{id}")
    public String cancelRegistration(@PathVariable Long id,
                                     Authentication auth,
                                     RedirectAttributes redirectAttributes) {

        try {
            // Find registration directly
            Registration reg = registrationRepository
                    .findById(id).orElse(null);

            if (reg == null) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Registration not found!");
                return "redirect:/student/my-registrations";
            }

            // Security check — only cancel own registrations
            if (!reg.getStudent().getEmail()
                    .equals(auth.getName())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "You cannot cancel this registration!");
                return "redirect:/student/my-registrations";
            }

            String eventName = reg.getEvent().getTitle();
            registrationRepository.delete(reg);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Registration for " + eventName
                            + " cancelled successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Could not cancel. Please try again!");
        }
        return "redirect:/student/my-registrations";
    }

}