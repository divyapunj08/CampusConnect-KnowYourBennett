package com.campusconnect;

// Spring Boot imports for application startup
import com.campusconnect.model.Admin;
import com.campusconnect.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// @SpringBootApplication enables auto-configuration, component scanning
// and configuration for the Spring Boot application
@SpringBootApplication
public class CampusconnectApplication {

	// main() is the entry point — SpringApplication.run() bootstraps
	// the entire Spring context and starts the embedded Tomcat server
	public static void main(String[] args) {
		SpringApplication.run(CampusconnectApplication.class, args);
	}

	// CommandLineRunner runs this code ONCE after the app starts
	// Used to auto-create the default admin account on first run
	@Bean
	CommandLineRunner createAdmin(AdminRepository adminRepository) {
		return args -> {
			// Check if admin already exists to avoid duplicates
			if (adminRepository.findByEmail("admin@bennett.edu")
					.isEmpty()) {
				Admin admin = new Admin();
				admin.setName("Admin");
				admin.setEmail("admin@bennett.edu");
				// BCryptPasswordEncoder hashes the password securely
				// Never store plain text passwords in the database
				admin.setPassword(
						new BCryptPasswordEncoder()
								.encode("admin"));
				adminRepository.save(admin);
				System.out.println("Admin created successfully!");
			} else {
				System.out.println("Admin already exists!");
			}
		};
	}
}