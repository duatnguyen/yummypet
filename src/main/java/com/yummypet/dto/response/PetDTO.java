package com.yummypet.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.yummypet.entity.Pet;
import com.yummypet.entity.PetImage;
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

    public static PetDTO toDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setPetCode(pet.getPetCode());
        dto.setCategory(CategoryDTO.toDTO(pet.getCategory()));
        dto.setName(pet.getName());
        dto.setSpecies(pet.getSpecies());
        dto.setBreed(pet.getBreed());
        if (pet.getCategory() != null) {
            dto.setCategory(CategoryDTO.toDTO(pet.getCategory()));
        }
        dto.setGender(pet.getGender());
        dto.setAgeMonths(pet.getAgeMonths());
        dto.setWeight(pet.getWeight());
        dto.setColor(pet.getColor());
        dto.setPrice(pet.getPrice());
        dto.setDescription(pet.getDescription());
        dto.setArrivalDate(pet.getArrivalDate());
        dto.setStatus(pet.getStatus());
        dto.setCertificateInfo(pet.getCertificateInfo());
        dto.setHealthStatus(pet.getHealthStatus());
        dto.setVaccinationStatus(pet.getVaccinationStatus());
        dto.setIsActive(pet.getIsActive());

        if (pet.getCreatedAt() != null) {
            dto.setCreatedAt(pet.getCreatedAt().toLocalDateTime());
        }

        if (pet.getUpdatedAt() != null) {
            dto.setUpdatedAt(pet.getUpdatedAt().toLocalDateTime());
        }

        return dto;
    }

    public static PetDTO toDTOWithImages(Pet pet, List<PetImage> images) {
        PetDTO dto = toDTO(pet);
        if (images != null && !images.isEmpty()) {
            dto.setImages(images.stream()
                    .map(PetImageDTO::toDTO)
                    .collect(Collectors.toList()));

            images.stream()
                    .filter(PetImage::getIsPrimary)
                    .findFirst()
                    .ifPresent(primaryImage -> dto.setPrimaryImageUrl(primaryImage.getImageUrl()));

            if (dto.getPrimaryImageUrl() == null && !images.isEmpty()) {
                dto.setPrimaryImageUrl(images.get(0).getImageUrl());
            }
        }
        return dto;
    }

    public static List<PetDTO> toDTOList(List<Pet> pets) {
        return pets.stream()
                .map(PetDTO::toDTO)
                .collect(Collectors.toList());
    }
}
