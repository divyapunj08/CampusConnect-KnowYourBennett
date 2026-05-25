package com.campusconnect.controller;

import com.campusconnect.model.Event;
import com.campusconnect.model.Student;
import com.campusconnect.service.EventService;
import com.campusconnect.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    @PostMapping("/register")
    public String registerStudent(@ModelAttribute Student student,
                                  Model model) {
        if (studentService.emailExists(student.getEmail())) {
            model.addAttribute("error", "Email already registered!");
            return "register";
        }
        student.setPassword(
                passwordEncoder.encode(student.getPassword()));
        studentService.saveStudent(student);
        return "redirect:/login?registered";
    }

    @GetMapping("/clubs")
    public String clubsPage(Model model) {
        model.addAttribute("allEvents", eventService.getAllEvents());
        return "clubs";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/search")
    public String searchEvents(@RequestParam String query,
                               Model model) {
        String q = query.trim().toLowerCase();

        if (q.isEmpty()) {
            return "redirect:/student/events";
        }

        List<Event> allEvents = eventService.getAllEvents();
        List<Event> results = allEvents.stream()
                .filter(e -> {
                    String title = e.getTitle().toLowerCase();
                    String category = e.getCategory().toLowerCase();
                    // Match whole words only - split title into words
                    String[] words = title.split("\\s+");
                    for (String word : words) {
                        if (word.startsWith(q)) return true;
                    }
                    // Also match exact category
                    if (category.startsWith(q)) return true;
                    // Also match if query is 4+ chars - broader search
                    if (q.length() >= 4 && title.contains(q)) return true;
                    return false;
                })
                .collect(java.util.stream.Collectors.toList());

        model.addAttribute("events", results);
        model.addAttribute("searchQuery", query.trim());
        model.addAttribute("selectedCategory", "all");
        return "event-list";
    }
}