package com.yummypet.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale.Category;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yummypet.dto.request.PetImageRequest;
import com.yummypet.dto.request.PetRequest;
import com.yummypet.dto.response.PetDTO;
import com.yummypet.entity.Pet;
import com.yummypet.entity.PetImage;
import com.yummypet.enums.Gender;
import com.yummypet.repository.CategoryRepository;
import com.yummypet.repository.PetImageRepository;
import com.yummypet.repository.PetRepository;
import com.yummypet.service.CodeGeneratorService;

import jakarta.transaction.Transactional;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final PetImageRepository petImageRepository;
    private final CategoryRepository categoryRepository;
    private final CodeGeneratorService codeGeneratorService;

    public PetService(PetRepository petRepository, PetImageRepository petImageRepository, CategoryRepository categoryRepository, CodeGeneratorService codeGeneratorService) {
        this.petRepository = petRepository;
        this.petImageRepository = petImageRepository;
        this.categoryRepository = categoryRepository;
        this.codeGeneratorService = codeGeneratorService;
    }

    public PetDTO createPet(PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setPetCode(petDTO.getPetCode());
        pet.setName(petDTO.getName());
        pet.setSpecies(petDTO.getSpecies());
        pet.setBreed(petDTO.getBreed());
        pet.setGender(petDTO.getGender());
        pet.setAgeMonths(petDTO.getAgeMonths());
        pet.setWeight(petDTO.getWeight());
        pet.setColor(petDTO.getColor());
        pet.setPrice(petDTO.getPrice());
        pet.setDescription(petDTO.getDescription());
        pet.setArrivalDate(petDTO.getArrivalDate());
        pet.setStatus(petDTO.getStatus());
        pet.setCertificateInfo(petDTO.getCertificateInfo());
        pet.setHealthStatus(petDTO.getHealthStatus());
        pet.setVaccinationStatus(petDTO.getVaccinationStatus());
        pet.setIsActive(petDTO.getIsActive());
        pet.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return PetDTO.toDTO(petRepository.save(pet));
    }

    @Transactional
    public List<PetDTO> getAllPets() {
        return petRepository.findAll().stream()
                .map(PetDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PetDTO getPetById(Integer id) {
        return petRepository.findById(id)
                .map(PetDTO::toDTO)
                .orElse(null);
    }

    @Transactional
    public Pet createPet(PetRequest petRequest) {

        Pet pet = new Pet();

        com.yummypet.entity.Category category = categoryRepository.findById(pet.getCategory().getId())
                .orElse(null);
        if (category == null) {
            throw new RuntimeException("Không tìm thấy danh mục");
        }
        pet.setCategory(category);

        String petCode = codeGeneratorService.generatePetCode();
        if (petRepository.existsByPetCode(petCode)) {
            throw new RuntimeException("Mã pet đã tồn tại");
        }
        pet.setPetCode(petCode);

        updatePetFormRequest(pet, petRequest, category);

        pet.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        pet.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        Pet savedPet = petRepository.save(pet);
        if (petRequest.getImages() != null && !petRequest.getImages().isEmpty()) {
            
            // kiểm tra hình ảnh primary
            boolean isPrimary = false;
            for (PetImageRequest imageRequest : petRequest.getImages()) {

                if (Boolean.TRUE.equals(imageRequest.getIsPrimary())) {
                    isPrimary = true;
                    break;
                }
            }

            // lưu hình ảnh
            for (PetImageRequest imageRequest : petRequest.getImages()) {
                PetImage petImage = new PetImage();
                petImage.setPet(savedPet);
                petImage.setImageUrl(imageRequest.getImageUrl());
                petImage.setAltText(imageRequest.getAltText());
                petImage.setIsPrimary(imageRequest.getIsPrimary());
                petImage.setDisplayOrder(imageRequest.getDisplayOrder());
                petImageRepository.save(petImage);
            }
        }

        return petRepository.save(pet);
    }

    @Transactional
    public Pet updatePet(Pet pet) {
        return petRepository.save(pet);
    }

    public void updatePetFormRequest(Pet pet, PetRequest request, com.yummypet.entity.Category category) {
        pet.setCategory(category);
        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        pet.setGender(Gender.valueOf(request.getGender()));
        pet.setAgeMonths(request.getAgeMonths());
        pet.setWeight(request.getWeight());
        pet.setColor(request.getColor());
        pet.setPrice(request.getPrice());
        pet.setCostPrice(request.getCostPrice());
        pet.setDescription(request.getDescription());
        pet.setArrivalDate(request.getArrivalDate());
        pet.setStatus(request.getStatus());
        pet.setCertificateInfo(request.getCertificateInfo());
        pet.setHealthStatus(request.getHealthStatus());
        pet.setVaccinationStatus(request.getVaccinationStatus());
        pet.setIsActive(request.getIsActive());
    }

}
