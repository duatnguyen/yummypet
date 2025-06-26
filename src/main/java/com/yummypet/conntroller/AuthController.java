package com.yummypet.conntroller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yummypet.config.JwtService;
import com.yummypet.dto.ApiResponse;
import com.yummypet.dto.ath.LoginResponse;
import com.yummypet.entity.CreateUserRequest;
import com.yummypet.entity.LoginRequest;
import com.yummypet.entity.RegisterRequest;
import com.yummypet.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequest request) {
        authService.registerCustomer(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công"));
    }

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse<Void>> createUser(@RequestBody CreateUserRequest request) {
        authService.createEmployee(request);
        return ResponseEntity.ok(ApiResponse.success("Tạo tài khoản nhân viên thành công"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", loginResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody String token) {
        authService.logout(token);
        return ResponseEntity.ok(ApiResponse.success("Đăng xuất thành công"));
    }

}
