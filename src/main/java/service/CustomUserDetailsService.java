package com.campusconnect.service;

import com.campusconnect.model.Admin;
import com.campusconnect.model.Student;
import com.campusconnect.repository.AdminRepository;
import com.campusconnect.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

// @Service marks this as a Spring service component
// Implements UserDetailsService — required by Spring Security
// to load user information during login
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Repository to query admin accounts from database
    @Autowired
    private StudentRepository studentRepository;

    // Repository to query student accounts from database
    @Autowired
    private AdminRepository adminRepository;

    // loadUserByUsername() is called by Spring Security during login
    // It looks up the user by email and returns their details + role
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // First check if email belongs to an Admin
        Admin admin = adminRepository.findByEmail(email)
                .orElse(null);
        if (admin != null) {
            // Return UserDetails with ROLE_ADMIN authority
            return new User(
                    admin.getEmail(),
                    admin.getPassword(),
                    Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_ADMIN")
                    )
            );
        }

        // Then check if email belongs to a Student
        Student student = studentRepository.findByEmail(email)
                .orElse(null);
        if (student != null) {
            // Return UserDetails with ROLE_STUDENT authority
            return new User(
                    student.getEmail(),
                    student.getPassword(),
                    Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_STUDENT")
                    )
            );
        }

        // If no user found, throw exception — login will fail
        throw new UsernameNotFoundException(
                "No user found with email: " + email);
    }
}