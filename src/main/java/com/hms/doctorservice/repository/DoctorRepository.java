package com.hms.doctorservice.repository;

import com.hms.doctorservice.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Search by first name or last name (case-insensitive)
    List<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    // Find by specialization
    List<Doctor> findBySpecializationIgnoreCase(String specialization);

    // Find available doctors
    List<Doctor> findByIsAvailable(Boolean isAvailable);

    // Find by department
    List<Doctor> findByDepartmentId(Long departmentId);
}