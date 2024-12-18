package com.example.rent_read.exchange.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class RegisterRequest {

  @Email(message = "Email should be valid")
  @NotBlank(message = "Email is mandatory")
  private String email;

  @NotBlank(message = "Password is mandatory")
  private String password;

  private String firstName;

  private String lastName;

  @Pattern(regexp = "customer|admin", message = "Role must be either 'customer' or 'admin'")
  private String role;
}
