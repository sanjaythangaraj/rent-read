package com.example.rent_read.exception.book;

public class BookNotAvailableException extends RuntimeException{

  public BookNotAvailableException() {
    super("Book currently not available to rent");
  }

  public BookNotAvailableException(String message) {
    super(message);
  }
}
