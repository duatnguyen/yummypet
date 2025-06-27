package com.yummypet.service;

import com.yummypet.entity.Customer;
import com.yummypet.entity.User;
import com.yummypet.exception.AccessDeniedException;
import com.yummypet.repository.CustomerRepository;
import com.yummypet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.beans.Transient;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor

public class CustomesService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodeGeneratorService codeGeneratorService;

    @Transactional
    public Customer createCustomer(Customer customer) throws IllegalAccessException {
        if(!StringUtils.hasText(customer.getCustomerCode())) {
            customer.setCustomerCode(codeGeneratorService.generateCustomerCode());
        }

        if (!codeGeneratorService.isValidCustomerCode(customer.getCustomerCode())) {
            throw new IllegalAccessException("Mã khách hàng không hợp lệ");
        }

        if (customerRepository.existsByCustomerCode(customer.getCustomerCode())) {
            throw new IllegalAccessException("Mã khách hàng đã tồn tai:" + customer.getCustomerCode());

        }
        customer.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        customer.setIsActive(true);
        customer.setLoyaltyPoints(0);

        log.info("Tạo mới khách hàng với mã: {}", customer.getCustomerCode());
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Integer id, Customer customerDetails, String username){
        Customer customer = getCustomerById(id);

        if(customer.getUser() != null && !isCustomerOwnerOrAdmin(customer, username)){
            throw new AccessDeniedException("Bạn không có quyên cập nhật thông tin khách hàng này");
        }

        if (StringUtils.hasText(customerDetails.getFullName())) {
            customer.setFullName(customerDetails.getFullName());
        }

        if (StringUtils.hasText(customerDetails.getPhone())) {
            customer.setPhone(customerDetails.getPhone());
        }

        if (StringUtils.hasText(customerDetails.getEmail())) {
            customer.setEmail(customerDetails.getEmail());
        }

        if (StringUtils.hasText(customerDetails.getAddress())) {
            customer.setAddress(customerDetails.getAddress());
        }

        if (customerDetails.getDateOfBirth() != null) {
            customer.setDateOfBirth(customerDetails.getDateOfBirth());
        }

        if (customerDetails.getGender() != null) {
            customer.setGender(customerDetails.getGender());
        }

        customer.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        log.info("Cập nhật thông tin khách hàng có mã: {}", customer.getCustomerCode());
        return customerRepository.save(customer);
    }



    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + id));
    }

    private boolean isCustomerOwnerOrAdmin(Customer customer, String username) {
        if(customer.getUser() == null) {
            return false;
        }
        if(customer.getUser().getUsername().equals(username)) {
            return true;
        }
//        Optional<User> userOpt = userRepository.findByUsername(username);
//        if (userOpt.isPresent() && userOpt.get().getRole() != null) {
//            return "admin".equals(userOpt.get().getRole().getName());
//        }
        return false;
    }
}
