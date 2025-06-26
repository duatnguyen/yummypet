package com.yummypet.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private String role;
    private String position;
    private BigDecimal salary;
    private LocalDate dateOfBirth;
    private LocalDate hireDate;
    private String department;
}
