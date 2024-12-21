package com.example.rent_read.service;

import com.example.rent_read.data.BookEntity;
import com.example.rent_read.data.CustomerEntity;
import com.example.rent_read.exception.book.*;
import com.example.rent_read.exception.customer.CustomerNotFoundException;
import com.example.rent_read.exchange.book.BookResponse;
import com.example.rent_read.exchange.book.CreateBookRequest;
import com.example.rent_read.exchange.book.UpdateBookRequest;
import com.example.rent_read.repository.BookRepository;
import com.example.rent_read.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

  private BookRepository bookRepository;

  private CustomerRepository customerRepository;

  private ModelMapper modelMapper;

  public BookService(BookRepository bookRepository, CustomerRepository customerRepository, ModelMapper modelMapper) {
    this.bookRepository = bookRepository;
    this.customerRepository = customerRepository;
    this.modelMapper = modelMapper;
  }

  public List<BookResponse> findAll() {
    List<BookEntity> bookEntities = bookRepository.findAll();
    return bookEntities
        .stream()
        .map(bookEntity -> modelMapper.map(bookEntity, BookResponse.class)).toList();

  }

  public List<BookResponse> findAllAvailableBooks() {
    return bookRepository.findAllByIsAvailableIsTrue().stream()
        .map(bookEntity -> modelMapper.map(bookEntity, BookResponse.class))
        .toList();
  }

  public BookResponse findById(Long id) {
    return bookRepository.findById(id)
        .map(bookEntity -> modelMapper.map(bookEntity, BookResponse.class))
        .orElseThrow(BookNotFoundException::new);
  }

  public BookResponse save(CreateBookRequest createBookRequest) {
    BookEntity bookEntity = modelMapper.map(createBookRequest, BookEntity.class);
    if (bookEntity.getIsAvailable() == null) bookEntity.setIsAvailable(true);
    bookEntity = bookRepository.save(bookEntity);
    return modelMapper.map(bookEntity, BookResponse.class);
  }

  public BookResponse update(Long id, UpdateBookRequest updateBookRequest) {
    BookEntity bookEntity = bookRepository.save(mapToBookEntity(id, updateBookRequest));
    return modelMapper.map(bookEntity, BookResponse.class);
  }

  public BookResponse setBookAvailability(Long id, Boolean isAvailable) {
    BookEntity bookEntity = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    bookEntity.setIsAvailable(isAvailable);
    bookEntity = bookRepository.save(bookEntity);
    return modelMapper.map(bookEntity, BookResponse.class);
  }

  public BookResponse rentBook(Long id) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    CustomerEntity customerEntity = customerRepository.findOneByEmail(email)
        .orElseThrow(CustomerNotFoundException::new);

    BookEntity bookEntity = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    if (!bookEntity.getIsAvailable()) throw new BookNotAvailableException();

    if (customerEntity.getBookEntityList().size() >= 2) throw new BookRentalLimitReachedException();

    bookEntity.setIsAvailable(false);
    bookEntity.setCustomerEntity(customerEntity);
    bookEntity = bookRepository.save(bookEntity);
    return modelMapper.map(bookEntity, BookResponse.class);
  }

  public BookResponse returnBook(Long id) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();

    BookEntity bookEntity = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    if (bookEntity.getIsAvailable()) throw new BookNotRentedException();

    if (!bookEntity.getCustomerEntity().getEmail().equalsIgnoreCase(email)) {
      throw new BookRentedByDifferentCustomerException();
    }

    bookEntity.setIsAvailable(true);
    bookEntity.setCustomerEntity(null);
    bookEntity = bookRepository.save(bookEntity);
    return modelMapper.map(bookEntity, BookResponse.class);
  }

  public List<BookResponse> getBorrower(Long id) {
    BookEntity bookEntity = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    if (bookEntity.getIsAvailable()) return List.of();

    return List.of(modelMapper.map(bookEntity.getCustomerEntity(), BookResponse.class));
  }

  public void deleteById(Long id) {
    findById(id);
    bookRepository.deleteById(id);
  }

  private BookEntity mapToBookEntity(Long id, UpdateBookRequest updateBookRequest) {
    BookEntity bookEntity = modelMapper.map(findById(id),BookEntity.class);
    if (updateBookRequest.getTitle() != null) bookEntity.setTitle(updateBookRequest.getTitle());
    if (updateBookRequest.getGenre() != null) bookEntity.setGenre(updateBookRequest.getGenre());
    if (updateBookRequest.getAuthor() != null) bookEntity.setAuthor(updateBookRequest.getAuthor());
    if (updateBookRequest.getIsAvailable() != null) bookEntity.setIsAvailable(updateBookRequest.getIsAvailable());

    return bookEntity;
  }
}
