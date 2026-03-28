package com.hms.doctorservice.service;

import com.hms.doctorservice.dto.DepartmentDTO;
import com.hms.doctorservice.dto.DoctorDTO;
import com.hms.doctorservice.model.Doctor;
import com.hms.doctorservice.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final RestTemplate restTemplate;

    @Value("${department.service.url}")
    private String departmentServiceUrl;

    // ─── CREATE ───────────────────────────────────────────
    public DoctorDTO createDoctor(DoctorDTO dto) {
        // If departmentId provided, fetch department name from Department Service
        if (dto.getDepartmentId() != null) {
            try {
                String url = departmentServiceUrl + "/" + dto.getDepartmentId();
                DepartmentDTO dept = restTemplate.getForObject(url, DepartmentDTO.class);
                if (dept != null) {
                    dto.setDepartmentName(dept.getName());
                }
            } catch (Exception e) {
                log.warn("Could not fetch department info: {}", e.getMessage());
                // Continue without department name — don't block doctor creation
            }
        }

        Doctor doctor = mapToEntity(dto);
        Doctor saved = doctorRepository.save(doctor);
        return mapToDTO(saved);
    }

    // ─── GET ALL ──────────────────────────────────────────
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── GET BY ID ────────────────────────────────────────
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        return mapToDTO(doctor);
    }

    // ─── UPDATE ───────────────────────────────────────────
    public DoctorDTO updateDoctor(Long id, DoctorDTO dto) {
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setSpecialization(dto.getSpecialization());
        existing.setLicenseNumber(dto.getLicenseNumber());
        existing.setExperienceYears(dto.getExperienceYears());
        existing.setIsAvailable(dto.getIsAvailable());

        // Update department if changed
        if (dto.getDepartmentId() != null) {
            existing.setDepartmentId(dto.getDepartmentId());
            try {
                String url = departmentServiceUrl + "/" + dto.getDepartmentId();
                DepartmentDTO dept = restTemplate.getForObject(url, DepartmentDTO.class);
                if (dept != null) {
                    existing.setDepartmentName(dept.getName());
                }
            } catch (Exception e) {
                log.warn("Could not fetch department info: {}", e.getMessage());
            }
        }

        Doctor updated = doctorRepository.save(existing);
        return mapToDTO(updated);
    }

    // ─── DELETE ───────────────────────────────────────────
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }

    // ─── SEARCH BY NAME ───────────────────────────────────
    public List<DoctorDTO> searchDoctorsByName(String name) {
        return doctorRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── GET BY DEPARTMENT ────────────────────────────────
    public List<DoctorDTO> getDoctorsByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ─── MAPPERS ──────────────────────────────────────────
    private Doctor mapToEntity(DoctorDTO dto) {
        return Doctor.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .specialization(dto.getSpecialization())
                .licenseNumber(dto.getLicenseNumber())
                .departmentId(dto.getDepartmentId())
                .departmentName(dto.getDepartmentName())
                .experienceYears(dto.getExperienceYears())
                .isAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true)
                .build();
    }

    private DoctorDTO mapToDTO(Doctor doctor) {
        return DoctorDTO.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .phoneNumber(doctor.getPhoneNumber())
                .specialization(doctor.getSpecialization())
                .licenseNumber(doctor.getLicenseNumber())
                .departmentId(doctor.getDepartmentId())
                .departmentName(doctor.getDepartmentName())
                .experienceYears(doctor.getExperienceYears())
                .isAvailable(doctor.getIsAvailable())
                .build();
    }
}