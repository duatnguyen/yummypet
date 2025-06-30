package com.yummypet.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.yummypet.entity.Category;
import com.yummypet.enums.CategoryType;

import lombok.Data;

@Data
public class CategoryDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer parentId;
    private String parentName;
    private CategoryType categoryType;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CategoryDTO toDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        if (category.getParent() != null) {
            categoryDTO.setParentId(category.getParent().getId());
            categoryDTO.setParentName(category.getParent().getName());
        }
        categoryDTO.setCategoryType(category.getType());
        categoryDTO.setIsActive(category.getIsActive());
        categoryDTO.setCreatedAt(category.getCreatedAt().toLocalDateTime());
        categoryDTO.setUpdatedAt(category.getUpdatedAt().toLocalDateTime());
        return categoryDTO;
    }

    public static List<CategoryDTO> toDTOList(List<Category> categories) {
        return categories.stream()
                .map(CategoryDTO::toDTO)
                .collect(Collectors.toList());
    }

}
