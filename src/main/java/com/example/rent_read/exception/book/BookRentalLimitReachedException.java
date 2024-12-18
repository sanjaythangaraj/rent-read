package com.example.rent_read.exception.book;

public class BookRentalLimitReachedException extends RuntimeException {
  public BookRentalLimitReachedException() {
    super("Book rental limit of 2 already reached");
  }

  public BookRentalLimitReachedException(String message) {
    super(message);
  }
}
