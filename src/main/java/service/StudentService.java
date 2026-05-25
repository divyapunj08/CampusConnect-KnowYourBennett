package com.campusconnect.service;

import com.campusconnect.model.Student;
import com.campusconnect.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

// Handles business logic for student accounts
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Save or update a student record in the database
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // Find student by email — used during login and profile lookup
    // Returns Optional to safely handle case where student not found
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    // Check if email is already registered — used during signup
    // Prevents duplicate accounts
    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }

    // Find student by database ID
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }
}