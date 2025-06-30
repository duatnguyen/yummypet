package com.yummypet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yummypet.entity.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
    @Query("SELECT MAX(p.id) FROM Pet p")
    Optional<Integer> findMaxId();

    Optional<Pet> findByPetCode(String petCode);

    Optional<Pet> findById(Integer id);

    boolean existsByPetCode(String petCode);

}
