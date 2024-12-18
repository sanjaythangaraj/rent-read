package com.example.rent_read.exception.book;

public class BookRentedByDifferentCustomerException extends RuntimeException {
  public BookRentedByDifferentCustomerException() {
    super("Can't return a book that hasn't been rented by you");
  }

  public BookRentedByDifferentCustomerException(String message) {
    super(message);
  }
}
