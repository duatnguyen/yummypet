package com.yummypet.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import com.yummypet.config.JwtService;
import com.yummypet.config.UserDetailsImpl;
import com.yummypet.dto.ApiResponse;
import com.yummypet.dto.ath.LoginResponse;
import com.yummypet.entity.CreateUserRequest;
import com.yummypet.entity.Customer;
import com.yummypet.entity.Employee;
import com.yummypet.entity.JwtBlacklist;
import com.yummypet.entity.LoginRequest;
import com.yummypet.entity.RegisterRequest;
import com.yummypet.entity.Role;
import com.yummypet.entity.User;
import com.yummypet.repository.CustomerRepository;
import com.yummypet.repository.EmployeeRepository;
import com.yummypet.repository.JwtBlacklistRepository;
import com.yummypet.repository.PasswordResetTokenRepository;
import com.yummypet.repository.RoleRepository;
import com.yummypet.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CodeGeneratorService codeGeneratorService;
    private final JwtBlacklistRepository jwtBlacklistRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    public ApiResponse<Void> register(RegisterRequest request) {
        return null;
    }

    public void registerCustomer(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Tài khoản đã tồn tại");

        Role role = roleRepository.findByName("customer")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role"));

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .isActive(true)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .role(role)
                .build();
        userRepository.save(user);

        Customer customer = Customer.builder()
                .customerCode(codeGeneratorService.generateCustomerCode())
                .user(user)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .isActive(true)
                .build();
        customerRepository.save(customer);
    }

    public void createEmployee(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Tài khoản đã tồn tại");

        Role role = roleRepository.findByName("employee")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role"));

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .isActive(true)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .role(role)
                .build();
        userRepository.save(user);

        Employee employee = Employee.builder()
                .employeeCode(codeGeneratorService.generateEmployeeCode())
                .hireDate(request.getHireDate())
                .user(user)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .position(request.getPosition())
                .salary(request.getSalary())
                .department(request.getDepartment())
                .dateOfBirth(request.getDateOfBirth())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .isActive(true)
                .build();
        employeeRepository.save(employee);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        User user = ((UserDetailsImpl) auth.getPrincipal()).getUser();
        String token = jwtService.generateToken(user);

        return new LoginResponse(token, user.getRole().getName());
    }

    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);

            if (jwtService.validateToken(jwt)) {
                JwtBlacklist blacklistEntry = JwtBlacklist.builder()
                        .token(jwt)
                        .expiryDate(jwtService.extractExpirationDate(jwt))
                        .build();

                jwtBlacklistRepository.save(blacklistEntry);
            }
        }
    }

}
