package com.yummypet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yummypet.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
}
