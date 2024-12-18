package com.example.rent_read.exception.book;

public class BookNotRentedException extends RuntimeException {
  public BookNotRentedException() {
    super("Book has not been rented yet or has been returned");
  }

  public BookNotRentedException(String message) {
    super(message);
  }
}
