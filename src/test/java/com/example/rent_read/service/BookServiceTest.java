package com.example.rent_read.service;

import com.example.rent_read.data.BookEntity;
import com.example.rent_read.data.CustomerEntity;
import com.example.rent_read.exception.book.*;
import com.example.rent_read.exchange.book.BookResponse;
import com.example.rent_read.repository.BookRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

  @Mock
  private BookRepository bookRepository;

  @Mock
  private CustomerRepository customerRepository;

  private ModelMapper modelMapper;

  @InjectMocks
  private BookService bookService;

  @BeforeEach
  void setup() {
    modelMapper = new ModelMapper();
    bookService = new BookService(bookRepository, customerRepository, modelMapper);
  }

  @Test
  void testRentBookSuccess() {
    Long bookId = 1L;
    String email = "test@example.com";
    CustomerEntity customerEntity = new CustomerEntity();
    customerEntity.setEmail(email);
    BookEntity bookEntity = new BookEntity();
    bookEntity.setId(bookId);
    bookEntity.setIsAvailable(true);
    customerEntity.setBookEntityList(new ArrayList<>());

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null));

    Mockito.when(customerRepository.findOneByEmail(email)).thenReturn(Optional.of(customerEntity));
    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
    Mockito.when(bookRepository.save(Mockito.any(BookEntity.class))).thenReturn(bookEntity);

    BookResponse bookResponse = bookService.rentBook(bookId);

    Assertions.assertNotNull(bookResponse);
    Assertions.assertEquals(bookId, bookResponse.getId());
    Assertions.assertEquals(false, bookResponse.getIsAvailable());
    Mockito.verify(bookRepository).save(bookEntity);
  }

  @Test
  void testRentBookForBookNotFound() {
    Long bookId = 1L;
    String email = "test@example.com";
    CustomerEntity customerEntity = new CustomerEntity();
    customerEntity.setEmail(email);

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null));

    Mockito.when(customerRepository.findOneByEmail(email)).thenReturn(Optional.of(customerEntity));

    assertThrows(BookNotFoundException.class, () -> {
      bookService.rentBook(bookId);
    });

  }

  @Test
  void testRentBookForBookNotAvailable() {
    Long bookId = 1L;
    String email = "test@example.com";
    CustomerEntity customerEntity = new CustomerEntity();
    customerEntity.setEmail(email);
    BookEntity bookEntity = new BookEntity();
    bookEntity.setIsAvailable(false);

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null));

    Mockito.when(customerRepository.findOneByEmail(email)).thenReturn(Optional.of(customerEntity));
    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));

    assertThrows(BookNotAvailableException.class, () -> {
      bookService.rentBook(bookId);
    });

  }

  @Test
  void testRentBookForRentalLimitReached() {
    Long bookId = 1L;
    String email = "test@example.com";
    CustomerEntity customerEntity = new CustomerEntity();
    customerEntity.setEmail(email);
    customerEntity.setBookEntityList(List.of(new BookEntity(), new BookEntity()));

    BookEntity bookEntity = new BookEntity();
    bookEntity.setId(bookId);
    bookEntity.setIsAvailable(true);

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null));

    Mockito.when(customerRepository.findOneByEmail(email)).thenReturn(Optional.of(customerEntity));
    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));

    assertThrows(BookRentalLimitReachedException.class, () -> {
      bookService.rentBook(bookId);
    });

  }

  @Test
  public void testReturnBookSuccess() {
    Long bookId = 1L;
    String email = "test@example.com";

    BookEntity bookEntity = new BookEntity();
    bookEntity.setId(bookId);
    bookEntity.setIsAvailable(false);

    CustomerEntity customerEntity = new CustomerEntity();
    customerEntity.setEmail(email);

    bookEntity.setCustomerEntity(customerEntity);

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null));

    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
    Mockito.when(bookRepository.save(Mockito.any(BookEntity.class))).thenReturn(bookEntity);

    BookResponse bookResponse = bookService.returnBook(bookId);

    Assertions.assertNotNull(bookResponse);
    Assertions.assertEquals(bookId, bookResponse.getId());
    Assertions.assertEquals(true, bookResponse.getIsAvailable());
    Mockito.verify(bookRepository).save(bookEntity);

  }

  @Test
  public void testReturnBookForBookNotRented() {
    Long bookId = 1L;
    String email = "test@example.com";

    BookEntity bookEntity = new BookEntity();
    bookEntity.setId(bookId);
    bookEntity.setIsAvailable(true);

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null));

    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));

    assertThrows(BookNotRentedException.class, () -> {
      bookService.returnBook(bookId);
    });

  }

  @Test
  public void testReturnBookForBookRentedByDifferentCustomer() {
    Long bookId = 1L;
    String email = "test@example.com";

    BookEntity bookEntity = new BookEntity();
    bookEntity.setId(bookId);
    bookEntity.setIsAvailable(false);

    CustomerEntity customerEntity = new CustomerEntity();
    customerEntity.setEmail("user@example.com");

    bookEntity.setCustomerEntity(customerEntity);

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null));

    Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));

    assertThrows(BookRentedByDifferentCustomerException.class, () -> {
      bookService.returnBook(bookId);
    });

  }

  @Test
  public void testReturnBookForBookNotFound() {
    Long bookId = 1L;
    String email = "test@example.com";

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null));

    assertThrows(BookNotFoundException.class, () -> {
      bookService.returnBook(bookId);
    });

  }

}
