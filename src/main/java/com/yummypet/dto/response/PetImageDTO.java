package com.yummypet.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.yummypet.entity.PetImage;

import lombok.Data;

@Data
public class PetImageDTO {
    private Integer id;
    private Integer petId;
    private String imageUrl;
    private String altText;
    private Boolean isPrimary;
    private Integer displayOrder;
    private LocalDateTime createdAt;


    public static PetImageDTO toDTO(PetImage petImage) {
        PetImageDTO dto = new PetImageDTO();
        dto.setId(petImage.getId());
        dto.setPetId(petImage.getPet().getId());
        dto.setImageUrl(petImage.getImageUrl());
        dto.setAltText(petImage.getAltText());
        dto.setIsPrimary(petImage.getIsPrimary());
        dto.setDisplayOrder(petImage.getDisplayOrder());

        return dto;
    }

    public static List<PetImageDTO> toDTOList(List<PetImage> petImages) {
        return petImages.stream()
                .map(PetImageDTO::toDTO)
                .collect(Collectors.toList());
    }
}
