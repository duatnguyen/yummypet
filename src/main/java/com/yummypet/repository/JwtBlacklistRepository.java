package com.yummypet.repository;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yummypet.entity.JwtBlacklist;

@Repository
public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {

    boolean existsByToken(String token);
    

    void deleteByExpiryDateBefore(Date expiryDate);
}
