package com.example.rent_read.controller;

import com.example.rent_read.exchange.book.BookResponse;
import com.example.rent_read.exchange.customer.CustomerResponse;
import com.example.rent_read.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @GetMapping
  public ResponseEntity<List<CustomerResponse>> findAll() {
    List<CustomerResponse> responses = customerService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
    CustomerResponse response = customerService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{id}/books")
  public ResponseEntity<List<BookResponse>> getRentedBooks(@PathVariable Long id) {
    List<BookResponse> list = customerService.getRentedBooks(id);
    return ResponseEntity.status(HttpStatus.OK).body(list);
  }

}
