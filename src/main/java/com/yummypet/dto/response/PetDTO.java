package com.yummypet.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.yummypet.enums.Gender;
import com.yummypet.enums.HealthStatus;
import com.yummypet.enums.PetStatus;
import com.yummypet.enums.VaccinationStatus;

import lombok.Data;

@Data
public class PetDTO {
    private Integer id;
    private String petCode;
    private CategoryDTO category;
    private String name;
    private String species;
    private String breed;
    private Gender gender;
    private Integer ageMonths;
    private BigDecimal weight;
    private String color;
    private BigDecimal price;
    private String description;
    private LocalDate arrivalDate;
    private PetStatus status;
    private String certificateInfo;
    private HealthStatus healthStatus;
    private VaccinationStatus vaccinationStatus;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PetImageDTO> images;
    private String primaryImageUrl;

}
