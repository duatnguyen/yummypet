package com.yummypet.service;

import com.yummypet.dto.request.EmployeeRequest;
import com.yummypet.dto.request.UpdateEmployeeRequest;
import com.yummypet.dto.response.EmployeeDTO;
import com.yummypet.entity.Employee;
import com.yummypet.entity.Role;
import com.yummypet.entity.User;
import com.yummypet.repository.EmployeeRepository;
import com.yummypet.repository.RoleRepository;
import com.yummypet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor

public class EmployeeService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodeGeneratorService codeGeneratorService; @Transactional
    public EmployeeDTO createEmployee(EmployeeRequest request) {
        Role staffRole = roleRepository.findByName("staff")
                .orElseThrow(() -> new RuntimeException("<UNK> <UNK> <UNK> <UNK>."));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(staffRole)
                .isActive(true)
                .createdAt(Timestamp.from(Instant.now()))
                .build();

        User savedUser = userRepository.save(user);

        Employee employee = Employee.builder()
                .user(savedUser)
                .employeeCode(codeGeneratorService.generateEmployeeCode())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .hireDate(request.getHireDate())
                .salary(request.getSalary())
                .position(request.getPosition())
                .department(request.getDepartment())
                .isActive(request.getIsActive())
                .createdAt(Timestamp.from(Instant.now()))
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Created employee with code: {}", savedEmployee.getEmployeeCode());

        return convertToDTO(savedEmployee);

    }









    private EmployeeDTO convertToDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .username(employee.getUser().getUsername())
                .email(employee.getEmail())
                .fullName(employee.getFullName())
                .phone(employee.getPhone())
                .address(employee.getAddress())
                .dateOfBirth(employee.getDateOfBirth())
                .hireDate(employee.getHireDate())
                .salary(employee.getSalary())
                .position(employee.getPosition())
                .department(employee.getDepartment())
                .isActive(employee.getIsActive())
                .roleName(employee.getUser().getRole().getName())
                .build();
    }



}
