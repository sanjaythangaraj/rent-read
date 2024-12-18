package com.example.rent_read.exception.customer;

public class UnauthorizedAccessException extends RuntimeException {
  public UnauthorizedAccessException() {
    super("Unauthorized access - customers can only access their own resources");
  }

  public UnauthorizedAccessException(String message) {
    super(message);
  }
}
