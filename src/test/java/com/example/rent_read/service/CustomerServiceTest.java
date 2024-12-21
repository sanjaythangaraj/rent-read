package com.example.rent_read.service;

import com.example.rent_read.data.AuthorityEntity;
import com.example.rent_read.data.BookEntity;
import com.example.rent_read.data.CustomerEntity;
import com.example.rent_read.exception.book.BookNotFoundException;
import com.example.rent_read.exception.customer.UnauthorizedAccessException;
import com.example.rent_read.exchange.book.BookResponse;
import com.example.rent_read.exchange.customer.CustomerResponse;
import com.example.rent_read.repository.AuthorityRepository;
import com.example.rent_read.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private AuthorityRepository authorityRepository;

  private ModelMapper modelMapper;

  @InjectMocks
  CustomerService customerService;

  @BeforeEach
  void setup() {
    modelMapper = new ModelMapper();
    customerService = new CustomerService(customerRepository, authorityRepository, modelMapper);
  }

  @Test
  void testGetRentedBooksSuccess() {
    String email = "test@example.com";
    String role = "ROLE_ADMIN";
    Long customerId = 1L;
    Long bookId = 1L;

    BookEntity bookEntity = new BookEntity();
    bookEntity.setId(bookId);
    bookEntity.setIsAvailable(false);

    CustomerEntity customerEntity = new CustomerEntity();
    customerEntity.setEmail(email);
    customerEntity.setBookEntityList(List.of(bookEntity));

    Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(role)));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));

    List<BookResponse> responses = customerService.getRentedBooks(customerId);

    Assertions.assertNotNull(responses);
    Assertions.assertNotEquals(responses.size(), 0);
    Assertions.assertEquals(responses.get(0).getId(), bookId);
    Assertions.assertEquals(responses.get(0).getIsAvailable(), false);
  }

  @Test
  void testGetRentedBooksForUnauthorizedAccess() {
    String email = "test@email.com";
    String role = "ROLE_CUSTOMER";

    Long customerId = 1L;

    CustomerEntity customerEntity = new CustomerEntity();
    customerEntity.setEmail(email);

    Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", null, List.of(new SimpleGrantedAuthority(role)));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));

    assertThrows(UnauthorizedAccessException.class, () -> {
      customerService.getRentedBooks(customerId);
    });

  }

}
