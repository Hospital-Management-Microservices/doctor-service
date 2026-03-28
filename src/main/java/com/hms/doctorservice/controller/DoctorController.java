package com.hms.doctorservice.controller;

import com.hms.doctorservice.dto.DoctorDTO;
import com.hms.doctorservice.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor Service", description = "APIs for managing doctors in HMS")
@CrossOrigin(origins = "*")
public class DoctorController {

    private final DoctorService doctorService;

    // POST /doctors
    @PostMapping
    @Operation(summary = "Create a new doctor")
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO) {
        DoctorDTO created = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // GET /doctors
    @GetMapping
    @Operation(summary = "Get all doctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // GET /doctors/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    // PUT /doctors/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Update doctor by ID")
    public ResponseEntity<DoctorDTO> updateDoctor(
            @PathVariable Long id,
            @RequestBody DoctorDTO doctorDTO) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctorDTO));
    }

    // DELETE /doctors/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete doctor by ID")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok("Doctor deleted successfully");
    }

    // GET /doctors/search?name=John
    @GetMapping("/search")
    @Operation(summary = "Search doctors by name")
    public ResponseEntity<List<DoctorDTO>> searchDoctors(
            @RequestParam String name) {
        return ResponseEntity.ok(doctorService.searchDoctorsByName(name));
    }

    // GET /doctors/department/{departmentId}
    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get all doctors by department ID")
    public ResponseEntity<List<DoctorDTO>> getDoctorsByDepartment(
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(doctorService.getDoctorsByDepartment(departmentId));
    }
}