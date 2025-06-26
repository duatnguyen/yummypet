package com.yummypet.service;

import org.springframework.stereotype.Service;

@Service    
public class CodeGeneratorService {
    public String generateCustomerCode() {
        return "CUS" + System.currentTimeMillis();
    }

    public String generateEmployeeCode() {  
        return "EMP" + System.currentTimeMillis();
    }
}
