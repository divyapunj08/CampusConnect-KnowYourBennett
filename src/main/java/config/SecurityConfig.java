package com.campusconnect.config;

import com.campusconnect.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;
import java.util.Collection;

// @Configuration marks this as a Spring configuration class
// @EnableWebSecurity enables Spring Security's web security support
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inject our custom user details service for loading users
    @Autowired
    private CustomUserDetailsService userDetailsService;

    // BCryptPasswordEncoder is the industry standard for password hashing
    // It uses a salt and multiple rounds making brute force very hard
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DaoAuthenticationProvider connects Spring Security to our database
    // It uses UserDetailsService to load users and PasswordEncoder to verify passwords
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // AuthenticationManager manages the authentication process
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Custom success handler to redirect based on user role
    // Admins go to /admin/dashboard, students to /student/dashboard
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(
                    HttpServletRequest request,
                    HttpServletResponse response,
                    Authentication authentication)
                    throws IOException {

                Collection<? extends GrantedAuthority> authorities =
                        authentication.getAuthorities();

                // Default redirect for students
                String redirectUrl = "/student/dashboard";

                // Check if logged in user has ROLE_ADMIN
                for (GrantedAuthority authority : authorities) {
                    if (authority.getAuthority()
                            .equals("ROLE_ADMIN")) {
                        redirectUrl = "/admin/dashboard";
                        break;
                    }
                }
                response.sendRedirect(redirectUrl);
            }
        };
    }

    // SecurityFilterChain defines the security rules for HTTP requests
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        http
                // Disable CSRF for simplicity (enabled in production)
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        // Public pages — no login needed
                        .requestMatchers("/", "/login", "/register",
                                "/clubs", "/about", "/search",
                                "/css/**", "/js/**", "/images/**").permitAll()
                        // Admin pages — only ROLE_ADMIN can access
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/student/**").authenticated()
                        // All other pages require authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );
        return http.build();
    }
}