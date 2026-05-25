🎓 CampusConnect: KnowYourBennett
A Full-Stack Event Management System built for Bennett University, solving the real campus problem of fragmented event information across multiple platforms.

📌 About the Project
CampusConnect: KnowYourBennett is a centralized platform that brings all university events, clubs, and announcements into one clean, student-friendly dashboard. Built with a focus on real usability from event discovery to seat tracking and admin management.

✨ Features

🖥️ Centralized dashboard with live events, banners & countdowns
🔍 Smart event discovery with categorization & club-based listings
🎟️ Seamless registration with confirmation tickets & seat tracking
👤 Dedicated student dashboard for registrations & updates
🛠️ Admin panel for event management & data export
📢 Real-time announcements system


🛠️ Tech Stack
Layer:  Technology,
Language: Java,
Framework: Spring Boot,
Security:  Spring Security & BCrypt,
ORM:       JPA / Hibernate,
Database:   MySQL,
Frontend:  Thymeleaf & Bootstrap,
Architecture:    MVC

🚀 How to Run
Prerequisites: 

Java 17+,
MySQL,
Maven

Steps:

Clone the repository:

bashgit clone https://github.com/divyapunj08/CampusConnect-KnowYourBennett.git

Set up MySQL database and update application.properties:

propertiesspring.datasource.url=jdbc:mysql://localhost:3306/campusconnect
spring.datasource.username=your_username
spring.datasource.password=your_password

Run the application:

bash./mvnw spring-boot:run

Open your browser and go to:

http://localhost:8080

💡 Key Learnings

Built a scalable MVC application using Spring Boot & Thymeleaf
Implemented secure authentication with Spring Security & BCrypt
Designed relational databases using JPA/Hibernate
Solved real-world debugging challenges & edge cases
Learned to build user-focused features from concept to deployment


👥 Team
Built collaboratively by:


Kshitij Dhanetwal(Team Leader),
Divyapunj,
Ritesh Gocher


📁 Project Structure
CampusConnect-KnowYourBennett/
│
├── src/

│   ├── main/

│   │   ├── java/          # Java source code (controllers, models, services)

│   │   └── resources/

│   │       ├── templates/ # Thymeleaf HTML templates

│   │       └── static/    # CSS, JS, images

│   └── test/              # Unit tests

├── pom.xml                # Maven dependencies

└── README.md


Built to solve a real problem at Bennett University from concept to a fully functional platform. 🚀
