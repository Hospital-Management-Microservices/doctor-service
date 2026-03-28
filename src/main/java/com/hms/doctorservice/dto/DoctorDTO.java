package com.hms.doctorservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String specialization;
    private String licenseNumber;
    private Long departmentId;
    private String departmentName;
    private Integer experienceYears;
    private Boolean isAvailable;
}