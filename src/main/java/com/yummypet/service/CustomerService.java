package com.yummypet.service;

import com.yummypet.entity.Customer;
import com.yummypet.entity.User;
import com.yummypet.exception.AccessDeniedException;
import com.yummypet.repository.CustomerRepository;
import com.yummypet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Pageable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor

public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
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

    public Customer getCustomerByCode(String customerCode) {
        return customerRepository.findByCustomerCode(customerCode)
                .orElseThrow(()-> new IllegalArgumentException("Không tìm thấy khách hàng với mã: " + customerCode));
    }

    public Customer getCustomerByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với username: " + username));
        return customerRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin khách hàng cho người dùng này"));
    }

    public Customer getCustomerByPhone(String phone) {
        return customerRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với số điện thoại: " + phone));
    }

    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Page<Customer> getActiveCustomer(Pageable pageable) {
        return customerRepository.findAllActive(pageable);
    }

    public Page<Customer> searchCustomersByPhonePartial(String phonePartial, Pageable pageable) {
        return customerRepository.findByPhoneContaining(phonePartial, pageable);
    }

    public Page<Customer> searchCustomers(String keyword, Pageable pageable) {
        return customerRepository.searchCustomers(keyword, pageable);
    }

    public List<Customer> getTopCustomersByLoyaltyPoints(Integer minPoints, Integer limit) {
        return customerRepository.findTopCustomersByLoyaltyPoints(minPoints, Pageable.ofSize(limit));
    }

    @Transactional
    public Customer updateLoyaltyPoints(Integer customerId, Integer points) {
        Customer customer = getCustomerById(customerId);
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + points);
        customer.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        log.info("Cập nhật điểm thưởng cho khách hàng {}: {} điểm", customer.getCustomerCode());
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer deactivateCustomer(Integer id) {
        Customer customer = getCustomerById(id);
        customer.setIsActive(false);
        customer.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        log.info("Vô hiệu hóa tài khoản khách hàng: {}", customer.getCustomerCode());
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer activateCustomer(Integer id) {
        Customer customer = getCustomerById(id);
        customer.setIsActive(true);
        customer.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        log.info("Kích hoạt tài khoản khách hàng: {}", customer.getCustomerCode());
        return customerRepository.save(customer);
    }
    private boolean isCustomerOwnerOrAdmin(Customer customer, String username) {
        if(customer.getUser() == null) {
            return false;
        }
        if(customer.getUser().getUsername().equals(username)) {
            return true;
        }
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getRole() != null) {
            return "admin".equals(userOpt.get().getRole().getName());
        }
        return false;
    }
}
