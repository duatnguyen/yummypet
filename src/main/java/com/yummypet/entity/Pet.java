package com.yummypet.entity;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDate;

import com.yummypet.enums.Gender;
import com.yummypet.enums.HealthStatus;
import com.yummypet.enums.PetStatus;
import com.yummypet.enums.VaccinationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pet_code", unique = true, length = 20)
    private String petCode;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String name;

    @Column(nullable = false)
    private String species;

    private String breed;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "age_months")
    private Integer ageMonths;

    private BigDecimal weight;
    private String color;
    private BigDecimal price;
    private BigDecimal costPrice;
    private String description;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Enumerated(EnumType.STRING)
    private PetStatus status = PetStatus.available;

    @Column(name = "certificate_info")
    private String certificateInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "health_status")
    private HealthStatus healthStatus = HealthStatus.unknown;

    @Enumerated(EnumType.STRING)
    @Column(name = "vaccination_status")
    private VaccinationStatus vaccinationStatus = VaccinationStatus.unknown;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Transient
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }
}
