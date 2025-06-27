package com.yummypet.service;

import com.yummypet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.yummypet.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeGeneratorService {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Tạo mã khách hàng duy nhất
     * Format: CUS + 6 số (ví dụ: CUS000001)
     */
    @Transactional(readOnly = true)
    public String generateCustomerCode() {
        long maxId = customerRepository.findMaxId().orElse(0);
        String code;
        int attempts = 0;
        final int MAX_ATTEMPTS = 10;

        do {
            code = String.format("CUS%06d", maxId + 1 + attempts);
            attempts++;
        } while (customerRepository.existsByCustomerCode(code) && attempts < MAX_ATTEMPTS);

        if (attempts >= MAX_ATTEMPTS) {
            throw new RuntimeException("Cannot generate unique customer code after " + MAX_ATTEMPTS + " attempts");
        }

        log.debug("Generated customer code: {}", code);
        return code;
    }

    /**
     * Tạo mã đơn hàng duy nhất
     * Format: ORD + YYYYMMDD + 4 số (ví dụ: ORD202506190001)
     */

    /**
     * Validate format của customer code
     */
    public boolean isValidCustomerCode(String code) {
        return code != null && code.matches("^CUS\\d{6}$");
    }

    /**
     * Validate format của order code
     */
    public boolean isValidOrderCode(String code) {
        return code != null && code.matches("^ORD\\d{8}\\d{4}$");
    }

    /**
     * Validate format của return code
     */
    public boolean isValidReturnCode(String code) {
        return code != null && code.matches("^RET\\d{8}\\d{4}$");
    }

    /**
     * Validate format của pet code
     */
    public boolean isValidPetCode(String code) {
        return code != null && code.matches("^PET\\d{6}$");
    }

    /**
     * Validate format của employee code
     */
    public boolean isValidEmployeeCode(String code) {
        return code != null && code.matches("^EMP\\d{6}$");
    }
}
