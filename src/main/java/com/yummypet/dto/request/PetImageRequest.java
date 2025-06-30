package com.yummypet.dto.request;

public class PetImageRequest {
    private String imageUrl;
    private Boolean isPrimary;
    private Integer displayOrder;
    private String altText;

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public String getAltText() {
        return altText;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
