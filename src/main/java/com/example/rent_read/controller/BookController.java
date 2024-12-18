package com.example.rent_read.controller;

import com.example.rent_read.exchange.book.BookResponse;
import com.example.rent_read.exchange.book.CreateBookRequest;
import com.example.rent_read.exchange.book.UpdateBookRequest;
import com.example.rent_read.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

  @Autowired
  private BookService bookService;

  @GetMapping
  public ResponseEntity<List<BookResponse>> findAllBooks(@RequestParam(required = false) String availability) {
    List<BookResponse> bookResponses;
    if (availability != null && availability.equalsIgnoreCase("available")) {
      bookResponses = bookService.findAllAvailableBooks();
    } else {
      bookResponses = bookService.findAll();
    }
    return ResponseEntity.status(HttpStatus.OK).body(bookResponses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<BookResponse> findByBookId(@PathVariable Long id) {
    BookResponse response = bookService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping
  public ResponseEntity<BookResponse> createBook(@RequestBody @Valid CreateBookRequest createBookRequest) {
    BookResponse response = bookService.save(createBookRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<BookResponse> updateBook(
      @PathVariable Long id, @RequestBody @Valid UpdateBookRequest updateBookRequest) {
    BookResponse response = bookService.update(id, updateBookRequest);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BookResponse> deleteVideo(@PathVariable Long id) {
    bookService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/rent")
  public ResponseEntity<BookResponse> rentBook(@PathVariable Long id) {
    BookResponse response = bookService.rentBook(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/{id}/return")
  public ResponseEntity<BookResponse> returnBook(@PathVariable Long id) {
    BookResponse response = bookService.returnBook(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
