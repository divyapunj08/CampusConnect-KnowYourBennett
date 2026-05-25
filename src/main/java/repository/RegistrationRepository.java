package com.campusconnect.repository;

import com.campusconnect.model.Event;
import com.campusconnect.model.Registration;
import com.campusconnect.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RegistrationRepository
        extends JpaRepository<Registration, Long> {

    List<Registration> findByStudent(Student student);

    List<Registration> findByEvent(Event event);

    boolean existsByStudentAndEvent(
            Student student, Event event);

    void deleteAllByEvent(Event event);
}