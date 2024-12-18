package com.example.rent_read.exception.customer;

public class CustomerNotFoundException extends RuntimeException{
  public CustomerNotFoundException() {
    super("Customer with given email or id not found");
  }

  public CustomerNotFoundException(String message) {
    super(message);
  }
}
