package com.yummypet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yummypet.entity.Pet;
import com.yummypet.entity.PetImage;

@Repository
public interface PetImageRepository extends JpaRepository<PetImage, Integer> {
    List<PetImage> findByPetId(Integer petId);

    List<PetImage> findByPetOrderByDisplayOrderAsc(Pet pet);

    List<PetImage> findByPetIdOrderByDisplayOrderAsc(Integer petId);

    List<PetImage> findByPetIdAndIsPrimaryTrue(Integer petId);

    List<PetImage> findByPetIdAndIsPrimaryFalse(Integer petId);

    List<PetImage> findByPetIdAndIsPrimaryTrueOrderByDisplayOrderAsc(Integer petId);

    List<PetImage> findByPetIdAndIsPrimaryFalseOrderByDisplayOrderAsc(Integer petId);
}
