package com.example.rent_read.service.auth;

import com.example.rent_read.exchange.auth.LoginRequest;
import com.example.rent_read.exchange.auth.LoginResponse;
import com.example.rent_read.exchange.auth.RegisterRequest;
import com.example.rent_read.exchange.auth.RegisterResponse;
import com.example.rent_read.exchange.customer.CustomerRequest;
import com.example.rent_read.exchange.customer.CustomerResponse;
import com.example.rent_read.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private CustomerService customerService;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public RegisterResponse register(RegisterRequest registerRequest) {
    CustomerRequest customerRequest = modelMapper.map(registerRequest, CustomerRequest.class);
    String hashPwd = passwordEncoder.encode(customerRequest.getPassword());
    customerRequest.setPassword(hashPwd);
    if (customerRequest.getRole() == null) customerRequest.setRole("customer");
    customerRequest.setRole("ROLE_" + customerRequest.getRole().toUpperCase());
    CustomerResponse customerResponse = customerService.save(customerRequest);
    return modelMapper.map(customerResponse, RegisterResponse.class);
  }

  private Authentication authenticate(String username, String password) {
    Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    return authenticationManager.authenticate(authentication);
  }

  public LoginResponse login(LoginRequest loginRequest) {
    authenticate(loginRequest.email(), loginRequest.password());
    return new LoginResponse(HttpStatus.OK.getReasonPhrase());
  }

}
