package com.example.rent_read.controller;

import com.example.rent_read.exchange.auth.LoginRequest;
import com.example.rent_read.exchange.auth.LoginResponse;
import com.example.rent_read.exchange.auth.RegisterRequest;
import com.example.rent_read.exchange.auth.RegisterResponse;
import com.example.rent_read.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
    RegisterResponse response = authService.register(registerRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
    LoginResponse response = authService.login(loginRequest);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
