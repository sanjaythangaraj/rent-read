package com.example.rent_read.exception.book;

public class BookNotFoundException extends RuntimeException {
  public BookNotFoundException() {
    super("Book with given id not found");
  }
  public BookNotFoundException(String message) {
    super(message);
  }
}
