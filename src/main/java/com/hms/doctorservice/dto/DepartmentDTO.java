package com.hms.doctorservice.dto;

import lombok.*;

// Used to receive data from Department Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
    private Long id;
    private String name;
    private String description;
    private String location;
    private String contactNumber;
    private String email;
    private Boolean isActive;
}